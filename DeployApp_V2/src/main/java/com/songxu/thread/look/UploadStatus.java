package com.songxu.thread.look;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.songxu.core.Server;
import com.songxu.interfaces.CoreHandler;
import com.songxu.memcached.MemecachedOperate;

/**
 * 2015-11-3 新增 用于上传服务器状态数据到Memcached服务器
 * 
 * @author songxu
 *
 */
public class UploadStatus implements CoreHandler, ApplicationContextAware
{
	private static Logger logger = Logger.getLogger(CheckQueueImpl.class);
	private ApplicationContext applicationContext;
	private boolean runMark = true;
	private boolean ifStop = false;
	/**
	 * 默认上传间隔为1000
	 */
	private int interval = 1000;

	@Override
	public void stop()
	{
		runMark = false;
	}

	public void setInterval(int interval)
	{
		this.interval = interval;
	}

	@Override
	public boolean getIfStop()
	{
		return ifStop;
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException
	{
		this.applicationContext = arg0;
	}

	@Override
	public void run()
	{
		runMark = true;
		logger.info("status upload thread started" + Thread.currentThread());
		Server server = (Server) applicationContext.getBean("server",
				Server.class);
		MemecachedOperate MemecachedOperate=(MemecachedOperate) applicationContext.getBean("memcachedOperate",MemecachedOperate.class);
		
		while (runMark)
		{
			try
			{
				MemecachedOperate.update("server.receiveSpeed",
						server.getReceiveSpeed());

				MemecachedOperate.update("server.sendSpeed", server.getSendSpeed());

				MemecachedOperate.update("server.receiveRate", server.getReceive());

				MemecachedOperate.update("server.sendRate", server.getSend());

				MemecachedOperate.update("server.clientCount", server.getClientPool().size());

				MemecachedOperate.update("server.DTUCount", server.getDTUPool()
						.size());
				Thread.sleep(interval);
						

			}
			catch (Exception e)
			{
			}
			
		}
		logger.info("status upload thread stopped " + Thread.currentThread());
		ifStop = true;
	}
}
