package com.songxu.interfaces;
/**
 * ��ʱsocket�����߳�
 * @author songxu
 *
 */
public interface TempSocketThread extends SocketThread
{
	public int getStep();
	public void refuse();
	public void changeStep();

}
