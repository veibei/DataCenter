package com.songxu.mina.corder;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class CodeFactory implements ProtocolCodecFactory
{

	private final Charset charset;
	public CodeFactory( String chartSetName)
	{
		this.charset=Charset.forName(chartSetName);
	}
	@Override
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception
	{
		return new Decoder(charset);
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception
	{
		return new Encoder();
	}

}
