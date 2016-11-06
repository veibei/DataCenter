package com.songxu.util.core;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.songxu.core.Server;
import com.songxu.interfaces.ClientSocketThread;
import com.songxu.interfaces.Dispatcher;
import com.songxu.interfaces.Message;
import com.songxu.interfaces.Register;

public class DispatcherImpl4Mina implements Dispatcher
{
	private static Logger logger = Logger
			.getLogger(DispatcherImpl4Mina.class);
	public static final int TEST = 0;
	public static final int REGISTER = 1;
	public static final int DS = 2;
	public static final int CS = 3;
	private  final Server server;
	private final Message msg;
	private final IoSession session;
	
	public DispatcherImpl4Mina(Message msg,IoSession session,Server server)
	{
		this.server = server;
		this.msg = msg;
		this.session=session;
	}

	
	@Override
	public int checkDataType()
	{
		String status_markString = msg.getStatus().substring(0, 1);
		switch (status_markString)
		{
		case "R":
			return REGISTER;
		case "D":
			return DS;
		case "C":
			return CS;
		case "0":
			return TEST;
		default:
			return -1;
		}
	}

	@Override
	public void dispatchReceive()
	{
		switch (checkDataType())
			{
			case REGISTER:
				Register register=new RegisterImpl4Mina(session,msg,server);
				register.regist();
				return;

			case TEST:
				// 测试数据 直接放进消息队列
				server.getTempPool().put(msg.getIMEI_F(), session);
				break;
			case CS:
				if (server.getClientPool().containsKey(msg.getIMEI_F())
						&& server.getDTUPool().containsKey(msg.getIMEI_T()))
				{
					//如果from to双方均存在  则放入消息队列
					
					break;
				}
				else if(msg.getIMEI_T().equals(server.getIMEI()))//服务器测试数据 可以进入
				{
					
					break;
				}
				else {
					return;
				}

			case DS:
				if (server.getDTUPool().containsKey(msg.getIMEI_F())
						&& server.getClientPool().containsKey(msg.getIMEI_T()))
				{
					//如果from to 双发均存在 则放入消息队列 
					break;
				}
				else if(msg.getIMEI_T().equals(server.getIMEI()))//服务器测试数据 可以进入
				{
					break;
				}
				else {
					return;
				}
			}
			try
			{	server.getReceivePool().put(msg);
				//logger.debug("消息入队：from:"+msg.getIMEI_F()+" to:"+msg.getIMEI_T()+" ,消息内容："+msg.getMsg());
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
				logger.error("消息入队失败");
			}

			
			
			
		
	}

	@Override
	public void dispatchSend()
	{
		if(server.isIfSendMsg())
		{
			session.write(msg);
			
			
		}
		else {
			logger.info("server refuse send msg");
		}
	}

	


	@Override
	public int getClientType()
	{
		String mark=msg.getIMEI_F().substring(0,1);
		int mark_Point=Integer.parseInt(mark);
		if(mark_Point==2)
		{
			return DS;
		}
		else if(mark_Point==3)
		{
			return CS;
		}
		else {
			return -1;
		}
	}

	@Override
	@Deprecated
	public void setClientSocketThread(ClientSocketThread clientSocketThread)
	{
		// TODO Auto-generated method stub
		
	}

}
