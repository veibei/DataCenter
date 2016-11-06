package com.songxu.mina.sever;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.songxu.core.Server;
import com.songxu.interfaces.Message;
import com.songxu.util.core.DispatcherImpl4Mina;
/**
 * 核心控制器
 * @author songxu
 * @since 3.0
 *
 */
public class SeverHandler extends IoHandlerAdapter implements ApplicationContextAware
{
	private static Logger logger=Logger.getLogger(SeverHandler.class);
	private Server server;
	private ApplicationContext applicationContext;
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception
	{
		server=(Server)applicationContext.getBean("server",Server.class);
		Message messageGet=(Message)message;
		
		DispatcherImpl4Mina fileterAndDispatcherImpl4Mina=new DispatcherImpl4Mina(messageGet, session,server);
		fileterAndDispatcherImpl4Mina.dispatchReceive();
		
	}
	@Override
	public void sessionOpened(IoSession session) throws Exception
	{
		server=(Server)applicationContext.getBean("server",Server.class);
		//新加入的session  设置其注册步骤为1
		if(!server.isIfAcceptConnect())
		{
			logger.info("server forbib connetion："+session.getRemoteAddress().toString().replace("/", ""));
			session.close(true);
			
		}
		else if(server.getClientThreadPooL().size()>server.getMaxConnections()){
			logger.info("server connetion limits connection refused："+session.getRemoteAddress().toString().replace("/", ""));
			session.close(true);
		}
		else {
			server.getRegisterStep().put(session, 1);
			/**
			 * 2015-10-27 更改ip输出方式 去掉"/"
			 *  共修改五处
			 */
			logger.info("connetion established："+session.getRemoteAddress().toString().replace("/", ""));
		}
		
	}
	
	@Override
	public void sessionClosed(IoSession session) throws Exception
	{
		
		
		logger.info("connection closed："+session.getRemoteAddress().toString().replace("/", ""));
		super.sessionClosed(session);
	}
	@Override
	/**
	 * @author songxu
	 * 2015-10-11 新增异常捕捉功能  去除客户端主动关闭的情况
	 */
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception
	{
		if(cause.getMessage().equals("远程主机强迫关闭了一个现有的连接。"))
		{
			return;
		}
		else {
			logger.error(cause);
			cause.printStackTrace();
		}
		
	}
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException
	{
		this.applicationContext=arg0;
	}

}
