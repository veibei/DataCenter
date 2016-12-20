package com.songxu.test;

import java.util.Random;

public class IntegerTest {
	public static void main(String[] args) {
		Random random=new Random();
		
		double result=random.nextDouble()*10000;
		System.out.println((int)result);
	}

}
