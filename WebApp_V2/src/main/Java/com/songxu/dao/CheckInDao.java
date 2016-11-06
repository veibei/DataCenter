package com.songxu.dao;

import java.util.List;

import com.songxu.bean.LogInIp;
import com.songxu.bean.User;

public interface CheckInDao
{
	public  boolean containsInMemory(String username);
	public User access(String username,String password,String ipAddress);
	public List<LogInIp> accessLog(int id);

}
