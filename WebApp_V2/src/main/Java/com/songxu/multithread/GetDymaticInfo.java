package com.songxu.multithread;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.songxu.bean.RemoteServerBean;
import com.songxu.memcached.MemecachedOperate;
@Deprecated
public class GetDymaticInfo implements CoreHandler
{

	private Logger logger=Logger.getLogger(GetDymaticInfo.class);
	private final Socket socket;
	private final ServletContext servletContext;
	private boolean isRun=false;
	private boolean isStop=true;
	public GetDymaticInfo(Socket socket,ServletContext servletContext )
	{
		this.socket=socket;
		this.servletContext=servletContext;
	}
	@Override
	public void run()
	{
		isRun=true;
		isStop=false;
		logger.info("remote status fetch thread started");
		while(isRun)
		{
			try
			{
				/**
				 * 2015-11-2 ����
				 * Ϊ�˶������������  ����һ������  ��ȡmemcached�ϵķ�����״̬
				 * ������״̬�뵱ǰserver���汣��ķ�����״̬��һ�� ����һ��push
				 */
				//int remoteStatusInMemcachde=(int) MemecachedOperate.get("server.status");
				int currentSavedStatus=((RemoteServerBean)servletContext.getAttribute("remoteServerBean")).getStatus();
				
				PushServerInfo pushServerInfo=new PushServerInfo(null,servletContext);
				/*if(remoteStatusInMemcachde!=currentSavedStatus)
				{
					System.out.println("״̬�ı�һ��");
					//������״̬�����˸ı� ����Ҫ����һ����Ϣ
					Thread thread=new Thread(pushServerInfo);
					thread.start();
					
					
				}*/
				
				
				DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());
				dataOutputStream.writeUTF("getDymatic");
				dataOutputStream.flush();
				InputStream inputStream=socket.getInputStream();
				if(inputStream.available()==0)
				{
					continue;
				}
				DataInputStream dataInputStream=new DataInputStream(inputStream);
				servletContext.setAttribute("receiveSpeed", dataInputStream.readDouble());
		    	servletContext.setAttribute("sendSpeed", dataInputStream.readDouble());
		    	
		    	servletContext.setAttribute("receiveRate", dataInputStream.readDouble());
		    	servletContext.setAttribute("sendRate", dataInputStream.readDouble());
		    	int clientCount= dataInputStream.readInt();
		    	int Dtucount=dataInputStream.readInt();
		    	servletContext.setAttribute("clientCount",clientCount);
		    	servletContext.setAttribute("DTUCount",Dtucount);
		    	servletContext.setAttribute("serverRunStatus", true);//��ʾ�Ѿ����ӵ�Զ������
			}
			catch (IOException e)
			{
				if (e.getMessage().equals("Software caused connection abort: socket write error"))
				{
					logger.info("remote server stopped  socket connection failed");
					servletContext.setAttribute("serverRunStatus", false);
					break;
				}
				else {
					e.printStackTrace();
				}
				
			}
		}
		isStop=true;
		try
		{
			socket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		logger.info("remote status fetch thread stopped");
		

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

}
