package com.songxu.interfaces;
/**
 * clientsocket线程
 * @author songxu
 *
 */
public interface ClientSocketThread extends SocketThread ,Comparable<ClientSocketThread>
{
	public int getCounter();
	/**
	 * 得到心跳计数器的值
	 * @return
	 */
	public int getHeartBeat();
	public void minusHeart();
	/**
	 * 重置心跳计数器
	 */
	public void resetHeartBeat();

}
