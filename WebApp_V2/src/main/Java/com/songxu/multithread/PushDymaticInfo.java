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
 * 向服务器推送消息
 * 
 * @author songxu
 *
 */
@Deprecated
public class PushDymaticInfo implements CoreHandler
{
	private Logger logger = Logger.getLogger(PushDymaticInfo.class);
	/**
	 * servlet上下文
	 */
	private final ServletContext servletContext;
	private boolean isRun = false;
	private boolean isStop = true;
	/**
	 * 信号量
	 * 如果它为true 证明当前服务器状态发生了改变 需要推送一次状态信息
	 */
	private boolean changeStatus=false;
	public PushDymaticInfo(ServletContext servletContext)
	{
		this.servletContext = servletContext;
	}

	/**
	 * 将远程服务器动态信息封装成string 方便推送
	 * @author songxu
	 * @version 1.0
	 * @return
	 */
	@Deprecated
	private String getInfo()
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("i");// 插入标识符 i=info
		stringBuilder.append("|");// 插入分隔符
		stringBuilder.append(servletContext.getAttribute("receiveSpeed"));
		stringBuilder.append("|");// 插入分隔符
		stringBuilder.append(servletContext.getAttribute("sendSpeed"));
		stringBuilder.append("|");// 插入分隔符
		stringBuilder.append(servletContext.getAttribute("receiveRate"));
		stringBuilder.append("|");// 插入分隔符
		stringBuilder.append(servletContext.getAttribute("sendRate"));
		stringBuilder.append("|");// 插入分隔符
		stringBuilder.append(servletContext.getAttribute("clientCount"));
		stringBuilder.append("|");// 插入分隔符
		stringBuilder.append(servletContext.getAttribute("DTUCount"));
		return stringBuilder.toString();
	}
	/**
	 * 将Memcached服务器动态信息封装成string 方便推送
	 * 2015-11-3启动 
	 * @author songxu
	 * @version 2.0
	 * @return
	 */
	private String getInfoV2()
	{
		/*StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("i");// 插入标识符 i=info
		stringBuilder.append("|");// 插入分隔符
		stringBuilder.append(MemecachedOperate.get("server.receiveSpeed"));
		stringBuilder.append("|");// 插入分隔符
		stringBuilder.append(MemecachedOperate.get("server.sendSpeed"));
		stringBuilder.append("|");// 插入分隔符
		stringBuilder.append(MemecachedOperate.get("server.receiveRate"));
		stringBuilder.append("|");// 插入分隔符
		stringBuilder.append(MemecachedOperate.get("server.sendRate"));
		stringBuilder.append("|");// 插入分隔符
		stringBuilder.append(MemecachedOperate.get("server.clientCount"));
		stringBuilder.append("|");// 插入分隔符
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
				 * 2015-11-3 替换为Version2.0版本
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
						logger.error("推送消息出错");
					}
				}
			}
			/*GetDymaticInfo getDymaticInfo = (GetDymaticInfo) servletContext
					.getAttribute("threadDymatic");
			// 如果发现该线程已经停止 重启
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
	 * 重启reStartGetDymatic线程
	 */
	private void reStartGetDymatic()
	{
		Socket socketDymatic;
		// 启动动态监测线程
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
				// 依然要放一个空值进入
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
