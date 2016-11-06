package com.songxu.communication;

import java.util.concurrent.ConcurrentHashMap;
/**
 * 统计类
 * 单例
 * @author songxu
 *
 */
public class Record 
{
	private final ConcurrentHashMap<String, Integer> counterSend=new ConcurrentHashMap<>();
	public ConcurrentHashMap<String, Integer> getCounterSend() {
		return counterSend;
	}
	private final ConcurrentHashMap<String, Integer> counterRev=new ConcurrentHashMap<>();
	public ConcurrentHashMap<String, Integer> getCounterRev() {
		return counterRev;
	}
	private static Record record;
	private  Record()
	{
		/*counterRev.put("1111111111111111", 0);
		counterSend.put("1111111111111111", 0);*/
	}
	public static Record getInstance()
	{
		if(record==null)
		{
			record=new Record();
			return record;
		}
		else {
			return record;
		}
		
	}
	
	public void add(String imei)
	{
		counterSend.put(imei, 0);
		counterRev.put(imei, 0);
	}
	public void  recordSend(String imei) 
	{
		if(null==counterSend.get(imei))
		{
			counterSend.put(imei, 0);
		}
		int count =counterSend.get(imei);
		
		counterSend.put(imei, ++count);
	}
	public void  recordRev(String imei) 
	{
		if(null==counterRev.get(imei))
		{
			counterRev.put(imei, 0);
		}
		int count =counterRev.get(imei);
		
		counterRev.put(imei, ++count);
	}
	
}
