package com.songxu.mina.corder;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;

import com.songxu.interfaces.Message;

/**
 * ������
 * @author songxu
 *
 */
public class Encoder extends ProtocolEncoderAdapter
{

	
	@Override
	/**
	 * ����TextLineCodecFactory �����ַ���
	 */
	public void encode(IoSession arg0, Object arg1, ProtocolEncoderOutput arg2)
			throws Exception
	{
		Message message =(Message)arg1;
		if(message.getMsg().contains("C")|message.getMsg().contains("D"))
		{
			System.out.println("�������ݳ���Ŀ�꣺"+message.getIMEI_T()+"|��Ϣ���ݣ�"+message.getMsg());
		}
		String msgSend=message.getString();
		if(msgSend.length()<34)
		{
			System.out.println("���������ʹ������ݣ�"+msgSend);
		}
		TextLineCodecFactory factory=new TextLineCodecFactory();
		factory.getEncoder(arg0).encode(arg0, msgSend, arg2);
		
	}

	

}
