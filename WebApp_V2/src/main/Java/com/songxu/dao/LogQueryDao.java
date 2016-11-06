package com.songxu.dao;

import java.util.List;

import com.songxu.bean.LogBean;

public interface LogQueryDao
{
	/**
	 * ������м�¼
	 * @return
	 */
	public List<LogBean> getAll();
	/**
	 * ��ȡ��ҳ
	 * @param index
	 * @param perPageCount
	 * @return
	 */
	public List<LogBean> getSubPage(int index,int perPageCount);
	/**
	 * ����ܵ�ҳ��
	 * @return
	 */
	public int getPages(int perPageCount);
	
	

}
