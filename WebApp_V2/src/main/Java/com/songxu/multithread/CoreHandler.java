package com.songxu.multithread;
/**
 * �����ڷ������ϵĺ��Ĵ����߳� ��ʶ�ӿ�
 * @author songxu
 *
 */
@Deprecated
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
