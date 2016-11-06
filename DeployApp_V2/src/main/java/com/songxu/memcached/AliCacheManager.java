package com.songxu.memcached;

import com.alisoft.xplatform.asf.cache.ICacheManager;
import com.alisoft.xplatform.asf.cache.IMemcachedCache;
import com.alisoft.xplatform.asf.cache.memcached.CacheUtil;
import com.alisoft.xplatform.asf.cache.memcached.MemcachedCacheManager;

public class AliCacheManager
{
	private static ICacheManager<IMemcachedCache> manager;
	// 初始化方法
	static
	{
		try
		{
			manager = CacheUtil.getCacheManager(IMemcachedCache.class,
					MemcachedCacheManager.class.getName());
			manager.setConfigFile("memcached.xml");
			manager.setResponseStatInterval(5 * 1000);
			manager.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static IMemcachedCache getIMemcachedCache()
	{
		return manager.getCache("mclient0");
	}
	public static void stop()
	{
		manager.stop();
	}

}
