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
 * ��������� ����
 *
 * @author songxu
 * @modify 2015-6-28 ʵ��ApplicationContextAware ��֧��Spring
 * @modify 2015-6-29  �� concurrentSkipListSet	��ҪԪ��ʵ��iosession ʵ��comparable�ӿ�
 * �޸�concurrentSkipListSet ΪconcurrentLinkedQueue
 */
public class Server implements ApplicationContextAware {
    @SuppressWarnings("unused")
    private ApplicationContext applicationContext;
    static Logger logger = Logger.getLogger(Server.class);
    public final static int STATUS_RUNNING = 1;
    public final static int STATUS_STOP = 0;
    /**
     * 2015-11-17 ����  memcached����spring����
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
         * 2015-11-2  ��ӷ�����״̬���µ�memcached
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

    // �ೣ��==================================
    private final ExecutorService executorService;
    /**
     * ����socket����
     */
    private final IoAcceptor acceptor;

    /**
     * ��������
     */
    private AtomicLong receive = new AtomicLong(0);
    /**
     * ���ͷ���
     */
    private AtomicLong send = new AtomicLong(0);

    /**
     * ��������
     */
    private double receiveSpeed = 0.0;
    /**
     * ��������
     */
    private double sendSpeed = 0.0;
    /**
     * Server IMEI 16λ1
     */
    private final String IMEI = "1111111111111111";

    private int maxConnections = 1000;

    /**
     * ��ʱsocket�� ��Ŵ������ֽ׶ε�socket����
     */
    private ConcurrentHashMap<String, IoSession> tempPool;
    /**
     * ���ע�Ჽ���hashmap
     */
    private ConcurrentHashMap<IoSession, Integer> registerStep;
    /**
     * �ͻ���socket�� ����Ѿ���������socket
     */
    private ConcurrentHashMap<String, IoSession> clientPool;
    /**
     * DTUsocket�� ����ѽ������ӵ�DTUSocket
     */
    private ConcurrentHashMap<String, IoSession> DTUPool;

    /**
     * 2015-10-26����  ����ͳ��
     * ���ڼ�¼�������ӵ��������������룩
     */
    private ConcurrentHashMap<String, Double> rateUpCount = new ConcurrentHashMap<>();
    /**
     * 2015-10-26����  ����ͳ��
     * ���ڼ�¼�������ӵ�����������������
     */
    private ConcurrentHashMap<String, Double> rateDownCount = new ConcurrentHashMap<>();
    /**
     * ������Ϣ����
     */
    private BlockingQueue<Message> receivePool;
    /**
     * ������Ϣ����
     */
    private BlockingQueue<Message> sendPool;

    /**
     * �̳߳�
     */
    private ConcurrentLinkedQueue<IoSession> clientThreadPooL;

    /**
     * �Ƿ������Ϣ
     */
    private boolean ifReceiveMsg = true;
    /**
     * �Ƿ�����Ϣ
     */
    private boolean ifSendMsg = true;
    /**
     * �Ƿ������������
     */
    private boolean ifAcceptConnect = true;

    // �����߳�================================
    /**
     * ���ն����߳�
     */
    private ReceiveQueueThread receiveQueueThread;
    /**
     * ���Ͷ����߳�
     */
    private SendQueueThread sendQueueThread;
    // �ػ��߳�================================

    /**
     * ������״̬�߳�
     */
    private CheckQueue checkQueueThread;
    /**
     * ��������״̬�߳�
     */
    private CheckStatus checkStatusThread;
    /**
     * 2015-11-3 ���� �����ϴ���Memcached
     */
    private UploadStatus uploadStatusThread;
    /**
     * ����������ʱ��
     */
    private Date startTime;

    private int status = RemoteServerBean.Status_Stopped;
    /**
     * 2015-11-3 ����
     * Ϊ��memcached������
     */
    private RemoteServerBean remoteServerBean;

    /**
     * 2015-11-6 ����
     * �ϴ������������ݵ�Memcached
     */
    private UploadRateData upLoadRateDataThread;

    // �෽��==================================

    /**
     * �ͷ���Դ
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
     * ��������
     */
    public boolean startSever() {
        if (remoteServerBean.getStatus() == STATUS_RUNNING) {
            return false;
        }

        logger.info("server starting");

        try {
            // �󶨶˿�,����������
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
        startTime = Calendar.getInstance().getTime();//��¼����������ʱ��
        status = RemoteServerBean.Status_Running;
        /**
         * 2015-11-3 ����
         * ������״̬����memcached����
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
     * ֹͣ���� ����ֹͣ�߳� Ȼ���ͷ���Դ
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


        // ֹͣ�ػ��߳�
        try {

            checkQueueThread.stop();
            checkStatusThread.stop();
            uploadStatusThread.stop();
            upLoadRateDataThread.stop();
        } catch (Exception e) {
        }

        // ֹͣ�����߳�
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
            // ����ػ��߳�
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
        // �ͷ���Դ
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
     * ��ý�������
     *
     * @return
     */
    public double getReceive() {
        return (this.receive.get() / 1024.0);
    }

    /**
     * ��÷�������
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
     * applicationContext ע��
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
