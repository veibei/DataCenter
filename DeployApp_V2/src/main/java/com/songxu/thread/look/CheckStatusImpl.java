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
 * �ػ��߳�
 * �������״̬
 * ��������ӹر�  ���������Ǵ����ӳ��Ƴ�
 * @author songxu
 *
 */
public class CheckStatusImpl implements CheckStatus, ApplicationContextAware
{
	private static Logger logger = Logger.getLogger(CheckStatusImpl.class);
	private ApplicationContext applicationContext;
	private volatile boolean runMark = true;
	private volatile boolean ifStop = false;
	private int interval = 5000;//ÿ5����һ��

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
			// ���DTU���ӳ�״̬
			Set<Entry<String, IoSession>> entrySet = server.getDTUPool()
					.entrySet();
			Iterator<Entry<String, IoSession>> iterator = entrySet.iterator();
			while (iterator.hasNext())
			{
				/**
				 * 2015-10-27 ����D/C�ر�ʶ��
				 */
				Entry<String, IoSession> entry=iterator.next();
				IoSession session = entry.getValue();
				if (!session.isConnected())
				{
					logger.info("DTU connection closed��"+entry.getKey());
					iterator.remove();
					
					
				}
			}
			// ���Client���ӳ�״̬
			entrySet = server.getClientPool().entrySet();
			iterator = entrySet.iterator();
			while (iterator.hasNext())
			{
				Entry<String, IoSession> entry=iterator.next();
				IoSession session = entry.getValue();
				if (!session.isConnected())
				{
					logger.info("Client connection closed��"+entry.getKey());
					iterator.remove();
				}
			}
			// ���Temp���ӳ�״̬
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
