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
 * 短线程 当服务器第一次加载 或者发生状态改变时 向远程主机请求一次信息 并把结果放入application
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
	 * 状态信号量 ，只有当push线程把新的remoteServerBean放入appliction以后 为true
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
					.getInfo();// 发送获得消息请求
			connectionRemoteSystem.closeConnection();
			socket.close();// 使用完毕后立即关闭

			servletContext.setAttribute("remoteServerBean", remoteServerBean);// 把新的服务器状态放入Application对象中
			isStoped = true;
			@SuppressWarnings("unchecked")
			BlockingQueue<Session> sessions = (BlockingQueue<Session>) servletContext
					.getAttribute("webSocketSessions");

			// 只有当存在websocket会话时，才需要推送消息
			if (sessions.size() > 0)
			{
				String timeString = "";
				if (remoteServerBean.getStatus() == 1)
				{
					// 转换日期格式 推送到前端
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
						logger.error("推送消息出错");
					}
				}

			}

		}
		catch (Exception e)
		{
			logger.error("服务器连接失败");
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
