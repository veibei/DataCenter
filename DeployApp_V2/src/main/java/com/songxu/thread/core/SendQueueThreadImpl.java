package com.songxu.thread.core;

import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.songxu.core.Server;
import com.songxu.interfaces.Message;
import com.songxu.interfaces.SendQueueThread;
import com.songxu.util.core.DispatcherImpl4Mina;
/**
 * ���Ͷ��д����߳�
 * �����߳�
 * @author songxu
 *
 */
public class SendQueueThreadImpl implements SendQueueThread,ApplicationContextAware
{

	private static Logger logger = Logger.getLogger(SendQueueThreadImpl.class);
	private ApplicationContext applicationContext;
	private boolean runMark = true;
	private boolean ifStop = false;
	
	@Override
	public void stop()
	{
		runMark=false;

	}

	@Override
	public boolean getIfStop()
	{
		return this.ifStop;
	}

	@Override
	public void run()
	{
		runMark=true;
		ifStop=false;
		Server server=(Server)applicationContext.getBean("server",Server.class);
		logger.info("send queue thread started:"+Thread.currentThread());
		while(runMark)
		{
			BlockingQueue<Message> sendQueue=server.getSendPool();
			
			for(int i=0;i<sendQueue.size();i++)
			{
				try
				{
					Message message=sendQueue.remove();
					String imei=message.getIMEI_T();
					if(imei.equals(server.getIMEI()))
					{
						//�������͵Ĳ������� ��������
						logger.debug("test data:"+message.getMsg());
						
					}
					else if(message.getStatus().equals("00"))
					{
						/**
						 * ���ڲ����ӳ�
						 * ���е����Ӷ���temp���ӳ���
						 * �ӳٲ��Ե����Ӳ��ᱻ��temp���Ƴ� ���Դ�temp���õ�
						 */
						IoSession ioSession=server.getTempPool().get(message.getIMEI_F());
						DispatcherImpl4Mina fileterAndDispatcherImpl4Mina=new DispatcherImpl4Mina(message, ioSession,server);
						fileterAndDispatcherImpl4Mina.dispatchSend();
						server.getTempPool().remove(message.getIMEI_F());
					}
					else
					{
						//�ǲ�������
						IoSession ioSession;
						if(server.getClientPool().containsKey(message.getIMEI_T()))
						{
							ioSession=server.getClientPool().get(message.getIMEI_T());
						}
						else if(server.getDTUPool().containsKey(message.getIMEI_T()))
						{
							ioSession=server.getDTUPool().get(message.getIMEI_T());
						}
						else {
							continue;
						}
						DispatcherImpl4Mina fileterAndDispatcherImpl4Mina=new DispatcherImpl4Mina(message, ioSession,server);
						fileterAndDispatcherImpl4Mina.dispatchSend();
						
						
					}
				}
				catch (NoSuchElementException e)
				{
					break;
				}
				
				
				
			}
		}
		ifStop=true;
		logger.info("send queue thread stopped");
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException
	{
		this.applicationContext=arg0;
	}

}
