package com.songxu.interfaces;
/**
 * �����ڷ������ϵĺ��Ĵ����߳� ��ʶ�ӿ�
 * @author songxu
 *
 */
public interface CoreHandler extends Runnable
{
	/**
	 * ֹͣ
	 */
	public void stop();
	/**
	 * �ж��Ƿ�ֹͣ
	 */
	public boolean getIfStop();

}
