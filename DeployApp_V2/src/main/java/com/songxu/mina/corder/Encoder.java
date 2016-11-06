package com.songxu.mina.corder;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;

import com.songxu.interfaces.Message;

/**
 * 编码器
 * @author songxu
 *
 */
public class Encoder extends ProtocolEncoderAdapter
{

	
	@Override
	/**
	 * 利用TextLineCodecFactory 发送字符串
	 */
	public void encode(IoSession arg0, Object arg1, ProtocolEncoderOutput arg2)
			throws Exception
	{
		Message message =(Message)arg1;
		if(message.getMsg().contains("C")|message.getMsg().contains("D"))
		{
			System.out.println("发送数据出错！目标："+message.getIMEI_T()+"|消息内容："+message.getMsg());
		}
		String msgSend=message.getString();
		if(msgSend.length()<34)
		{
			System.out.println("服务器发送错误数据："+msgSend);
		}
		TextLineCodecFactory factory=new TextLineCodecFactory();
		factory.getEncoder(arg0).encode(arg0, msgSend, arg2);
		
	}

	

}
