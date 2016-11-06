package com.songxu.memcached;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.alisoft.xplatform.asf.cache.IMemcachedCache;

class KeysBean
{

	public KeysBean(String server, long bytes, long expiry)
	{
		super();
		this.server = server;
		this.bytes = bytes;
		this.expiry = expiry;
	}

	private String server;
	private long bytes;
	private long expiry;
	@Override
	public String toString()
	{
		return(server+":"+bytes+":"+expiry);
	}
	public String getServer()
	{
		return server;
	}

	public void setServer(String server)
	{
		this.server = server;
	}

	public long getBytes()
	{
		return bytes;
	}

	public void setBytes(long bytes)
	{
		this.bytes = bytes;
	}

	public long getExpiry()
	{
		return expiry;
	}

	public void setExpiry(long expiry)
	{
		this.expiry = expiry;
	}

}
/**
 * 2015-11-30 
 * 改用阿里缓存
 * @author songxu
 * @version 3.0
 */
public class MemecachedOperate
{
	/**
	 * 工具类  获取时间
	 * @author songxu
	 *
	 */
	final static class DateUtil
	{
		/**
		 * 长过期时间
		 */
		private static final int LongEXP = 1;
		/**
		 * 短过期时间
		 */
		private static final int ShORTEXP =3600*60;
		
		/**
		 * 工具类 不对外提供构造方法
		 */
		private DateUtil()
		{
			
		}
		public static Date getLongExpire()
		{
			Calendar calendar=Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, LongEXP);
			return calendar.getTime();
		
		}
		public static Date getShortExpire()
		{
			Calendar calendar=Calendar.getInstance();
			calendar.add(Calendar.SECOND, ShORTEXP);
			return calendar.getTime();
		}
	}
	public MemecachedOperate()
	{
		this.memcachedClient=AliCacheManager.getIMemcachedCache();
	}
	private  IMemcachedCache memcachedClient;
	
	/**
	 * 2015-12-25 修改 不再使用过期功能  原因待证实
	 * @param key
	 * @param value
	 */
	public  void add(String key, Object value)
	{
		
		//memcachedClient.add(key, value, DateUtil.getShortExpire());
		memcachedClient.put(key, value);
	}
	public  Object get(String key)
	{
		return memcachedClient.get(key);
	}
	/**
	 * 2015-12-25 修改 不再使用过期功能  原因待证实
	 * @param key
	 * @param value
	 */
	public  void update(String key, Object value)
	{
//		memcachedClient.replace(key, value,DateUtil.getShortExpire());
		memcachedClient.replace(key, value);
	}

	public  void remove(String key)
	{
		memcachedClient.remove(key);
	}

	
	/**
	 * 清除所有缓存
	 * @return
	 */
	public boolean clearAll() 
	{
		return memcachedClient.clear();
		
	}
	
	/**
	 * 所有的值
	 * @return
	 */
	public Map<String, Object> getAll()
	{
		Map<String, Object> result=new HashMap<>();
		Set<String> keys=memcachedClient.keySet();
		
		for (String key : keys) {
			
			result.put(key, memcachedClient.get(key));
		}
		
		return result;
	}
	
	
	
}
