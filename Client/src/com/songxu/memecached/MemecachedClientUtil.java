package com.songxu.memecached;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

public final class MemecachedClientUtil {
	/*private static MemcachedClient client;
	static {
		try {
			client = new MemcachedClient(new InetSocketAddress("172.20.202.92",
					50120));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	 private static MemCachedClient client = new MemCachedClient();
     
	    static {
	        //����Socket���ӳ�ʵ��
	        SockIOPool spool = SockIOPool.getInstance();        
	        String[] servers = {"172.20.202.33:50120"};
	        Integer[] weights = {3};        
	        //���÷�������Ϣ
	        spool.setServers(servers);
	        spool.setWeights(weights);      
	        spool.setFailover(true);        
	        //���ó�ʼ����������С������������Լ������ʱ��
	        spool.setInitConn(5);
	        spool.setMinConn(5);
	        spool.setMaxConn(250);
	        spool.setMaxIdle(1000 * 60 * 60 * 6);          
	        //�������߳�˯��ʱ��
	        spool.setMaintSleep(30);           
	        //����TCP���������ӳ�ʱ��
	        spool.setNagle(false);
	        spool.setSocketTO(3000);
	        spool.setSocketConnectTO(0);
	        spool.setAliveCheck(true);         
	        //��ʼ�����ӳ�
	        spool.initialize(); 
	    }   
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static MemCachedClient getInstance()
	{
		return client;
	}

}
