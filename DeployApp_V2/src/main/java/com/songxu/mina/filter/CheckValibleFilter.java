package com.songxu.mina.filter;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.songxu.interfaces.Message;

/**
 * 核心拦截器
 * @author songxu
 *
 */
public class CheckValibleFilter extends  FilterImplment
{

	private static Logger logger=Logger.getLogger(CheckValibleFilter.class);
	@Override
	public void init() throws Exception
	{
		logger.info("msg check filter load");
	}

	@Override
	public void destroy() throws Exception
	{
		logger.info("msg check filter removed");
	}

	
	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message) throws Exception
	{
		Message messageR=(Message)message;
		
		if(messageR==null||!(messageR.checkDataValible()))
		{
			logger.info("illegal connection："+session.getRemoteAddress().toString().replace("/", ""));
			session.close(true);//立即关闭连接
		}
		else 
		{	
			nextFilter.messageReceived(session, message);
		}
		
	}

}
