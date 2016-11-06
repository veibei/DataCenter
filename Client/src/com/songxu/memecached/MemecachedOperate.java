package com.songxu.memecached;

import com.danga.MemCached.MemCachedClient;

public class MemecachedOperate 
{
	private static MemCachedClient client;
	/**
	 * 过期时间
	 */
	private static final int  exp=24*3600;
	static
	{
		setClient(MemecachedClientUtil.getInstance());
	}
	
	public static void setClient(MemCachedClient client) {
		MemecachedOperate.client = client;
	}
	public static void add(String key,Object value)
	{
		client.add(key, value, exp);
	}
	public static Object get(String key)
	{
		return client.get(key);
	}
	public static void update(String key,Object value)
	{
		client.replace(key, value, exp);
	}
	public static void remove(String key)
	{
		client.delete(key);
	}
}
