package com.songxu.mina.corder;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.songxu.interfaces.Message;
import com.songxu.interfaces.MessageImpl;

/**
 * ������
 * 
 * @author songxu
 *
 */
public class Decoder extends ProtocolDecoderAdapter {

	private final Charset charset;

	public Decoder(Charset charset) {
		this.charset = charset;
	}

	@Override
	public void decode(IoSession arg0, IoBuffer arg1, ProtocolDecoderOutput arg2)
			throws Exception {
		String receive = arg1.getString(charset.newDecoder());
		if (receive.length() > 40
				&& (receive.substring(34).contains("D") || receive
						.substring(34).contains("C"))) {
			
			String[] allReceive = receive.split("\n");
			for (String string : allReceive) {
				//System.out.println("�յ������ݣ�" + string);
				if (string.length() == 0) {
					continue;
				}
				Message message = new MessageImpl();
				message = message.wrapMessage(receive);
				arg2.write(message);
			}

		} else {
			Message message = new MessageImpl();
			message = message.wrapMessage(receive);
			if (message == null) {
				System.out.println("���տ�����,ʵ�ʽ�������Ϊ��" + receive);
				return;
			}
			if (message.getMsg().contains("C") | message.getMsg().contains("D")) {
				System.out.println("�������ݳ���Ŀ�꣺" + message.getIMEI_T()
						+ "|��Ϣ���ݣ�" + message.getMsg());
			}

			arg2.write(message);
		}

	}

}
