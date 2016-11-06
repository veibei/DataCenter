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
	
	 // 创建一个socket连接        
    private static final NioSocketConnector connector; 
	static
	{
		connector=new NioSocketConnector(); 
        // 获取过滤器链          
        DefaultIoFilterChainBuilder chain=connector.getFilterChain();  
          
//        ProtocolCodecFilter filter= new ProtocolCodecFilter(new CodeFactory("GBK"));  
        ProtocolCodecFilter filter= new ProtocolCodecFilter(new CodeFactory4Object());
        ProtocolCodecFilter filter2= new ProtocolCodecFilter(new ObjectSerializationCodecFactory());
        // 添加编码过滤器 处理乱码、编码问题    
        chain.addLast("objectFilter2",filter2);  
        //chain.addLast("objectFilter",filter);  
        // 消息核心处理器       
        connector.setHandler(new MinaClientHanlder());  
        // 设置链接超时时间       
        connector.setConnectTimeoutCheckInterval(30);  
        // 连接服务器，知道端口、地址      
        // 等待连接创建完成      
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
