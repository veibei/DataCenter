package com.songxu.mina.corder;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;

public class Encoder4Object extends ProtocolEncoderAdapter {

	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		
		//session.write(message);
		ObjectSerializationCodecFactory objectSerializationCodecFactory=new ObjectSerializationCodecFactory();
		objectSerializationCodecFactory.getEncoder(session).encode(session, message, out);
		
		//session.write(message);
	}

}
