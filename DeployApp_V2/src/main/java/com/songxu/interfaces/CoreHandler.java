package com.songxu.interfaces;
/**
 * 运行在服务器上的核心处理线程 标识接口
 * @author songxu
 *
 */
public interface CoreHandler extends Runnable
{
	/**
	 * 停止
	 */
	public void stop();
	/**
	 * 判断是否停止
	 */
	public boolean getIfStop();

}
