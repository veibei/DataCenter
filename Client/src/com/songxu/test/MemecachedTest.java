package com.songxu.test;

import java.util.HashSet;
import java.util.Set;

import com.songxu.communication.Communicate;
import com.songxu.memecached.MemecachedOperate;

public class MemecachedTest 
{
	public static void main(String[] args) {
		
		/*int a=100;
		MemecachedOperate.add("test", a);
		System.out.println(MemecachedOperate.get("test"));
	*/
		
		Set<String> set=(HashSet<String>)MemecachedOperate.get("DTU");
		System.out.println(set.size());
		Communicate communicate=new Communicate("3");
		System.out.println(communicate.imei_To);
	}

}
