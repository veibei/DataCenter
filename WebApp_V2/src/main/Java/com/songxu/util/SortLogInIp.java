package com.songxu.util;

import java.util.Comparator;

import com.songxu.bean.LogInIp;
/**
 * 为了倒序排列的比较器
 * @author songxu
 *
 */
public class SortLogInIp implements Comparator<LogInIp>
{

	@Override
	public int compare(LogInIp o1, LogInIp o2)
	{
		// TODO Auto-generated method stub
		return o2.getLogTime().compareTo(o1.getLogTime());
	}

}
