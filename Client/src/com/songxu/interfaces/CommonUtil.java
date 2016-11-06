package com.songxu.interfaces;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class CommonUtil
{
	public static final List<String> imei_collection=new ArrayList<>();
	/**
	 * Éú³ÉËæ»úimeiÂë
	 * @param count
	 * @param type
	 * @return
	 */
	public static final List<String> generalIMEI(int count,char type)
	{
		
		for(int i=0;i<count;i++)
		{
			imei_collection.add(type+getUUID(11)+getCheckRandom());
			
		}
		return imei_collection;
		
		
	}
	public static List<String> setTolist(HashSet<String> set)
	{
		Iterator<String> iterator=set.iterator();
		List<String> list=new ArrayList<>();
		while(iterator.hasNext())
		{
			list.add(iterator.next());
		}
		return list;
		
		
	}
	public static List<Integer> getRandomIntArray(int count,int range)
	{
		List<Integer> list=new ArrayList<>(count);
		for(int i=0;i<count;i++)
		{
			while(true)
			{
				Random random=new Random();
				int r=(int)(random.nextDouble()*range);
				if(!list.contains(r))
				{
					list.add(r);
					break;
				}
			}
			
			
		}
		return list;
	}
	
	private static String getUUID(int length)
	{
		UUID uuid=UUID.randomUUID();
		String uidString=uuid.toString();
		String uidArray=uidString.replace("-", "");
		return uidArray.substring(0,length);
		
		
	}
	private static String getCheckRandom()
	{
		Random random=new Random();
		
		Integer a=(int)(1000+random.nextDouble()*9000);
		return a.toString();
	}
	
	
	
	
	
	

}
