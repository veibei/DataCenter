package com.songxu.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.songxu.memcached.MemecachedOperate;

public class RateAnalysisDao 
{
	public  static final String iMEI_SERVER="11111111111111111";
	private MemecachedOperate MemecachedOperate;
	
	public RateAnalysisDao()
	{
		
	}
	/**
	 * ��IMEI��ȡ�ϴ�����
	 * ͶӰת��
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, List> getUpRateData()
	{
		Map<String, List> result = new HashMap<String, List>();
		
		ConcurrentHashMap dataUpInMemcached=(ConcurrentHashMap) MemecachedOperate.get("server.UpRateCount");
		Set<String> keySet=((ConcurrentHashMap)dataUpInMemcached.get(0)).keySet();
		for (String string : keySet)
		{
			List subResult=new ArrayList(5);
			for(int i=0;i<5;i++)
			{
				ConcurrentHashMap<String, Double> dataConcurrentHashMap=(ConcurrentHashMap<String, Double>) dataUpInMemcached.get(i);
				subResult.add(dataConcurrentHashMap.get(string));
			}
			result.put(string, subResult);
		}
		//��ȡ����������
		ConcurrentHashMap serverRateCount=(ConcurrentHashMap) MemecachedOperate.get("server.ServerRateCount");
		List serverResult=new ArrayList(5);
		for(int i=0;i<5;i++)
		{
			String data=(String) serverRateCount.get(i);
			if(null!=data)
			{
				data=data.split("\\|")[1];
			}
			serverResult.add(data);
			
			
		}
		result.put(iMEI_SERVER, serverResult);
		return result;
	}

	/**
	 * ��IMEI��ȡ�ϴ�����
	 * ͶӰת��
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, List> getDownRateData()
	{
		Map<String, List> result = new HashMap<String, List>();
		ConcurrentHashMap dataDownMemcached=(ConcurrentHashMap) MemecachedOperate.get("server.DownRateCount");
		Set<String> keySet=((ConcurrentHashMap)dataDownMemcached.get(0)).keySet();
		
		for (String string : keySet)
		{
			List subResult=new ArrayList(5);
			for(int i=0;i<5;i++)
			{
				ConcurrentHashMap<String, Double> dataConcurrentHashMap=(ConcurrentHashMap<String, Double>) dataDownMemcached.get(i);
				subResult.add(dataConcurrentHashMap.get(string));
			}
			result.put(string, subResult);
		}
		//��ȡ��������������
		ConcurrentHashMap serverRateCount=(ConcurrentHashMap) MemecachedOperate.get("server.ServerRateCount");
		List serverResult=new ArrayList(5);
		for(int i=0;i<5;i++)
		{
			String data=(String) serverRateCount.get(i);
			if(null!=data)
			{
				data=data.split("\\|")[0];
			}
			serverResult.add(data);
			
			
		}
		result.put(iMEI_SERVER, serverResult);
		return result;
	}
	@SuppressWarnings("rawtypes")
	/**
	 * ��ȡClient�����ٷֱ�
	 * ����0|����0|����1|����1
	 * @return
	 */
	public Map<String,String> getClientPercent()
	{
		Map<String,String> result=new HashMap<>();
		
		//��ȡ���е�����������
		Map<String, List> upData=getUpRateData();
		List serverUpData=upData.get(iMEI_SERVER);
		
		
		Map<String, List> downData=getDownRateData();
		List serverDownData=downData.get(iMEI_SERVER);
		
		for (String imeiString : upData.keySet())
		{
			if(imeiString.startsWith("3", 0))
			{
				List clientUp=upData.get(imeiString);
				List clientDown=downData.get(imeiString);
				StringBuilder stringBuilder=new StringBuilder();
				for(int i=0;i<5;i++)
				{
					//����
					try
					{
						double clientCount=(double) clientUp.get(i);
						double serverCount=(new Double(serverUpData.get(i).toString()))*1024;
						Double percent=clientCount/serverCount*100;
						String percentString=percent.toString().length()>4?percent.toString().substring(0,4):percent.toString();
						stringBuilder.append(percentString);
					}
					catch (Exception e)
					{
						stringBuilder.append("0");
					}
					stringBuilder.append("|");
					
					
					
					//����
					try
					{
						double clientCount=(double) clientDown.get(i);
						double serverCount=(new Double(serverDownData.get(i).toString()))*1024;
						Double percent=clientCount/serverCount*100;
						String percentString=percent.toString().length()>4?percent.toString().substring(0,4):percent.toString();
						stringBuilder.append(percentString);
					}
					catch (Exception e)
					{
						stringBuilder.append("0");
					}
					stringBuilder.append("|");
				}
				
				result.put(imeiString, stringBuilder.toString());
			}
		}
		return result;
	}
	@SuppressWarnings("rawtypes")
	/**
	 * ��ȡDTU��ռ����������
	 * @return
	 */
	public Map<String,String> getDTUPercent()
	{
		Map<String,String> result=new HashMap<>();
		
		//��ȡ���е�����������
		Map<String, List> upData=getUpRateData();
		List serverUpData=upData.get(iMEI_SERVER);
		
		
		Map<String, List> downData=getDownRateData();
		List serverDownData=downData.get(iMEI_SERVER);
		
		for (String imeiString : upData.keySet())
		{
			if(imeiString.startsWith("2", 0))
			{
				List clientUp=upData.get(imeiString);
				List clientDown=downData.get(imeiString);
				StringBuilder stringBuilder=new StringBuilder();
				for(int i=0;i<5;i++)
				{
					//����
					try
					{
						double clientCount=(double) clientUp.get(i);
						double serverCount=(new Double(serverUpData.get(i).toString()))*1024;
						Double percent=clientCount/serverCount*100;
						String percentString=percent.toString().length()>4?percent.toString().substring(0,4):percent.toString();
						stringBuilder.append(percentString);
					}
					catch (Exception e)
					{
						stringBuilder.append("0");
					}
					stringBuilder.append("|");
					
					
					
					//����
					try
					{
						double clientCount=(double) clientDown.get(i);
						double serverCount=(new Double(serverDownData.get(i).toString()))*1024;
						Double percent=clientCount/serverCount*100;
						String percentString=percent.toString().length()>4?percent.toString().substring(0,4):percent.toString();
						stringBuilder.append(percentString);
					}
					catch (Exception e)
					{
						stringBuilder.append("0");
					}
					stringBuilder.append("|");
				}
				
				result.put(imeiString, stringBuilder.toString());
			}
		}
		return result;
	}
	/**
	 * ��ȡmap�е����ֵ
	 * @param map
	 * @return
	 */
	public double getMaxValue(Map<String, String>map)
	{
		
		double result=0.0d;
		
		Set<Entry<String, String>> entrySet=map.entrySet();
		for (Entry<String, String> entry : entrySet)
		{
			String valueString=entry.getValue();
			String []splitValueStrings=valueString.split("\\|");
			for (String string : splitValueStrings)
			{
				if(!string.equals(""))
				{
					Double valueNow=new Double(string);
					if(valueNow!=null&&valueNow>result)
					{
						result=valueNow;
					}
				}
			}
		}
		
		
		
		return result;
		
		
		
		
		
		
		
	}
	
	public MemecachedOperate getMemecachedOperate()
	{
		return MemecachedOperate;
	}
	public void setMemecachedOperate(MemecachedOperate memecachedOperate)
	{
		MemecachedOperate = memecachedOperate;
	}
}
