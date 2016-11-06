package com.songxu.communication;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RecordThread implements Runnable {

	@Override
	public void run() 
	{
		Record record=Record.getInstance();
		while(true)
		{
			
			System.out.println("==================报告进度开始====================");
			ConcurrentHashMap<String, Integer> hashMap=record.getCounterSend();
			Set<Entry<String, Integer>> entryset=hashMap.entrySet();
			
			System.out.println("==================发送情况====================");
			
			for (Entry<String, Integer> entry : entryset) {
				System.out.println(entry.getKey()+":"+entry.getValue());
			}
			System.out.println("==================接收情况====================");
			
			
			
			hashMap=record.getCounterRev();
			entryset=hashMap.entrySet();
			for (Entry<String, Integer> entry : entryset) {
				System.out.println(entry.getKey()+":"+entry.getValue());
			}
			
			System.out.println("==================报告结束开始====================");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
