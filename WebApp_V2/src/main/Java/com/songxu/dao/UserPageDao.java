package com.songxu.dao;

import java.util.List;

import com.songxu.bean.LogInIp;
import com.songxu.bean.User;

public interface UserPageDao
{
	/**
	 * ������м�¼
	 * @return
	 */
	public List<LogInIp> getAll(User user);
	public List<LogInIp> getAll();
	/**
	 * ��ȡ��ҳ
	 * @param index
	 * @param perPageCount
	 * @return
	 */
	public List<LogInIp> getSubPage(int index,int perPageCount);
	/**
	 * ����ܵ�ҳ��
	 * @return
	 */
	public int getPages(int perPageCount);

}
