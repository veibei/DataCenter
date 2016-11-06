package com.songxu.multithread;

import com.songxu.bean.RemoteServerBean;
import com.songxu.memcached.MemecachedOperate;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 * 2015-11-3新增
 * 推送状态线程纳入线程池管理
 *
 * @author songxu
 * @version 2.0
 */
public class PushDymaticInfoV2 implements CoreHandlerV2<Integer> {


    private Logger logger = Logger.getLogger(PushDymaticInfoV2.class);
    /**
     * servlet上下文
     */
    private final ServletContext servletContext;
    private boolean isRun = false;
    private boolean isStop = true;
    private MemecachedOperate MemecachedOperate;

    public PushDymaticInfoV2(ServletContext servletContext) {
        this.servletContext = servletContext;

        ApplicationContext applicationContext = (ApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        MemecachedOperate = (com.songxu.memcached.MemecachedOperate) applicationContext.getBean("memcachedOperate");


    }

    /**
     * 将Memcached服务器动态信息封装成string 方便推送
     * 2015-11-3启动
     *
     * @return
     * @author songxu
     * @version 2.0
     */
    private String getInfoV2() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("i");// 插入标识符 i=info
        stringBuilder.append("|");// 插入分隔符
        stringBuilder.append(MemecachedOperate.get("server.receiveSpeed"));
        stringBuilder.append("|");// 插入分隔符
        stringBuilder.append(MemecachedOperate.get("server.sendSpeed"));
        stringBuilder.append("|");// 插入分隔符
        stringBuilder.append(MemecachedOperate.get("server.receiveRate"));
        stringBuilder.append("|");// 插入分隔符
        stringBuilder.append(MemecachedOperate.get("server.sendRate"));
        stringBuilder.append("|");// 插入分隔符
        stringBuilder.append(MemecachedOperate.get("server.clientCount"));
        stringBuilder.append("|");// 插入分隔符
        stringBuilder.append(MemecachedOperate.get("server.DTUCount"));
        return stringBuilder.toString();
    }

    @Override
    public Integer call() throws Exception {
        isRun = true;
        isStop = false;
        logger.info("dymatic data push thread started");

        while (isRun) {
            try {
                RemoteServerBean remoteServerBean = (RemoteServerBean) MemecachedOperate.get("server.serverBean");

                int statusM = ((RemoteServerBean) MemecachedOperate.get("server.serverBean")).getStatus();
                int statusC = ((RemoteServerBean) servletContext.getAttribute("remoteServerBean")).getStatus();
                if (statusC != statusM) {
                    ExecutorService executor = (ExecutorService) servletContext.getAttribute("threadPool");
                    PushServerInfoV2 pushServerInfoV2 = new PushServerInfoV2(servletContext);
                    executor.submit(pushServerInfoV2);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            @SuppressWarnings("unchecked")
            BlockingQueue<Session> sessions = (BlockingQueue<Session>) servletContext
                    .getAttribute("webSocketSessions");
            if (sessions.size() > 0) {
                String msg = getInfoV2();
                sessions.forEach(session -> {
                    try {
                        if (session.isOpen()) {
                            Basic basic = session.getBasicRemote();
                            synchronized (basic) {
                                basic.sendText(msg);
                            }

                        }
                    } catch (IOException e) {
                        logger.error("推送消息出错");
                    }
                });

            }
        }
        isStop = true;
        logger.info("dymatic data push thread stopped");
        return null;
    }

    @Override
    public void stop() {
        isRun = false;

    }

    @Override
    public boolean getIfStop() {
        return isStop;
    }


}
