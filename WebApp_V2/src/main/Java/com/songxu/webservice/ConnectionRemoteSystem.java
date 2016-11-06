package com.songxu.webservice;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.songxu.bean.RemoteServerBean;

public class ConnectionRemoteSystem
{
	private final Socket socket;

	public ConnectionRemoteSystem(Socket socket)
	{
		this.socket = socket;
	}

	/**
	 * 根据inputStream 转换成 RemoteServerBean
	 * 
	 * @param inputStream
	 * @return
	 */
	public RemoteServerBean getRemoteServerBean(InputStream inputStream)
	{
		RemoteServerBean bean = new RemoteServerBean();
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		try
		{
			bean.setStatus(dataInputStream.readInt());
			bean.setHostIP(dataInputStream.readUTF());
			bean.setPort(dataInputStream.readInt());
			bean.setMaxConnections(dataInputStream.readInt());
			bean.setMaxDtuConnections(dataInputStream.readInt());
			bean.setMaxClientConnections(dataInputStream.readInt());
			bean.setSendQueueLength(dataInputStream.readInt());
			bean.setReceiveQueueLength(dataInputStream.readInt());
			if (bean.getStatus() == RemoteServerBean.Status_Running)
			{
				String timeString = dataInputStream.readUTF();
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyyMMdd hh:mm:ss:SSS");

				bean.setStartTimeDate(dateFormat.parse(timeString));
			}
			else
			{

				bean.setStartTimeDate(null);
			}
			return bean;

		}
		catch (IOException | ParseException e)
		{
			return null;
		}

	}

	public RemoteServerBean getInfo() throws IOException
	{
		DataOutputStream dataOutputStream = new DataOutputStream(
				socket.getOutputStream());
		dataOutputStream.writeUTF("getInfo");
		return getRemoteServerBean(socket.getInputStream());

	}

	/**
	 * 启动系统
	 * 
	 * @throws IOException
	 */
	public boolean StartServer(String ip,String username) throws IOException
	{
		DataOutputStream dataOutputStream = new DataOutputStream(
				socket.getOutputStream());
		dataOutputStream.writeUTF("start");
		dataOutputStream.writeUTF(ip+"|"+username);
		InputStream inputStream = socket.getInputStream();
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		return dataInputStream.readBoolean();
	}

	/**
	 * 停止系统
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean StopServer(String ip,String username) throws IOException
	{
		DataOutputStream dataOutputStream = new DataOutputStream(
				socket.getOutputStream());
		dataOutputStream.writeUTF("stop");
		dataOutputStream.writeUTF(ip+"|"+username);
		InputStream inputStream = socket.getInputStream();
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		return dataInputStream.readBoolean();
	}

	/**
	 * 请求关闭连接
	 * 
	 * @return
	 * @throws IOException
	 */
	public boolean closeConnection() throws IOException
	{
		DataOutputStream dataOutputStream = new DataOutputStream(
				socket.getOutputStream());
		dataOutputStream.writeUTF("close");
		InputStream inputStream = socket.getInputStream();
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		return dataInputStream.readBoolean();

	}

	/**
	 * 获取远程DC连接情况
	 * 
	 * @return
	 */
	public String getRemoteDCData() throws IOException
	{
		DataOutputStream dataOutputStream = new DataOutputStream(
				socket.getOutputStream());
		dataOutputStream.writeUTF("DC");
		InputStream inputStream = socket.getInputStream();
		DataInputStream dataInputStream = new DataInputStream(inputStream);
		return dataInputStream.readUTF();
	}

}
