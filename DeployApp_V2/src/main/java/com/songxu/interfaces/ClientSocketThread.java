package com.songxu.interfaces;
/**
 * clientsocket�߳�
 * @author songxu
 *
 */
public interface ClientSocketThread extends SocketThread ,Comparable<ClientSocketThread>
{
	public int getCounter();
	/**
	 * �õ�������������ֵ
	 * @return
	 */
	public int getHeartBeat();
	public void minusHeart();
	/**
	 * ��������������
	 */
	public void resetHeartBeat();

}
