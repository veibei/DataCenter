package com.songxu.remote;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.songxu.bean.RemoteServerBean;
import com.songxu.core.Server;

/**
 * 核心控制流程 用于与远程主机通讯 发送信息
 * 
 * @author songxu
 *
 */
public class ControlThread implements Runnable
{
	private static Logger logger = Logger.getLogger(ControlThread.class);
	private final Socket socket;
	private final Server server;
	private volatile boolean isrun=false;
	public ControlThread(Socket socket, Server server)
	{
		this.socket = socket;
		this.server = server;
	}
	public void stop()
	{
		isrun=false;
	}
	@Override
	public void run()
	{
		isrun=true;
		while (isrun)
		{
			try
			{
				InputStream inputStream = socket.getInputStream();
				if (inputStream.available() != 0)
				{
					// 阻塞 等待inputstream
					DataInputStream dataInputStream=new DataInputStream(inputStream);
					
					String getInfo = dataInputStream.readUTF();
					if (getInfo.equals("getInfo"))
					{
						// 获得服务器消息
						sendServerInfo(socket.getOutputStream());
					}
					else if (getInfo.equals("start"))
					{
						/**
						 * 2015-10-27 新增记录远程控制ip和用户名功能
						 */
						String requestInfo=dataInputStream.readUTF();
						String []userInfo=requestInfo.split("\\|");
						// 启动服务
						logger.info(userInfo[1]+"@"+userInfo[0]
								+ "请求服务启动。。。");
						try
						{
							server.startSever();
							int count = 0;
							//检验服务启动是否超时 50*100 毫秒
							while (true)
							{
								count++;
								if (server.getStatus() == 1)
								{
									sendResult(true, socket.getOutputStream());
									break;
								}
								if (count > 100)
								{
									logger.info("server started timeout");
									server.stopSever();
									sendResult(false, socket.getOutputStream());
									break;

								}
								Thread.sleep(50);
							}
						}
						catch (Exception e)
						{
							//如果在停止服务过程中出现错误。。。则报告错误
							logger.error("server started error");
							sendResult(false, socket.getOutputStream());

						}

					}
					else if (getInfo.equals("stop"))
					{
						String requestInfo=dataInputStream.readUTF();
						String []userInfo=requestInfo.split("\\|");
						// 停止服务
						logger.info(userInfo[1]+"@"+userInfo[0]
								+  "请求服务关闭");
						try
						{
							server.stopSever();
							int count = 0;
							while (true)
							{
								count++;
								if (server.getStatus() == 0)
								{
									sendResult(true, socket.getOutputStream());
									break;
								}
								if (count > 100)
								{
									logger.info("server close request timeout");
									sendResult(false, socket.getOutputStream());
									break;

								}
								Thread.sleep(50);
							}
						}
						catch (Exception e)
						{
							logger.info("server close request error");
							sendResult(false, socket.getOutputStream());
						}

					}
					/**
					 * 获取动态数据
					 */
					else if(getInfo.equals("getDymatic"))
					{
						sendDymatic(socket.getOutputStream());
					}
					else if(getInfo.equals("close")){
						//远程主机关闭连接 
						sendResult(true, socket.getOutputStream());
						break;
					}
					/**
					 * 获取DTU/Client在线连接情况
					 */
					else if (getInfo.equals("DC")) {
						sendDCOL(socket.getOutputStream());
					}

				}
			}
			catch (IOException e)
			{
				// 远程控制器关闭  就关闭当前线程 释放资源 
				// 所有的请求式的连接都是短连接 请求完毕即中断连接 释放掉资源
				//只有一个线程是长连接  获取状态的线程
				if(e.getMessage().equals("Software caused connection abort: socket write error"))
				{
					logger.info("remote control socket closed:"+socket.getRemoteSocketAddress().toString().replace("/", ""));
					stop();
				}
				else {
					e.printStackTrace();
				}
				
			}
		}
		try
		{
			socket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		logger.info("remote control thread stopped"+Thread.currentThread());

	}

	/**
	 * 发送服务基础信息
	 * 
	 * @param outputStream
	 * @throws IOException
	 */
	private void sendServerInfo(OutputStream outputStream) throws IOException
	{
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		dataOutputStream.writeInt(server.getStatus());
		InetSocketAddress socketAddress = (InetSocketAddress) server
				.getAcceptor().getDefaultLocalAddress();
		dataOutputStream.writeUTF(socketAddress.getHostName());
		dataOutputStream.writeInt(socketAddress.getPort());
		dataOutputStream.writeInt(server.getMaxConnections());
		dataOutputStream.writeInt(500);
		dataOutputStream.writeInt(500);
		dataOutputStream.writeInt(server.getReceivePool().remainingCapacity());
		dataOutputStream.writeInt(server.getSendPool().remainingCapacity());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyyMMdd HH:mm:ss:SSS");
		if (server.getStatus() == RemoteServerBean.Status_Running)
		{
			dataOutputStream.writeUTF(dateFormat.format(server.getStartTime()));
		}

		dataOutputStream.flush();
	}

	/**
	 * 发送结果
	 * @param result
	 * @param outputStream
	 * @throws IOException
	 */
	private void sendResult(boolean result, OutputStream outputStream)
			throws IOException
	{
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		dataOutputStream.writeBoolean(result);
	}
	/**
	 * 发送动态数据
	 * @param outputStream
	 * @throws IOException
	 */
	private  void sendDymatic(OutputStream outputStream) throws IOException
	{
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		
		dataOutputStream.writeDouble(server.getReceiveSpeed());//接收速率
		dataOutputStream.writeDouble(server.getSendSpeed());//发送速率
		
		dataOutputStream.writeDouble(server.getReceive());//接收流量 
		dataOutputStream.writeDouble(server.getSend());//发送流量
		dataOutputStream.writeInt(server.getClientPool().size());//当前客户端连接数量
		dataOutputStream.writeInt(server.getDTUPool().size());//当前DTU连接数量
	}
	/**
	 * 发送DTU/Client 在线连接情况
	 * @param outputStream
	 * @throws IOException
	 */
	private void sendDCOL(OutputStream outputStream) throws IOException
	{
		StringBuilder stringBuffer=new StringBuilder();
		
		ConcurrentHashMap<String, IoSession> clientPool=server.getClientPool();
		ConcurrentHashMap<String, IoSession> dTUPool=server.getDTUPool();
		
		Set<Entry<String, IoSession>> clientEntry=clientPool.entrySet();
		for (Entry<String, IoSession> entry : clientEntry)
		{
			stringBuffer.append(entry.getKey());stringBuffer.append(",");
			stringBuffer.append(entry.getValue().getRemoteAddress().toString().replace("/", ""));stringBuffer.append(",");
			double up=server.getRateUpCount().get(entry.getKey());
			double down=server.getRateDownCount().get(entry.getKey());
			stringBuffer.append(up);stringBuffer.append(",");//上行流量
			stringBuffer.append(down);//下行流量
			stringBuffer.append("|");
		}
		Set<Entry<String, IoSession>> dtuEntry=dTUPool.entrySet();
		for (Entry<String, IoSession> entry : dtuEntry)
		{
			stringBuffer.append(entry.getKey());stringBuffer.append(",");
			stringBuffer.append(entry.getValue().getRemoteAddress().toString().replace("/", ""));stringBuffer.append(",");
			double up=server.getRateUpCount().get(entry.getKey());
			double down=server.getRateDownCount().get(entry.getKey());
			stringBuffer.append(up);stringBuffer.append(",");//上行流量
			stringBuffer.append(down);//下行流量
			stringBuffer.append("|");
		}
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		dataOutputStream.writeUTF(stringBuffer.toString());
		
		
	}

}
