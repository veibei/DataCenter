package com.songxu.multithread;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.servlet.ServletContext;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.songxu.bean.LogBean;
import com.songxu.dao.LogDao;
@Deprecated
public class PushLogInfo implements CoreHandler
{

	private Logger logger = Logger.getLogger(PushLogInfo.class);
	/**
	 * servlet������
	 */
	private boolean isRun = false;
	private boolean isStop = true;
	private final LogDao logDao;
	private final ServletContext servletContext;
	
	/*
	 * ������һ�ε���־��¼�������û�з����仯���������������
	 */
	private int lastCount=0;
	public PushLogInfo(ServletContext servletContext)
	{
		logDao=WebApplicationContextUtils.getWebApplicationContext(servletContext).getBean("logDao", LogDao.class
				);
		this.servletContext=servletContext;
	}
	@Override
	public void run()
	{
		isRun=true;
		while(isRun)
		{
			@SuppressWarnings("unchecked")
			BlockingQueue<Session> sessions = (BlockingQueue<Session>) servletContext
					.getAttribute("webSocketSessions");
			//ֻ�е�����websocket�Ựʱ������Ҫ������Ϣ
			if(sessions.size()>0)
			{
				List<LogBean>logBeans=logDao.getLog();
				if(logBeans==null)
				{
					logger.error("logDao��ѯ����");
					continue;
				}
				/*if(logBeans.size()==lastCount)//���û���µļ�¼���� ����������Ϣ
				{
					continue;
				}*/
				else
				{
					lastCount=logBeans.size();
					Iterator<Session>iterator=sessions.iterator();
					String msg=getMsg(logBeans);
					while(iterator.hasNext())
					{
						Session session=iterator.next();
						try
						{
							Basic basic=session.getBasicRemote();
							synchronized (basic)
							{
								basic.sendText(msg);
							}
							
						}
						catch (IOException e)
						{
							logger.error("������Ϣ����");
						}
					}
					
				}
			}
			try
			{
				Thread.sleep(2000);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void stop()
	{
		isRun=false;
	}

	@Override
	public boolean getIfStop()
	{
		return isStop;
	}
	private String getMsg(List<LogBean> logBeans)
	{
		StringBuilder result=new StringBuilder();
		result.append("l");//�����ʶ��
		//�����¼С��10�� ��ȫ������
		if(logBeans.size()<10)
		{
			for (LogBean logBean : logBeans)
			{
				result.append(logBean.getLog_date());
				result.append("|");
				result.append(logBean.getLog_level());
				result.append("|");
				result.append(logBean.getMessage());
				result.append(";");
			}
		}
		else {
			for(int i=logBeans.size()-10;i<logBeans.size();i++)
			{
				LogBean logBean=logBeans.get(i);
				result.append(logBean.getLog_date());
				result.append("|");
				result.append(logBean.getLog_level());
				result.append("|");
				result.append(logBean.getMessage());
				result.append(";");
			}
		}
		return result.toString();
	} 

}
