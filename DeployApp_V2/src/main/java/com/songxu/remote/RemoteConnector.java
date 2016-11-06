package com.songxu.remote;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.songxu.core.Server;
/**
 * 接收远程控制器连接
 * 所有的服务都从这里开始  从这里终止
 * @author songxu
 *
 */
public class RemoteConnector
{
	private static Logger logger=Logger.getLogger(RemoteConnector.class);
	private Server server;
	private boolean isRun=false;
	private ServerSocket serverSocket;
	public RemoteConnector(int port)
	{
		try
		{
			serverSocket=new ServerSocket(port);
			logger.info("remote control established");
			
		}
		catch (IOException e)
		{
			serverSocket=null;
			e.printStackTrace();
		
		}
	}
	public void acceptConnection()
	{
		ExecutorService executor=Executors.newFixedThreadPool(5);
		isRun=true;
		while(isRun)
		{
			try
			{
				//阻塞等待
				Socket socket=serverSocket.accept();
				logger.info("remote control connetion established:"+socket.getRemoteSocketAddress().toString().replace("/", ""));
				ControlThreadV2 controlThreadV2=new ControlThreadV2(socket, server);
				executor.submit(controlThreadV2);
				
	
			}
			catch (IOException e)
			{
				
			}
			
		}
	}
	public void stop()
	{
		try
		{
			isRun=false;
			this.serverSocket.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	public void setServer(Server server)
	{
		this.server = server;
	}
	

}
