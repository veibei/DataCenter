package com.songxu.test;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class BlockQueueTest 
{
	public static void main(String[] args) throws Throwable {
		BlockingDeque<String> blockingDeque=new LinkedBlockingDeque<>();
		
		blockingDeque.put("123");
		
		System.out.println(blockingDeque.size());
		
		
	}

}
