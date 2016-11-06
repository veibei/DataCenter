package com.songxu.bean;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.songxu.util.ConstantValue;
@Entity
@Table(name="user_info")
public class User
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String username;
	private String password;
	@OneToMany(mappedBy="user",targetEntity=LogInIp.class,cascade=CascadeType.ALL)
	private Set<LogInIp>loginip=new HashSet<LogInIp>();
	private Timestamp logtimeTimestamp;
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public Set<LogInIp> getLoginip()
	{
		return loginip;
	}
	public void setLoginip(Set<LogInIp> loginip)
	{
		this.loginip = loginip;
	}
	/**
	 *  返回一个是否过期的标识  过期返回-1
	 * @return
	 */
	public long getLogtimeTimestamp()
	{
		long minus=0L;
		return (minus=(System.currentTimeMillis()-logtimeTimestamp.getTime()))<ConstantValue.OUTOFTIME?minus:-1;
	}
	public void setLogtimeTimestamp(Timestamp logtimeTimestamp)
	{
		this.logtimeTimestamp = logtimeTimestamp;
	}

}
