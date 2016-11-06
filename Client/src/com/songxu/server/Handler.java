package com.songxu.server;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.songxu.interfaces.Message;

public class Handler extends IoHandlerAdapter 
{
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		
		Message message2=(Message)message;
		System.out.println("服务器收到消息："+message2.getString());
	}

}
