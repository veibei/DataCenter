package com.songxu.multithread;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

import javax.servlet.ServletContext;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;

import org.apache.log4j.Logger;

import com.songxu.memcached.MemecachedOperate;

/**
 * �������������Ϣ
 * 
 * @author songxu
 *
 */
@Deprecated
public class PushDymaticInfo implements CoreHandler
{
	private Logger logger = Logger.getLogger(PushDymaticInfo.class);
	/**
	 * servlet������
	 */
	private final ServletContext servletContext;
	private boolean isRun = false;
	private boolean isStop = true;
	/**
	 * �ź���
	 * �����Ϊtrue ֤����ǰ������״̬�����˸ı� ��Ҫ����һ��״̬��Ϣ
	 */
	private boolean changeStatus=false;
	public PushDymaticInfo(ServletContext servletContext)
	{
		this.servletContext = servletContext;
	}

	/**
	 * ��Զ�̷�������̬��Ϣ��װ��string ��������
	 * @author songxu
	 * @version 1.0
	 * @return
	 */
	@Deprecated
	private String getInfo()
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("i");// �����ʶ�� i=info
		stringBuilder.append("|");// ����ָ���
		stringBuilder.append(servletContext.getAttribute("receiveSpeed"));
		stringBuilder.append("|");// ����ָ���
		stringBuilder.append(servletContext.getAttribute("sendSpeed"));
		stringBuilder.append("|");// ����ָ���
		stringBuilder.append(servletContext.getAttribute("receiveRate"));
		stringBuilder.append("|");// ����ָ���
		stringBuilder.append(servletContext.getAttribute("sendRate"));
		stringBuilder.append("|");// ����ָ���
		stringBuilder.append(servletContext.getAttribute("clientCount"));
		stringBuilder.append("|");// ����ָ���
		stringBuilder.append(servletContext.getAttribute("DTUCount"));
		return stringBuilder.toString();
	}
	/**
	 * ��Memcached��������̬��Ϣ��װ��string ��������
	 * 2015-11-3���� 
	 * @author songxu
	 * @version 2.0
	 * @return
	 */
	private String getInfoV2()
	{
		/*StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("i");// �����ʶ�� i=info
		stringBuilder.append("|");// ����ָ���
		stringBuilder.append(MemecachedOperate.get("server.receiveSpeed"));
		stringBuilder.append("|");// ����ָ���
		stringBuilder.append(MemecachedOperate.get("server.sendSpeed"));
		stringBuilder.append("|");// ����ָ���
		stringBuilder.append(MemecachedOperate.get("server.receiveRate"));
		stringBuilder.append("|");// ����ָ���
		stringBuilder.append(MemecachedOperate.get("server.sendRate"));
		stringBuilder.append("|");// ����ָ���
		stringBuilder.append(MemecachedOperate.get("server.clientCount"));
		stringBuilder.append("|");// ����ָ���
		stringBuilder.append(MemecachedOperate.get("server.DTUCount"));
		return stringBuilder.toString();*/
		return"";
	}
	@Override
	public void run()
	{
		isRun = true;
		isStop = false;
		logger.info("log push thread started");
		while (isRun)
		{
			@SuppressWarnings("unchecked")
			BlockingQueue<Session> sessions = (BlockingQueue<Session>) servletContext
					.getAttribute("webSocketSessions");
			if (sessions.size() > 0)
			{
				/**
				 * 2015-11-3 �滻ΪVersion2.0�汾
				 */
				String msg = getInfoV2();
				Iterator<Session> iterator = sessions.iterator();
				while (iterator.hasNext())
				{
					Session session = iterator.next();
					try
					{
						if (session.isOpen())
						{
							Basic basic=session.getBasicRemote();
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
			/*GetDymaticInfo getDymaticInfo = (GetDymaticInfo) servletContext
					.getAttribute("threadDymatic");
			// ������ָ��߳��Ѿ�ֹͣ ����
			if (getDymaticInfo.getIfStop())
			{
				reStartGetDymatic();
			}
			try
			{
				Thread.sleep(500);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/

		}
		isStop = true;
		logger.info("log push thread stopped");
	}

	@Override
	public void stop()
	{
		isRun = false;

	}

	@Override
	public boolean getIfStop()
	{
		return isStop;
	}

	/**
	 * ����reStartGetDymatic�߳�
	 */
	private void reStartGetDymatic()
	{
		Socket socketDymatic;
		// ������̬����߳�
		try
		{
			String ip = servletContext.getInitParameter("ip");
			int port = Integer
					.parseInt(servletContext.getInitParameter("port"));
			socketDymatic = new Socket(ip, port);
			GetDymaticInfo getDymaticInfo = new GetDymaticInfo(socketDymatic,
					servletContext);
			Thread threadDymatic = new Thread(getDymaticInfo);
			threadDymatic.start();
			servletContext.setAttribute("threadDymatic", getDymaticInfo);
			logger.info("restart request sent");
		}
		catch (IOException e)
		{
			if (e.getMessage().equals("Connection refused: connect"))
			{
				logger.info("remote server closed");
				// ��ȻҪ��һ����ֵ����
				GetDymaticInfo getDymaticInfo = new GetDymaticInfo(null,
						servletContext);
				servletContext.setAttribute("threadDymatic", getDymaticInfo);
			}
			else {
				logger.error(e.getStackTrace());
			}
		}

	}

}
