package com.songxu.bean;

import java.io.Serializable;
import java.util.Date;

import com.songxu.core.Server;

/**
 * Զ��������Bean
 * @author songxu
 *
 */
public class RemoteServerBean implements Serializable
{
	public  final static int  Status_Running=Server.STATUS_RUNNING;
	public final static int Status_Stopped=Server.STATUS_STOP;

	private static final long serialVersionUID = -7233420248494739568L;
	/**
	 * ����������״̬  
	 */
	private int status;
	/**
	 * Զ��������
	 */
	private String hostIP;
	/**
	 * Զ�̶˿ں�
	 */
	private int port;
	/**
	 * ���������
	 */
	private int maxConnections;
	/**
	 * ���DTU������
	 */
	private int maxDtuConnections;
	/**
	 * ���ͻ��������� 
	 */
	private int maxClientConnections;
	/**
	 * ���ն��г���
	 */
	private int receiveQueueLength;
	/**
	 * ���Ͷ��г���
	 */
	private int sendQueueLength;
	/**
	 * ����������ʱ��
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
