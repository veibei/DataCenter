package com.songxu.multithread;

import java.util.concurrent.Callable;

public interface CoreHandlerV2<V> extends Callable<V>
{
	/**
	 * ֹͣ
	 */
	public void stop();
	/**
	 * �ж��Ƿ�ֹͣ
	 */
	public boolean getIfStop();

}
