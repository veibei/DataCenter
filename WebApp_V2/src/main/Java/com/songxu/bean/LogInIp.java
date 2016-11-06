package com.songxu.bean;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Entity(name="loginip")
public class LogInIp implements Comparable<LogInIp>
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ManyToOne(targetEntity=User.class,fetch=FetchType.LAZY)
	@JoinColumn(name="userId",referencedColumnName="id",nullable=false)
	private User user;
	private String ip;
	private Timestamp logTime;
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public User getUser()
	{
		return user;
	}
	public void setUser(User user)
	{
		this.user = user;
	}
	public String getIp()
	{
		return ip;
	}
	public void setIp(String ip)
	{
		this.ip = ip;
	}
	public Timestamp getLogTime()
	{
		return logTime;
	}
	public void setLogTime(Timestamp logTime)
	{
		this.logTime = logTime;
	}
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((logTime == null) ? 0 : logTime.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogInIp other = (LogInIp) obj;
		if (ip == null)
		{
			if (other.ip != null)
				return false;
		}
		else if (!ip.equals(other.ip))
			return false;
		if (logTime == null)
		{
			if (other.logTime != null)
				return false;
		}
		else if (!logTime.equals(other.logTime))
			return false;
		return true;
	}
	@Override
	/**
	 * 为了使用排序方法
	 */
	public int compareTo(LogInIp o)
	{

		return this.logTime.compareTo(o.logTime);
		
	}

}
