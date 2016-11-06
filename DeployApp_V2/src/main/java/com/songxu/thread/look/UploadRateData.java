package com.songxu.thread.look;


import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.songxu.core.Server;
import com.songxu.interfaces.LookThread;
import com.songxu.memcached.MemecachedOperate;

public class UploadRateData implements LookThread,ApplicationContextAware
{
	private static Logger logger=Logger.getLogger(UploadRateData.class);
	private  volatile boolean isRun=false;
	private int interval=1*1000;
	private int counter=1;
	private ApplicationContext applicationContext;
	public UploadRateData()
	{
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void run()
	{
		isRun=true;
		logger.info("rate data upload thread started:"+Thread.currentThread());
		Server server = (Server) applicationContext.getBean("server",
				Server.class);
		int sleepTime=interval;
		MemecachedOperate MemecachedOperate=(MemecachedOperate) applicationContext.getBean("memcachedOperate",MemecachedOperate.class);
		
		while(isRun)
		{
			ConcurrentHashMap upRateCount=(ConcurrentHashMap) MemecachedOperate.get("server.UpRateCount");
			ConcurrentHashMap downRateCount=(ConcurrentHashMap) MemecachedOperate.get("server.DownRateCount");
			ConcurrentHashMap serverRateCount=(ConcurrentHashMap) MemecachedOperate.get("server.ServerRateCount");
			
			upRateCount.put(counter%5,server.getRateDownCount());
			downRateCount.put(counter%5, server.getRateUpCount());
			serverRateCount.put(counter%5, server.getSend()+"|"+server.getReceive());
			
			MemecachedOperate.update("server.UpRateCount", upRateCount);
			MemecachedOperate.update("server.DownRateCount", downRateCount);
			MemecachedOperate.update("server.ServerRateCount", serverRateCount);
			
			
			switch (counter%5)
			{
			case 0:
				sleepTime=interval;
				break;
			case 1:
				sleepTime=interval*2;
				break;
			case 2:
				sleepTime=interval*6;
				break;
			case 3:
				sleepTime=interval*10;
				break;
			case 4:
				sleepTime=interval*20;
				break;
			
			}
			counter++;
			try
			{
				/**
				 * 睡眠时间动态变化
				 */
				
				Thread.sleep(sleepTime);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			
			
		}
		isRun=false;
		logger.info("rate data upload thread stopped:"+Thread.currentThread());
	
	}

	@Override
	public void stop()
	{
		isRun=false;
	}

	@Override
	public void setInterval(int interval)
	{
		this.interval=interval;
	}

	@Override
	public boolean getIfStop()
	{
		return isRun;
	}
	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException
	{
		this.applicationContext=arg0;
	}

}
