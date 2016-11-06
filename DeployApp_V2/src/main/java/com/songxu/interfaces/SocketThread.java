package com.songxu.interfaces;

import java.net.Socket;

/**
 * socket线程
 * @author songxu
 *
 */
public interface SocketThread extends Runnable
{
	/**
	 * 停止方法
	 */
	public void stop();
	public Socket getSocket();

}
