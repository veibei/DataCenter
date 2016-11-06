package com.songxu.websocket;

import javax.servlet.ServletContext;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.util.concurrent.BlockingQueue;

public class ServerEndPoint extends Endpoint {

    /**
     * �������Ӵ�ʱ  ��session���������� ���������߳���webҳ��������Ϣ
     */
    @Override
    public void onOpen(Session arg0, EndpointConfig arg1) {
        ServletContext servletContext = (ServletContext) arg0.getUserProperties().get("ServletContext");
        BlockingQueue<Session> sessions = (BlockingQueue<Session>) servletContext.getAttribute("webSocketSessions");
        try {
            sessions.put(arg0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClose(Session session, CloseReason closeReason) {
        ServletContext servletContext = (ServletContext) session.getUserProperties().get("ServletContext");
        BlockingQueue<Session> sessions = (BlockingQueue<Session>) servletContext.getAttribute("webSocketSessions");
        sessions.remove(session);
        super.onClose(session, closeReason);
    }

}
