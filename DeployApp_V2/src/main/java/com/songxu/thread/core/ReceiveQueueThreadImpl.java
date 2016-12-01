package com.songxu.thread.core;

import com.songxu.core.Server;
import com.songxu.interfaces.Message;
import com.songxu.interfaces.ReceiveQueueThread;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.concurrent.BlockingQueue;

/**
 * 接收队列处理线程 核心线程
 *
 * @author songxu
 */
public class ReceiveQueueThreadImpl implements ReceiveQueueThread, ApplicationContextAware {

    private static Logger logger = Logger
            .getLogger(ReceiveQueueThreadImpl.class);
    private ApplicationContext applicationContext;
    private boolean runMark = true;
    private boolean ifStop = false;

    public ReceiveQueueThreadImpl() {
    }

    @Override
    public void stop() {
        runMark = false;
    }

    @Override
    public boolean getIfStop() {
        return ifStop;
    }

    @Override
    public void run() {
        logger.info("receive queue started...");
        runMark = true;
        ifStop = false;
        Server server = (Server) applicationContext.getBean("server", Server.class);
        while (runMark) {

            BlockingQueue<Message> rBlockingQueue = server
                    .getReceivePool();
            BlockingQueue<Message> sBlockingQueue = server
                    .getSendPool();
            try {
                // 仅仅是简单的搬运工作。。。
                for (int i = 0; i < rBlockingQueue.size(); i++) {
                    sBlockingQueue.put(rBlockingQueue.take());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ifStop = true;
        logger.info("receice queue stopped...");

    }

    @Override
    public void setApplicationContext(ApplicationContext arg0)
            throws BeansException {
        applicationContext = arg0;
    }

}
