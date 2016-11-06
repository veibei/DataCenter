package com.songxu.interfaces;

/**
 * ���Ŀ�����
 * @author songxu
 *
 */
public interface Dispatcher
{

	/**
	 * ��ȡ������������
	 * @return
	 */
	public int checkDataType();
	/**
	 * ��ȡ�ͻ�������
	 * @return
	 */
	public int getClientType();
	public void dispatchReceive();
	public void dispatchSend();
	public void setClientSocketThread(ClientSocketThread clientSocketThread);
	
	

}
