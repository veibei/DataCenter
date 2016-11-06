package com.songxu.listener;

import com.songxu.multithread.PushDymaticInfoV2;
import com.songxu.multithread.PushLogInfoV2;
import com.songxu.multithread.PushServerInfoV2;
import com.songxu.websocket.ServerEndPoint;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Application Lifecycle Listener implementation class ServletContextInit
 */
@WebListener
public class ServletContextInit implements ServletContextListener {
    private static Logger logger = Logger.getLogger(ServletContextInit.class);

    /**
     * Default constructor.
     */
    public ServletContextInit() {


    }

    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        //关闭线程池
        ExecutorService executor = (ExecutorService) arg0.getServletContext().getAttribute("threadPool");
        executor.shutdown();
    }

    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
        ServletContext servletContext = arg0.getServletContext();

        int capacity = Integer.parseInt(servletContext.getInitParameter("maxConnection"));
        //将所有的session的容器放入application上下文
        BlockingQueue<Session> webSocketSessions = new ArrayBlockingQueue<Session>(capacity);
        servletContext.setAttribute("webSocketSessions", webSocketSessions);

        /**
         * 2015-11-3 引入线程池
         */
        ExecutorService executor = Executors.newCachedThreadPool();
        servletContext.setAttribute("threadPool", executor);


        String ip = arg0.getServletContext().getInitParameter("ip");
        int port = Integer.parseInt(arg0.getServletContext().getInitParameter("port"));

        logger.info("system init:  loading socket information：" + ip + ":" + port);

        //启动推送服务器状态线程
        PushServerInfoV2 pushServerInfoV2 = new PushServerInfoV2(servletContext);
        executor.submit(pushServerInfoV2);


        //启动推送线程
        PushLogInfoV2 pushLogInfo = new PushLogInfoV2(servletContext);
        executor.submit(pushLogInfo);

        PushDymaticInfoV2 pushDymaticInfoV2 = new PushDymaticInfoV2(servletContext);
        executor.submit(pushDymaticInfoV2);


        //添加websocket节点
        addEndpoints(arg0);
        logger.info("webSocket established");
        logger.info("system init finished ");


    }

    /**
     * 添加ws端点
     *
     * @param ctxEvent
     */
    private void addEndpoints(ServletContextEvent ctxEvent) {
        /*********Endpoint *******************/
        final ServerContainer serverContainer = (ServerContainer) ctxEvent
                .getServletContext().getAttribute(
                        "javax.websocket.server.ServerContainer");
        try {
            ServerEndpointConfig c = ServerEndpointConfig.Builder.create(
                    ServerEndPoint.class, "/ws_server").build();
            c.getUserProperties().put("ServletContext",
                    ctxEvent.getServletContext());
            serverContainer.addEndpoint(c);
        } catch (DeploymentException e) {
            logger.error(e.getMessage());
        }
    }

}
