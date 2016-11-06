package com.songxu.multithread;

import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

import javax.servlet.ServletContext;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;

import org.apache.log4j.Logger;

import com.songxu.bean.RemoteServerBean;
import com.songxu.webservice.ConnectionRemoteSystem;

/**
 * ���߳� ����������һ�μ��� ���߷���״̬�ı�ʱ ��Զ����������һ����Ϣ ���ѽ������application
 * 
 * @author songxu
 *
 */
@Deprecated
public class PushServerInfo implements Runnable
{
	private static Logger logger = Logger.getLogger(PushServerInfo.class);
	private ServletContext servletContext;
	private Socket socket = null;
	/**
	 * ״̬�ź��� ��ֻ�е�push�̰߳��µ�remoteServerBean����appliction�Ժ� Ϊtrue
	 */
	private boolean isStoped = false;

	public PushServerInfo(ServletContext servletContext)
	{
		this.servletContext = servletContext;
	}

	public PushServerInfo(Socket socket, ServletContext servletContext)
	{
		this.socket = socket;
		this.servletContext = servletContext;
	}

	@Override
	public void run()
	{
		logger.info("server status push thread started");
		isStoped = false;
		try
		{
			if (socket == null)
			{
				String ip = servletContext.getInitParameter("ip");
				int port = Integer.parseInt(servletContext
						.getInitParameter("port"));
				socket = new Socket(ip, port);
			}

			ConnectionRemoteSystem connectionRemoteSystem = new ConnectionRemoteSystem(
					socket);
			RemoteServerBean remoteServerBean = connectionRemoteSystem
					.getInfo();// ���ͻ����Ϣ����
			connectionRemoteSystem.closeConnection();
			socket.close();// ʹ����Ϻ������ر�

			servletContext.setAttribute("remoteServerBean", remoteServerBean);// ���µķ�����״̬����Application������
			isStoped = true;
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
			try
			{
				socket.close();
			}
			catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		logger.info("server status push thread stopped");
		isStoped = true;
	}

	public boolean isStoped()
	{
		return isStoped;
	}

}
