package com.songxu.mina.client;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.songxu.mina.corder.CodeFactory;
import com.songxu.mina.corder.CodeFactory4Object;

public final class MinaClient 
{
	
	 // ����һ��socket����        
    private static final NioSocketConnector connector; 
	static
	{
		connector=new NioSocketConnector(); 
        // ��ȡ��������          
        DefaultIoFilterChainBuilder chain=connector.getFilterChain();  
          
//        ProtocolCodecFilter filter= new ProtocolCodecFilter(new CodeFactory("GBK"));  
        ProtocolCodecFilter filter= new ProtocolCodecFilter(new CodeFactory4Object());
        ProtocolCodecFilter filter2= new ProtocolCodecFilter(new ObjectSerializationCodecFactory());
        // ��ӱ�������� �������롢��������    
        chain.addLast("objectFilter2",filter2);  
        //chain.addLast("objectFilter",filter);  
        // ��Ϣ���Ĵ�����       
        connector.setHandler(new MinaClientHanlder());  
        // �������ӳ�ʱʱ��       
        connector.setConnectTimeoutCheckInterval(30);  
        // ���ӷ�������֪���˿ڡ���ַ      
        // �ȴ����Ӵ������      
        /*cf.awaitUninterruptibly();  
        cf.getSession().getCloseFuture().awaitUninterruptibly();  
        connector.dispose();  */
	}
	public static IoSession getConnect(String address,int bindPort)
	{
		ConnectFuture cf = connector.connect(new InetSocketAddress(address,bindPort));  
		IoSession session=null;
		try {
			session= cf.awaitUninterruptibly().getSession();
		} catch (Exception e) 
		{
			
		}
		finally
		{
			return session;
		}
		
		
        
	}

}
