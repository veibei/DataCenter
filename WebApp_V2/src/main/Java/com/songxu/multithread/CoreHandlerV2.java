package com.songxu.multithread;

import java.util.concurrent.Callable;

public interface CoreHandlerV2<V> extends Callable<V>
{
	/**
	 * Õ£÷π
	 */
	public void stop();
	/**
	 * ≈–∂œ «∑ÒÕ£÷π
	 */
	public boolean getIfStop();

}
