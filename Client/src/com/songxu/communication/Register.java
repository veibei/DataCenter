package com.songxu.communication;

import java.io.IOException;
import java.util.HashSet;

import javax.swing.JTextArea;

import org.apache.mina.core.session.IoSession;

import com.songxu.interfaces.Message;
import com.songxu.interfaces.MessageImpl;
import com.songxu.memecached.MemecachedOperate;
import com.songxu.mina.client.MinaClientHanlder;

public class Register {
	private int step = 1;
	private String imei_F;
	private IoSession session;
	private JTextArea jTextArea;

	public Register(String imei_F, IoSession session,JTextArea jTextArea) {
		this.imei_F = imei_F;
		this.session = session;
		this.jTextArea=jTextArea;

	}

	private static final String SERVER_IMEI = "0000000000000000";

	private void sendOnce() {
		Message message = new MessageImpl("R1", this.imei_F, SERVER_IMEI, "");
		session.write(message);

	}

	private void sendTwice(Message receive) {
		String receiveMsg = receive.getMsg();
		int receiveInt = Integer.parseInt(receiveMsg);
		Integer msgSend = receiveInt >> 4;
		Message message = new MessageImpl("R3", this.imei_F, SERVER_IMEI,
				msgSend+"");
		session.write(message);

	}
	
	public Communicate registClient(Message message,Object object)
	{
		if(step==1)
		{
			sendOnce();
			step++;
			return null;
		}
		else if(step==2)
		{
			sendTwice(message);
			step++;
			return null;
		}
		else {
			if(message.getMsg().equals("success"))
			{
				registClientInMemeroy();
				MinaClientHanlder minaClientHanlder=(MinaClientHanlder)object;
				minaClientHanlder.allRegister.remove(message.getIMEI_T());
				Communicate communicate=new Communicate(message.getIMEI_T());
				minaClientHanlder.allCommunicator.put(message.getIMEI_T(),communicate);
				
				jTextArea.append(this.imei_F+":连接建立！\n");
				return communicate;
			}
			else {
				return null;
			}
		}
	}
	private  void registClientInMemeroy()
	{
		HashSet<String> collcetion;
		if(this.imei_F.substring(0,1).equals("2"))
		{
			collcetion=(HashSet<String>)MemecachedOperate.get("DTU");
			if(collcetion!=null)
			{
				collcetion.add(this.imei_F);
				MemecachedOperate.update("DTU", collcetion);
			}
			else {
				collcetion=new HashSet<>();
				collcetion.add(this.imei_F);
				MemecachedOperate.add("DTU", collcetion);
				
			}
			
		}
		else {
			collcetion=(HashSet<String>)MemecachedOperate.get("client");
			if(collcetion!=null)
			{
				collcetion.add(this.imei_F);
				MemecachedOperate.update("client", collcetion);
			}
			else {
				collcetion=new HashSet<>();
				collcetion.add(this.imei_F);
				MemecachedOperate.add("client", collcetion);
			}
			
			
			
		}
		
	
		
		
		
	}

}
