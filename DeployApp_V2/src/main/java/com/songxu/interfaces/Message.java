package com.songxu.interfaces;

import java.io.Serializable;


/**
 * ��Ϣ��ӿ�
 * ��ϵͳ�ڲ� ��Ϣ��Message��ʽ��ת
 * @author songxu
 *
 */
public interface Message extends Serializable
{
	/**
	 * ��װ��Ϣ
	 * @param receive
	 * @return
	 */
	public Message wrapMessage(String receive);
	/**
	 * ������Ϣ���ַ���
	 * @return
	 */
	public String  getString();
	/**
	 * �����Ϣ״̬��
	 * @return
	 */
	public String  getStatus();
	/**
	 * �����Ϣ��Դ
	 * @return
	 */
	public String  getIMEI_F();
	/**
	 * �����Ϣȥ��
	 * @return
	 */
	public String  getIMEI_T();
	/**
	 * �����Ϣ����
	 * @return
	 */
	public String  getMsg();
	/**
	 * ��ȡ��ϢУ����
	 * @return
	 */
	public String  getCheck();
	/**
	 * ���������Ƿ�Ϸ�
	 * @return
	 */
	public boolean checkDataValible();

}
