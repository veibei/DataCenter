package com.songxu.interfaces;

import java.net.Socket;

/**
 * socket�߳�
 * @author songxu
 *
 */
public interface SocketThread extends Runnable
{
	/**
	 * ֹͣ����
	 */
	public void stop();
	public Socket getSocket();

}
