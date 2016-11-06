package com.songxu.interfaces;

/**
 * 运行在服务器上的守护线程     标识接口
 * @author songxu
 *
 */
public interface LookThread extends Runnable
{
	/**
	 * 停止
	 */
	public void stop();
	/**
	 * 设置轮询监测时间间隔
	 */
	public void setInterval(int interval);
	
	/**
	 * 判断是否停止
	 * @return 停止标识
	 */
	public boolean getIfStop();

}
