package com.songxu.test;

import org.apache.mina.core.session.IoSession;

import com.songxu.core.Server;
import com.songxu.interfaces.Message;
import com.songxu.interfaces.MessageImpl;
import com.songxu.util.core.DispatcherImpl4Mina;

public class SendHandler 
{
	
	Server server;
	public SendHandler(Server server) {
		this.server=server;
	
	}
	public void send(Message message)
	{
		IoSession ioSession;
		if(server.getClientPool().containsKey(message.getIMEI_T()))
		{
			ioSession=server.getClientPool().get(message.getIMEI_T());
		}
		else if(server.getDTUPool().containsKey(message.getIMEI_T()))
		{
			ioSession=server.getDTUPool().get(message.getIMEI_T());
		}
		else {
			return;
		}
		
		DispatcherImpl4Mina fileterAndDispatcherImpl4Mina=new DispatcherImpl4Mina(message, ioSession,server);
		fileterAndDispatcherImpl4Mina.dispatchSend();
	}
	/**
	 * loop测试
	 * @param message
	 */
	public void sendMyself(Message message)
	{
		IoSession ioSession;
		ioSession=server.getClientPool().get(message.getIMEI_F());
		
		//重新构造
		Message messageSend=new MessageImpl(message.getStatus(), server.getIMEI(), message.getIMEI_F(), "111");
		
		DispatcherImpl4Mina fileterAndDispatcherImpl4Mina=new DispatcherImpl4Mina(messageSend, ioSession,server);
		fileterAndDispatcherImpl4Mina.dispatchSend();
		
	}
	

}
