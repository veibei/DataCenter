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
 * 2015-11-3 新增 
 * 更改服务器状态获取方式为Memcached获取
 * 更改实现接口为Callable  为线程池的引入做准备
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
	 * 状态信号量 ，只有当push线程把新的remoteServerBean放入appliction以后 为true
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
		
			servletContext.setAttribute("remoteServerBean", remoteServerBean);// 把新的服务器状态放入Application对象中
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
			
		}
		logger.info("server status push thread stopped");
		return remoteServerBean;
		
	}

}
