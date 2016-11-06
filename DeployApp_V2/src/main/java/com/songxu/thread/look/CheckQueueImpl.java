package com.songxu.thread.look;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.songxu.core.Server;
import com.songxu.interfaces.CheckQueue;
/**
 * 目前被用来获得上下行速率
 * @author songxu
 *
 */
public class CheckQueueImpl implements CheckQueue,ApplicationContextAware
{
	private static Logger logger=Logger.getLogger(CheckQueueImpl.class);
	private ApplicationContext applicationContext;
	private boolean runMark=true;
	private boolean ifStop=false;
	private int interval=5000;
	public CheckQueueImpl()
	{
	}
	@Override
	public void stop()
	{
		runMark=false;
	}

	@Override
	public void setInterval(int interval)
	{
		this.interval=interval;
	}

	@Override
	public boolean getIfStop()
	{
		return ifStop;
	}


	@Override
	public void run()
	{
		runMark=true;
		logger.info("check queue deamon thread started: "+Thread.currentThread());
		Server server=(Server)applicationContext.getBean("server",Server.class);
		while(runMark)
		{
			try
			{
				/*BlockingQueue<Message> receiveQueue=server.getReceivePool();
				BlockingQueue<Message> sendQueue=server.getSendPool();
				logger.info("接收队列长度："+receiveQueue.size());
				logger.info("发送队列长度："+sendQueue.size());
				*/
				double sendRate1=server.getSend();
				Thread.sleep(1000);
				double sendRate2=server.getSend();
				server.setSendSpeed(sendRate2-sendRate1);
				
				double receiveRate1=server.getReceive();
				Thread.sleep(1000);
				double receiveRate2=server.getReceive();
				server.setReceiveSpeed(receiveRate2-receiveRate1);
				
			}
			catch (InterruptedException e)
			{
			}
		}
		logger.info("check queue deamon thread stopped "+Thread.currentThread());
		ifStop=true;

	}
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException
	{
		this.applicationContext=arg0;
	}

}
