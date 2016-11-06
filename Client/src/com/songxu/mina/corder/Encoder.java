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
		if(message.getMsg()!=null&&message.getMsg().length()>2&&(message.getMsg().contains("D")|message.getMsg().contains("C")))
		{
			char [] array=message.getMsg().toCharArray();
			System.out.println("客户端发送数据出错");
			System.out.println(array);
			
		}
		String msgSend=message.getString();
		if(msgSend==null)
		{
			System.out.println("发送空数据。。。");
			return;
		}
		
		
		
		TextLineCodecFactory factory=new TextLineCodecFactory();
		factory.getEncoder(arg0).encode(arg0, msgSend, arg2);
		
	}

	

}
