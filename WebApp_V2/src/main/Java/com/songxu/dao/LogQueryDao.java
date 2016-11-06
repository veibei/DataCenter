package com.songxu.dao;

import java.util.List;

import com.songxu.bean.LogBean;

public interface LogQueryDao
{
	/**
	 * 获得所有记录
	 * @return
	 */
	public List<LogBean> getAll();
	/**
	 * 获取分页
	 * @param index
	 * @param perPageCount
	 * @return
	 */
	public List<LogBean> getSubPage(int index,int perPageCount);
	/**
	 * 获得总的页数
	 * @return
	 */
	public int getPages(int perPageCount);
	
	

}
