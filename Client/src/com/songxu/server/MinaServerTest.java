package com.songxu.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MinaServerTest 
{
    public static void main(String[] args) 
	{
    	SocketAcceptor acceptor = new NioSocketAcceptor();   
    	DefaultIoFilterChainBuilder chain = acceptor.getFilterChain(); 
    	chain.addLast("myChin", new ProtocolCodecFilter(     
                new ObjectSerializationCodecFactory()));     
        acceptor.setHandler(new Handler());     
        try {     
            acceptor.bind(new InetSocketAddress(50121)); 
            System.out.println("服务已启动。。。");
        } catch (IOException e) {     
            e.printStackTrace();     
        }     
	}
	
	

}
