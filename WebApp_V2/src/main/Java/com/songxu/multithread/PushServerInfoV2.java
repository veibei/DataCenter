package com.songxu.multithread;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import javax.servlet.ServletContext;
import javax.websocket.Session;
import javax.websocket.RemoteEndpoint.Basic;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import com.songxu.bean.RemoteServerBean;
import com.songxu.memcached.MemecachedOperate;
/**
 * 2015-11-3 ���� 
 * ���ķ�����״̬��ȡ��ʽΪMemcached��ȡ
 * ����ʵ�ֽӿ�ΪCallable  Ϊ�̳߳ص�������׼��
 * @version 2.0
 * @author songxu
 *
 */
public class PushServerInfoV2 implements Callable<RemoteServerBean>
{
	private static Logger logger = Logger.getLogger(PushServerInfoV2.class);
	private ServletContext servletContext;
	private MemecachedOperate MemecachedOperate;
	
	/**
	 * ״̬�ź��� ��ֻ�е�push�̰߳��µ�remoteServerBean����appliction�Ժ� Ϊtrue
	 */

	public PushServerInfoV2(ServletContext servletContext)
	{
		this.servletContext = servletContext;
		ApplicationContext applicationContext=(ApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		MemecachedOperate =(com.songxu.memcached.MemecachedOperate) applicationContext.getBean("memcachedOperate");
		

	}

	@Override
	public RemoteServerBean call() throws Exception
	{
		logger.info("server status push thread started");
		RemoteServerBean remoteServerBean=null;
		try
		{
			
			remoteServerBean = (RemoteServerBean) MemecachedOperate.get("server.serverBean");
		
			servletContext.setAttribute("remoteServerBean", remoteServerBean);// ���µķ�����״̬����Application������
			@SuppressWarnings("unchecked")
			BlockingQueue<Session> sessions = (BlockingQueue<Session>) servletContext
					.getAttribute("webSocketSessions");

			// ֻ�е�����websocket�Ựʱ������Ҫ������Ϣ
			if (sessions.size() > 0)
			{
				String timeString = "";
				if (remoteServerBean.getStatus() == 1)
				{
					// ת�����ڸ�ʽ ���͵�ǰ��
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
							"yyyy/MM/dd HH:mm:ss");
					timeString = simpleDateFormat.format(remoteServerBean
							.getStartTimeDate());
				}
				String msg = "s" + remoteServerBean.getStatus() + timeString;
				Iterator<Session> iterator = sessions.iterator();
				while (iterator.hasNext())
				{
					Session session = iterator.next();
					try
					{
						if (session.isOpen())
						{
							Basic basic = session.getBasicRemote();
							synchronized (basic)
							{
								basic.sendText(msg);
							}

						}
					}
					catch (IOException e)
					{
						logger.error("������Ϣ����");
					}
				}

			}

		}
		catch (Exception e)
		{
			logger.error("����������ʧ��");
			e.printStackTrace();
			
		}
		logger.info("server status push thread stopped");
		return remoteServerBean;
		
	}

}
