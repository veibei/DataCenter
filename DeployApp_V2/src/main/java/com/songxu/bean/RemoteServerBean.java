package com.songxu.bean;

import java.io.Serializable;
import java.util.Date;

import com.songxu.core.Server;

/**
 * 远程主机的Bean
 * @author songxu
 *
 */
public class RemoteServerBean implements Serializable
{
	public  final static int  Status_Running=Server.STATUS_RUNNING;
	public final static int Status_Stopped=Server.STATUS_STOP;

	private static final long serialVersionUID = -7233420248494739568L;
	/**
	 * 服务器运行状态  
	 */
	private int status;
	/**
	 * 远程主机名
	 */
	private String hostIP;
	/**
	 * 远程端口号
	 */
	private int port;
	/**
	 * 最大连接数
	 */
	private int maxConnections;
	/**
	 * 最大DTU连接数
	 */
	private int maxDtuConnections;
	/**
	 * 最大客户端连接数 
	 */
	private int maxClientConnections;
	/**
	 * 接收队列长度
	 */
	private int receiveQueueLength;
	/**
	 * 发送队列长度
	 */
	private int sendQueueLength;
	/**
	 * 服务器启动时间
	 */
	private Date startTimeDate;
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public String getHostIP()
	{
		return hostIP;
	}
	public void setHostIP(String hostIP)
	{
		this.hostIP = hostIP;
	}
	public int getPort()
	{
		return port;
	}
	public void setPort(int port)
	{
		this.port = port;
	}
	public int getMaxConnections()
	{
		return maxConnections;
	}
	public void setMaxConnections(int maxConnections)
	{
		this.maxConnections = maxConnections;
	}
	public int getMaxDtuConnections()
	{
		return maxDtuConnections;
	}
	public void setMaxDtuConnections(int maxDtuConnections)
	{
		this.maxDtuConnections = maxDtuConnections;
	}
	public int getMaxClientConnections()
	{
		return maxClientConnections;
	}
	public void setMaxClientConnections(int maxClientConnections)
	{
		this.maxClientConnections = maxClientConnections;
	}
	public int getReceiveQueueLength()
	{
		return receiveQueueLength;
	}
	public void setReceiveQueueLength(int receiveQueueLength)
	{
		this.receiveQueueLength = receiveQueueLength;
	}
	public int getSendQueueLength()
	{
		return sendQueueLength;
	}
	public void setSendQueueLength(int sendQueueLength)
	{
		this.sendQueueLength = sendQueueLength;
	}
	public Date getStartTimeDate()
	{
		return startTimeDate;
	}
	public void setStartTimeDate(Date startTimeDate)
	{
		this.startTimeDate = startTimeDate;
	}

}
