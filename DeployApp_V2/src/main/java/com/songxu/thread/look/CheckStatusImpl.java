package com.songxu.thread.look;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.songxu.core.Server;
import com.songxu.interfaces.CheckStatus;
/**
 * 守护线程
 * 检测连接状态
 * 如果有连接关闭  立即将他们从连接池移除
 * @author songxu
 *
 */
public class CheckStatusImpl implements CheckStatus, ApplicationContextAware
{
	private static Logger logger = Logger.getLogger(CheckStatusImpl.class);
	private ApplicationContext applicationContext;
	private volatile boolean runMark = true;
	private volatile boolean ifStop = false;
	private int interval = 5000;//每5秒检测一次

	public CheckStatusImpl()
	{
	}

	@Override
	public void stop()
	{
		runMark = false;
	}

	@Override
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
	public void run()
	{
		runMark = true;
		Server server = (Server) applicationContext.getBean("server",
				Server.class);
		logger.info("DC status monitor started" + Thread.currentThread());
		while (runMark)
		{
			// 检测DTU连接池状态
			Set<Entry<String, IoSession>> entrySet = server.getDTUPool()
					.entrySet();
			Iterator<Entry<String, IoSession>> iterator = entrySet.iterator();
			while (iterator.hasNext())
			{
				/**
				 * 2015-10-27 新增D/C关闭识别
				 */
				Entry<String, IoSession> entry=iterator.next();
				IoSession session = entry.getValue();
				if (!session.isConnected())
				{
					logger.info("DTU connection closed："+entry.getKey());
					iterator.remove();
					
					
				}
			}
			// 检测Client连接池状态
			entrySet = server.getClientPool().entrySet();
			iterator = entrySet.iterator();
			while (iterator.hasNext())
			{
				Entry<String, IoSession> entry=iterator.next();
				IoSession session = entry.getValue();
				if (!session.isConnected())
				{
					logger.info("Client connection closed："+entry.getKey());
					iterator.remove();
				}
			}
			// 检测Temp连接池状态
			entrySet = server.getTempPool().entrySet();
			iterator = entrySet.iterator();
			while (iterator.hasNext())
			{
				IoSession session = iterator.next().getValue();
				if (!session.isConnected())
				{
					iterator.remove();
				}
			}
			
			try
			{
				Thread.sleep(interval);
			}
			catch (InterruptedException e)
			{
			}

		}
		logger.info("DC status monitor stopped" + Thread.currentThread());
		ifStop = true;
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException
	{
		this.applicationContext = arg0;
	}

}
