package com.songxu.dao;

import java.util.List;

import com.songxu.bean.LogInIp;
import com.songxu.bean.User;

public interface UserPageDao
{
	/**
	 * 获得所有记录
	 * @return
	 */
	public List<LogInIp> getAll(User user);
	public List<LogInIp> getAll();
	/**
	 * 获取分页
	 * @param index
	 * @param perPageCount
	 * @return
	 */
	public List<LogInIp> getSubPage(int index,int perPageCount);
	/**
	 * 获得总的页数
	 * @return
	 */
	public int getPages(int perPageCount);

}
