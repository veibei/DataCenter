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
 * ���Ŀ������� ������Զ������ͨѶ ������Ϣ
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
					// ���� �ȴ�inputstream
					DataInputStream dataInputStream=new DataInputStream(inputStream);
					
					String getInfo = dataInputStream.readUTF();
					if (getInfo.equals("getInfo"))
					{
						// ��÷�������Ϣ
						sendServerInfo(socket.getOutputStream());
					}
					else if (getInfo.equals("start"))
					{
						/**
						 * 2015-10-27 ������¼Զ�̿���ip���û�������
						 */
						String requestInfo=dataInputStream.readUTF();
						String []userInfo=requestInfo.split("\\|");
						// ��������
						logger.info(userInfo[1]+"@"+userInfo[0]
								+ "�����������������");
						try
						{
							server.startSever();
							int count = 0;
							//������������Ƿ�ʱ 50*100 ����
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
							//�����ֹͣ��������г��ִ��󡣡����򱨸����
							logger.error("server started error");
							sendResult(false, socket.getOutputStream());

						}

					}
					else if (getInfo.equals("stop"))
					{
						String requestInfo=dataInputStream.readUTF();
						String []userInfo=requestInfo.split("\\|");
						// ֹͣ����
						logger.info(userInfo[1]+"@"+userInfo[0]
								+  "�������ر�");
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
					 * ��ȡ��̬����
					 */
					else if(getInfo.equals("getDymatic"))
					{
						sendDymatic(socket.getOutputStream());
					}
					else if(getInfo.equals("close")){
						//Զ�������ر����� 
						sendResult(true, socket.getOutputStream());
						break;
					}
					/**
					 * ��ȡDTU/Client�����������
					 */
					else if (getInfo.equals("DC")) {
						sendDCOL(socket.getOutputStream());
					}

				}
			}
			catch (IOException e)
			{
				// Զ�̿������ر�  �͹رյ�ǰ�߳� �ͷ���Դ 
				// ���е�����ʽ�����Ӷ��Ƕ����� ������ϼ��ж����� �ͷŵ���Դ
				//ֻ��һ���߳��ǳ�����  ��ȡ״̬���߳�
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
	 * ���ͷ��������Ϣ
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
	 * ���ͽ��
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
	 * ���Ͷ�̬����
	 * @param outputStream
	 * @throws IOException
	 */
	private  void sendDymatic(OutputStream outputStream) throws IOException
	{
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		
		dataOutputStream.writeDouble(server.getReceiveSpeed());//��������
		dataOutputStream.writeDouble(server.getSendSpeed());//��������
		
		dataOutputStream.writeDouble(server.getReceive());//�������� 
		dataOutputStream.writeDouble(server.getSend());//��������
		dataOutputStream.writeInt(server.getClientPool().size());//��ǰ�ͻ�����������
		dataOutputStream.writeInt(server.getDTUPool().size());//��ǰDTU��������
	}
	/**
	 * ����DTU/Client �����������
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
			stringBuffer.append(up);stringBuffer.append(",");//��������
			stringBuffer.append(down);//��������
			stringBuffer.append("|");
		}
		Set<Entry<String, IoSession>> dtuEntry=dTUPool.entrySet();
		for (Entry<String, IoSession> entry : dtuEntry)
		{
			stringBuffer.append(entry.getKey());stringBuffer.append(",");
			stringBuffer.append(entry.getValue().getRemoteAddress().toString().replace("/", ""));stringBuffer.append(",");
			double up=server.getRateUpCount().get(entry.getKey());
			double down=server.getRateDownCount().get(entry.getKey());
			stringBuffer.append(up);stringBuffer.append(",");//��������
			stringBuffer.append(down);//��������
			stringBuffer.append("|");
		}
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		dataOutputStream.writeUTF(stringBuffer.toString());
		
		
	}

}
