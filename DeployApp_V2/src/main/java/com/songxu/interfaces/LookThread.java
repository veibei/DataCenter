package com.songxu.interfaces;

/**
 * �����ڷ������ϵ��ػ��߳�     ��ʶ�ӿ�
 * @author songxu
 *
 */
public interface LookThread extends Runnable
{
	/**
	 * ֹͣ
	 */
	public void stop();
	/**
	 * ������ѯ���ʱ����
	 */
	public void setInterval(int interval);
	
	/**
	 * �ж��Ƿ�ֹͣ
	 * @return ֹͣ��ʶ
	 */
	public boolean getIfStop();

}
