package com.songxu.mina.corder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.songxu.interfaces.Message;


public class Decoder4Object extends ProtocolDecoderAdapter {

	@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
			throws Exception {
		
		Object objectReceive=in.getObject();
		
		Message message=(Message)objectReceive;
		if(null!=message)
		{
			System.out.println(message.getString());
		}
		session.write(message);
	
		
		

	}

}
