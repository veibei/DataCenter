package com.songxu.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.songxu.data.DataTranfer;
import com.songxu.data.DataTranferImpl;
import com.songxu.data.DataTranferImplV3;



public class SocketSever
{
	private static ServerSocket serverSocket;
	public static ServerSocket getServerSocket() throws IOException
	{
		if (serverSocket==null)
		{
			 serverSocket=new ServerSocket();
			 serverSocket.bind(new InetSocketAddress("172.20.202.33",50116));
		}
		return serverSocket;
	}
	/**
	 * 单例
	 */
	private SocketSever()
	{
		
	}
	public static void main(String[] args) throws IOException
	{
		Logger logger=Logger.getLogger(SocketSever.class);
		/*Set<Socket> sockets=new HashSet<Socket>();
		PropertyConfigurator.configure("src/log4j.properties");
		ServerSocket socket=SocketSever.getServerSocket();
		List<Socket> allSockets=new LinkedList<Socket>();
		
		
		Thread checkthread=new Thread(new CheckClientOnline(sockets));
		checkthread.start();
		*/
		ServerSocket socket=new ServerSocket(50118);
		
		
		
		
		while(true)
		{
			final Socket clientSocket=socket.accept();
			if(clientSocket!=null)
			{
				
				
				logger.info("有新的设备连接...");
				logger.info("新的设备ip"+clientSocket.getRemoteSocketAddress());
				DataTranfer dataTranfer=new DataTranferImplV3();
				dataTranfer.sendData(clientSocket, new Object());
				Thread thread=new Thread(new Runnable()
				{
					
					@Override
					public void run()
					{
						while(true)
						{
							try
							{
								(new DataTranferImplV3()).getData(clientSocket);
							}
							catch (IOException e)
							{
								e.printStackTrace();
							}
						}
						
					}
				});
				thread.start();
			}
		}
		
		
	}
}
