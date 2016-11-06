package com.songxu.mina.filter;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.songxu.core.Server;
/**
 * 拦截器  拦截所有上下行流量
 * @author songxu
 *
 */
public class ReceiveAndSendFilter extends FilterImplment implements ApplicationContextAware
{
	private static Logger   logger=Logger.getLogger(ReceiveAndSendFilter.class);
	private ApplicationContext application;
	private Server sever;
	@Override
	public void init() throws Exception
	{
		logger.info("receive filter load");
	}
	@Override
	public void destroy() throws Exception
	{
		logger.info("receive filer removed");
	}
	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message) throws Exception
	{
		sever=(Server)application.getBean("server");
		if(sever.isIfReceiveMsg())
		{
			nextFilter.messageReceived(session, message);
		}
		else {
			logger.info("server refuse receive msg...");
		}
	}
	@Override
	public void messageSent(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception
	{
		sever=(Server)application.getBean("server");
		if(sever.isIfSendMsg())
		{
			nextFilter.messageSent(session, writeRequest);
		}
		else {
			logger.info("server refuse send msg");
		}
	}
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException
	{
		this.application=arg0;
	}
	
}
