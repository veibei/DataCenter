package com.songxu.mina.filter;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.songxu.core.Server;
import com.songxu.interfaces.Message;
/**
 * 流量统计filter
 * @author songxu
 * @modify 2015-06-28 实现了ApplicationContextAware接口 server的单例方法被替换
 */
public class RateStatistics extends FilterAdapter implements ApplicationContextAware
{
	private ApplicationContext ApplicationContext;
	private Server sever;
	
	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message) throws Exception
	{
		sever=(Server)ApplicationContext.getBean("server");
		Message messageR=(Message)message;
		int length=messageR.getString().getBytes().length;//转换为字节
		sever.addReceive(length);
		/**
		 * 2015-10-26 新增imei流量统计
		 */
		Double nowRateUp=sever.getRateUpCount().get(messageR.getIMEI_F());
		if(nowRateUp!=null)
		{
			sever.getRateUpCount().put(messageR.getIMEI_F(),nowRateUp+length);
		}
		super.messageReceived(nextFilter, session, message);
		
	}
	@Override
	public void messageSent(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception
	{
		sever=(Server)ApplicationContext.getBean("server");
		Message messageR=(Message)writeRequest.getMessage();
		int length=messageR.getString().getBytes().length;//转换为字节
		sever.addSend(length);
		/**
		 * 2015-10-26 新增imei流量统计
		 */
		Double nowRateDown=sever.getRateDownCount().get(messageR.getIMEI_F());
		if(nowRateDown!=null)
		{
			sever.getRateDownCount().put(messageR.getIMEI_T(),nowRateDown+length);
		}
		super.messageSent(nextFilter, session, writeRequest);
		
	}
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException
	{
		this.ApplicationContext=arg0;
	}

}
