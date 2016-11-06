package com.songxu.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="logging")
public class LogBean implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7899137165660343817L;
	public LogBean()
	{
		
	}
	@Id
	private int id;
	private Date log_date;
	private String log_level;
	private String location;
	private String message;
	private String dateString="123";
	public Date getLog_date()
	{
		return log_date;
	}
	public void setLog_date(Date log_date)
	{
		setDateString(log_date);
		this.log_date = log_date;
	}
	public String getLog_level()
	{
		return log_level;
	}
	public void setLog_level(String log_level)
	{
		this.log_level = log_level;
	}
	public String getLocation()
	{
		return location;
	}
	public void setLocation(String location)
	{
		this.location = location;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message = message;
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getDateString()
	{
		return dateString;
	}
	/**
	 * 时间格式转换方法
	 * @param date
	 */
	public void setDateString(Date date)
	{
		
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		this.dateString=simpleDateFormat.format(date);
	}

}
