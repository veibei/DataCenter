package com.songxu.core;

import com.songxu.bean.RemoteServerBean;
import com.songxu.interfaces.*;
import com.songxu.memcached.MemecachedOperate;
import com.songxu.thread.look.UploadRateData;
import com.songxu.thread.look.UploadStatus;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 服务核心类 单例
 *
 * @author songxu
 * @modify 2015-6-28 实现ApplicationContextAware 来支持Spring
 * @modify 2015-6-29  因 concurrentSkipListSet	需要元素实现iosession 实现comparable接口
 * 修改concurrentSkipListSet 为concurrentLinkedQueue
 */
public class Server implements ApplicationContextAware {
    @SuppressWarnings("unused")
    private ApplicationContext applicationContext;
    static Logger logger = Logger.getLogger(Server.class);
    public final static int STATUS_RUNNING = 1;
    public final static int STATUS_STOP = 0;
    /**
     * 2015-11-17 新增  memcached纳入spring管理
     */
    private MemecachedOperate MemecachedOperate;

    public Server(IoAcceptor acceptor,
                  ConcurrentHashMap<String, IoSession> tempPool,
                  ConcurrentHashMap<String, IoSession> clientPool,
                  ConcurrentHashMap<String, IoSession> dTUPool,
                  BlockingQueue<Message> receivePool,
                  BlockingQueue<Message> sendPool,
                  ConcurrentLinkedQueue<IoSession> clientThreadPooL,
                  boolean ifReceiveMsg, boolean ifSendMsg, boolean ifAcceptConnect,
                  ReceiveQueueThread receiveQueueThread,
                  SendQueueThread sendQueueThread, CheckQueue checkQueueThread,
                  CheckStatus checkStatusThread, UploadStatus uploadStatusThread, UploadRateData uploadRateDataThread,
                  MemecachedOperate MemecachedOperate) {
        this.MemecachedOperate = MemecachedOperate;
        logger.info("server loading...");

        this.acceptor = acceptor;
        logger.info("socket service established:" + acceptor.getDefaultLocalAddress().toString().replace("/", ""));

        this.tempPool = tempPool;
        logger.info("temp pool established");

        this.clientPool = clientPool;
        logger.info("client pool established");

        this.DTUPool = dTUPool;
        logger.info("DTU pool established");

        this.receivePool = receivePool;
        logger.info("receive queue established with length:" + receivePool.remainingCapacity());

        this.sendPool = sendPool;
        logger.info("send queue established with length :" + sendPool.remainingCapacity());

        this.clientThreadPooL = clientThreadPooL;
        logger.info("client thread pool established");

        this.ifReceiveMsg = ifReceiveMsg;
        logger.info("server receive msg switcher:" + ifReceiveMsg);

        this.ifSendMsg = ifSendMsg;
        logger.info("server send msg switcher:" + ifSendMsg);

        this.ifAcceptConnect = ifAcceptConnect;
        logger.info("server connction switcher:" + ifAcceptConnect);

        this.receiveQueueThread = receiveQueueThread;
        logger.info("receive queue established");

        this.sendQueueThread = sendQueueThread;
        logger.info("send queue established");

        this.checkQueueThread = checkQueueThread;
        logger.info("check queue established");

        this.checkStatusThread = checkStatusThread;
        logger.info("status check thread established");

        this.uploadStatusThread = uploadStatusThread;
        logger.info("status upload thread established");

        this.upLoadRateDataThread = uploadRateDataThread;
        logger.info("rate data thread established");


        registerStep = new ConcurrentHashMap<IoSession, Integer>(100);


        /**
         * 2015-11-2  添加服务器状态更新到memcached
         */
        MemecachedOperate.remove("server.status");
        MemecachedOperate.add("server.status", RemoteServerBean.Status_Stopped);

        InetSocketAddress socketAddress = (InetSocketAddress) this
                .getAcceptor().getDefaultLocalAddress();


        remoteServerBean = new RemoteServerBean();
        remoteServerBean.setHostIP(socketAddress.getHostName());
        remoteServerBean.setPort(socketAddress.getPort());
        remoteServerBean.setMaxConnections(this.getMaxConnections());
        remoteServerBean.setMaxClientConnections(500);
        remoteServerBean.setMaxDtuConnections(500);
        remoteServerBean.setReceiveQueueLength(getReceivePool().remainingCapacity());
        remoteServerBean.setSendQueueLength(getSendPool().remainingCapacity());
        remoteServerBean.setStartTimeDate(null);
        remoteServerBean.setStatus(RemoteServerBean.Status_Stopped);

        MemecachedOperate.remove("server.serverBean");
        MemecachedOperate.add("server.serverBean", remoteServerBean);

        MemecachedOperate.remove("server.receiveSpeed");
        MemecachedOperate.add("server.receiveSpeed", this.getReceiveSpeed());

        MemecachedOperate.remove("server.sendSpeed");
        MemecachedOperate.add("server.sendSpeed", this.getSendSpeed());

        MemecachedOperate.remove("server.receiveRate");
        MemecachedOperate.add("server.receiveRate", getReceive());

        MemecachedOperate.remove("server.sendRate");
        MemecachedOperate.add("server.sendRate", getSend());

        MemecachedOperate.remove("server.clientCount");
        MemecachedOperate.add("server.clientCount", this.getClientPool().size());

        MemecachedOperate.remove("server.DTUCount");
        MemecachedOperate.add("server.DTUCount", this.getDTUPool().size());

        MemecachedOperate.remove("server.DTURateCount");
        MemecachedOperate.remove("server.ClientRateCount");
        MemecachedOperate.remove("server.ServerRateCount");

        MemecachedOperate.add("server.UpRateCount", new ConcurrentHashMap<>());
        MemecachedOperate.add("server.DownRateCount", new ConcurrentHashMap<>());
        MemecachedOperate.add("server.ServerRateCount", new ConcurrentHashMap<>());

        executorService = Executors.newFixedThreadPool(10);

    }

    // 类常量==================================
    private final ExecutorService executorService;
    /**
     * 核心socket控制
     */
    private final IoAcceptor acceptor;

    /**
     * 接收流量
     */
    private AtomicLong receive = new AtomicLong(0);
    /**
     * 发送发送
     */
    private AtomicLong send = new AtomicLong(0);

    /**
     * 接收速率
     */
    private double receiveSpeed = 0.0;
    /**
     * 发送速率
     */
    private double sendSpeed = 0.0;
    /**
     * Server IMEI 16位1
     */
    private final String IMEI = "1111111111111111";

    private int maxConnections = 1000;

    /**
     * 临时socket池 存放处于握手阶段的socket连接
     */
    private ConcurrentHashMap<String, IoSession> tempPool;
    /**
     * 存放注册步骤的hashmap
     */
    private ConcurrentHashMap<IoSession, Integer> registerStep;
    /**
     * 客户端socket池 存放已经建立连接socket
     */
    private ConcurrentHashMap<String, IoSession> clientPool;
    /**
     * DTUsocket池 存放已建立连接的DTUSocket
     */
    private ConcurrentHashMap<String, IoSession> DTUPool;

    /**
     * 2015-10-26新增  流量统计
     * 用于记录所有连接的上行流量（流入）
     */
    private ConcurrentHashMap<String, Double> rateUpCount = new ConcurrentHashMap<>();
    /**
     * 2015-10-26新增  流量统计
     * 用于记录所有连接的下行流量（流出）
     */
    private ConcurrentHashMap<String, Double> rateDownCount = new ConcurrentHashMap<>();
    /**
     * 接收消息队列
     */
    private BlockingQueue<Message> receivePool;
    /**
     * 发送消息队列
     */
    private BlockingQueue<Message> sendPool;

    /**
     * 线程池
     */
    private ConcurrentLinkedQueue<IoSession> clientThreadPooL;

    /**
     * 是否接受消息
     */
    private boolean ifReceiveMsg = true;
    /**
     * 是否发送消息
     */
    private boolean ifSendMsg = true;
    /**
     * 是否接受连接请求
     */
    private boolean ifAcceptConnect = true;

    // 核心线程================================
    /**
     * 接收队列线程
     */
    private ReceiveQueueThread receiveQueueThread;
    /**
     * 发送队列线程
     */
    private SendQueueThread sendQueueThread;
    // 守护线程================================

    /**
     * 检测队列状态线程
     */
    private CheckQueue checkQueueThread;
    /**
     * 检测服务器状态线程
     */
    private CheckStatus checkStatusThread;
    /**
     * 2015-11-3 新增 用于上传至Memcached
     */
    private UploadStatus uploadStatusThread;
    /**
     * 服务器启动时间
     */
    private Date startTime;

    private int status = RemoteServerBean.Status_Stopped;
    /**
     * 2015-11-3 新增
     * 为了memcached的引入
     */
    private RemoteServerBean remoteServerBean;

    /**
     * 2015-11-6 新增
     * 上传各项流量数据到Memcached
     */
    private UploadRateData upLoadRateDataThread;

    // 类方法==================================

    /**
     * 释放资源
     */
    @Deprecated
    private void releaseResouce() {
        checkStatusThread = null;
        checkQueueThread = null;
        sendQueueThread = null;
        receiveQueueThread = null;
        clientPool = null;
        System.gc();
    }

    /**
     * 启动服务
     */
    public boolean startSever() {
        if (remoteServerBean.getStatus() == STATUS_RUNNING) {
            return false;
        }

        logger.info("server starting");

        try {
            // 绑定端口,启动服务器
            acceptor.bind();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return false;
        }
        executorService.submit(receiveQueueThread);
        executorService.submit(sendQueueThread);

        if (checkQueueThread != null) {
            executorService.submit(checkQueueThread);


        }
        if (checkStatusThread != null) {
            executorService.submit(checkStatusThread);

        }
        if (uploadStatusThread != null) {
            executorService.submit(uploadStatusThread);
        }
        if (upLoadRateDataThread != null) {
            executorService.submit(upLoadRateDataThread);
        }
        startTime = Calendar.getInstance().getTime();//记录服务器启动时间
        status = RemoteServerBean.Status_Running;
        /**
         * 2015-11-3 新增
         * 服务器状态放入memcached缓存
         *
         */
        remoteServerBean.setStartTimeDate(startTime);
        remoteServerBean.setStatus(STATUS_RUNNING);
        MemecachedOperate.update("server.status", RemoteServerBean.Status_Running);
        MemecachedOperate.update("server.serverBean", remoteServerBean);

        logger.info("server started");
        return true;
    }

    /**
     * 停止服务 首先停止线程 然后释放资源
     */
    public boolean stopSever() {
        if (remoteServerBean.getStatus() == STATUS_STOP) {
            return false;
        }

        logger.info("stopping service");
        acceptor.unbind();

        MemecachedOperate.update("server.receiveSpeed",
                0);

        MemecachedOperate.update("server.sendSpeed", 0);

        MemecachedOperate.update("server.receiveRate", 0);

        MemecachedOperate.update("server.sendRate", 0);

        MemecachedOperate.update("server.clientCount", 0);

        MemecachedOperate.update("server.DTUCount", 0);


        // 停止守护线程
        try {

            checkQueueThread.stop();
            checkStatusThread.stop();
            uploadStatusThread.stop();
            upLoadRateDataThread.stop();
        } catch (Exception e) {
        }

        // 停止核心线程
        receiveQueueThread.stop();
        sendQueueThread.stop();

        while (!(receiveQueueThread.getIfStop() && sendQueueThread.getIfStop())) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            // 检测守护线程
            while (!checkQueueThread.getIfStop()
                    && checkStatusThread.getIfStop()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {

        }


        status = RemoteServerBean.Status_Stopped;


        remoteServerBean.setStartTimeDate(null);
        remoteServerBean.setStatus(STATUS_STOP);
        MemecachedOperate.update("server.status", RemoteServerBean.Status_Stopped);
        MemecachedOperate.update("server.serverBean", remoteServerBean);

        logger.info("service stopped");
        // 释放资源
        clientPool.clear();
        DTUPool.clear();
        tempPool.clear();
        return true;
    }

    public ConcurrentHashMap<String, IoSession> getTempPool() {
        return tempPool;
    }

    public ConcurrentHashMap<String, IoSession> getClientPool() {
        return clientPool;
    }

    public ConcurrentHashMap<String, IoSession> getDTUPool() {
        return DTUPool;
    }

    public BlockingQueue<Message> getSendPool() {
        return sendPool;
    }

    public BlockingQueue<Message> getReceivePool() {
        return receivePool;
    }

    public ReceiveQueueThread getReceiveQueueThread() {
        return receiveQueueThread;
    }

    public SendQueueThread getSendQueueThread() {
        return sendQueueThread;
    }

    public CheckQueue getCheckQueueThread() {
        return checkQueueThread;
    }

    public CheckStatus getCheckStatusThread() {
        return checkStatusThread;
    }

    public boolean isIfReceiveMsg() {
        return ifReceiveMsg;
    }

    public void setIfReceiveMsg(boolean ifReceiveMsg) {
        this.ifReceiveMsg = ifReceiveMsg;
    }

    public boolean isIfSendMsg() {
        return ifSendMsg;
    }

    public void setIfSendMsg(boolean ifSendMsg) {
        this.ifSendMsg = ifSendMsg;
    }

    public boolean isIfAcceptConnect() {
        return ifAcceptConnect;
    }

    public void setIfAcceptConnect(boolean ifAcceptConnect) {
        this.ifAcceptConnect = ifAcceptConnect;
    }

    public ConcurrentLinkedQueue<IoSession> getClientThreadPooL() {
        return clientThreadPooL;
    }

    public String getIMEI() {
        return IMEI;
    }

    public ConcurrentHashMap<IoSession, Integer> getRegisterStep() {
        return registerStep;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    /**
     * 获得接收流量
     *
     * @return
     */
    public double getReceive() {
        return (this.receive.get() / 1024.0);
    }

    /**
     * 获得发送流量
     *
     * @return
     */
    public double getSend() {
        return (this.send.get() / 1024.0);
    }

    public void addReceive(int add) {
        this.receive.addAndGet(add);
    }

    public void addSend(int add) {
        this.send.addAndGet(add);
    }

    @Override
    /**
     * applicationContext 注入
     */
    public void setApplicationContext(ApplicationContext arg0)
            throws BeansException {
        this.applicationContext = arg0;

    }

    public Date getStartTime() {
        return startTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public IoAcceptor getAcceptor() {
        return acceptor;
    }

    public double getReceiveSpeed() {
        return receiveSpeed;
    }

    public void setReceiveSpeed(double receiveSpeed) {
        this.receiveSpeed = receiveSpeed;
    }

    public double getSendSpeed() {
        return sendSpeed;
    }

    public void setSendSpeed(double sendSpeed) {
        this.sendSpeed = sendSpeed;
    }


    public ConcurrentHashMap<String, Double> getRateDownCount() {
        return rateDownCount;
    }

    public void setRateDownCount(ConcurrentHashMap<String, Double> rateDownCount) {
        this.rateDownCount = rateDownCount;
    }

    public ConcurrentHashMap<String, Double> getRateUpCount() {
        return rateUpCount;
    }

    public void setRateUpCount(ConcurrentHashMap<String, Double> rateUpCount) {
        this.rateUpCount = rateUpCount;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public RemoteServerBean getRemoteServerBean() {
        return remoteServerBean;
    }

    public void setRemoteServerBean(RemoteServerBean remoteServerBean) {
        this.remoteServerBean = remoteServerBean;
    }

}
