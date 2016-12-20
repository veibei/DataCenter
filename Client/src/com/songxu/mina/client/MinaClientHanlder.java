package com.songxu.mina.client;

import java.util.HashMap;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.springframework.context.support.StaticApplicationContext;

import com.songxu.communication.Communicate;
import com.songxu.communication.Record;
import com.songxu.communication.Register;
import com.songxu.interfaces.CommonUtil;
import com.songxu.interfaces.Message;
import com.songxu.ui.Main;

public class MinaClientHanlder extends IoHandlerAdapter {
	
	public HashMap<String, Register> allRegister=new HashMap<>();
	public HashMap<String, Communicate> allCommunicator=new HashMap<>();
	public Record record=Record.getInstance();
	private int index=0;
	public void sessionOpened(IoSession session) throws Exception {
		String imei_F=CommonUtil.imei_collection.get(index++);
		Register register=new Register(imei_F, session, Main.txtLog);
		
		allRegister.put(imei_F, register);
		register.registClient(null,null);
		
	}
	public void messageSent(IoSession session, Object message) throws Exception {
		
	};

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {

		Message messageR=(Message)message;
		Register register=allRegister.get(messageR.getIMEI_T());
		if(null!=register)
		{
			Communicate communicate;
			if((communicate=register.registClient(messageR,this))!=null)
			{
				communicate.sendMsg(session, messageR.getIMEI_F());
				record.recordSend(messageR.getIMEI_F());
			}
			
		}
		else {
			Main.txtLog.append(messageR.getIMEI_T()+":收到来自|"+messageR.getIMEI_F()+"|消息："+messageR.getMsg()+"\n========================\n");
			Main.scrollAndSetCursor();
			record.recordRev(messageR.getIMEI_T());
			allCommunicator.get(messageR.getIMEI_T()).sendMsg(session, messageR.getIMEI_F());
			record.recordSend(messageR.getIMEI_F());
		}
		
		
		
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {

		System.out.println("客户端连接关闭");
	}

}
