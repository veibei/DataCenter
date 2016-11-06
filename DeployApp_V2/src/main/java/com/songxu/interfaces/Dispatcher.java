package com.songxu.interfaces;

/**
 * 核心控制器
 * @author songxu
 *
 */
public interface Dispatcher
{

	/**
	 * 获取发送数据类型
	 * @return
	 */
	public int checkDataType();
	/**
	 * 获取客户端类型
	 * @return
	 */
	public int getClientType();
	public void dispatchReceive();
	public void dispatchSend();
	public void setClientSocketThread(ClientSocketThread clientSocketThread);
	
	

}
