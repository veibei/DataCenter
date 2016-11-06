package com.songxu.interfaces;
/**
 * 临时socket连接线程
 * @author songxu
 *
 */
public interface TempSocketThread extends SocketThread
{
	public int getStep();
	public void refuse();
	public void changeStep();

}
