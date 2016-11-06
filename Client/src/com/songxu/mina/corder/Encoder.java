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
		if(message.getMsg()!=null&&message.getMsg().length()>2&&(message.getMsg().contains("D")|message.getMsg().contains("C")))
		{
			char [] array=message.getMsg().toCharArray();
			System.out.println("�ͻ��˷������ݳ���");
			System.out.println(array);
			
		}
		String msgSend=message.getString();
		if(msgSend==null)
		{
			System.out.println("���Ϳ����ݡ�����");
			return;
		}
		
		
		
		TextLineCodecFactory factory=new TextLineCodecFactory();
		factory.getEncoder(arg0).encode(arg0, msgSend, arg2);
		
	}

	

}
