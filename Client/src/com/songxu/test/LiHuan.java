package com.songxu.test;

public class LiHuan 
{
	
	
	/**
	 * A B�����෴  B��������C�ľű�  A B�����ΪC������
	 * @param args
	 */
	public static void main(String[] args)
	
	{
		
		int x=0;
		int y=0;
		
		/*int ageA=10*x+y;
		int ageB=10*y+x;
		int ageC=ageB/9;
		*/
		
		for(int i=0;i<10;i++)
		{
			for(int j=0;j<10;j++)
			{
				int ageA=10*i+j;
				int ageB=10*j+i;
				double ageC=ageB/10.0;
				
				if(Math.abs(ageA-ageB)==2*ageC)
				{
					System.out.println(i+":"+j);
					
				}
			}
		}
		
	}
	
	
	
	
	
	
	
	
	
	

}
