package com.songxu.dao;

import java.util.List;

import com.songxu.bean.ClientBean;
import com.songxu.bean.DTUBean;

/**
 * 查询所有的DTU/Client连接情况
 * @author songxu
 *
 */
public interface DCOLDao
{
	/**
	 * 获取所有在线Client
	 * @return
	 */
	public List<ClientBean> getClientBeans();
	/**
	 * 获取所有在线DTU
	 * @return
	 */
	public List<DTUBean> getDtuBeans();
	

}
