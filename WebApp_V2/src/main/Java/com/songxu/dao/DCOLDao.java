package com.songxu.dao;

import java.util.List;

import com.songxu.bean.ClientBean;
import com.songxu.bean.DTUBean;

/**
 * ��ѯ���е�DTU/Client�������
 * @author songxu
 *
 */
public interface DCOLDao
{
	/**
	 * ��ȡ��������Client
	 * @return
	 */
	public List<ClientBean> getClientBeans();
	/**
	 * ��ȡ��������DTU
	 * @return
	 */
	public List<DTUBean> getDtuBeans();
	

}
