package com.songxu.communication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.mina.core.session.IoSession;

import com.songxu.interfaces.CommonUtil;
import com.songxu.interfaces.Message;
import com.songxu.interfaces.MessageImpl;
import com.songxu.memecached.MemecachedOperate;

public class Communicate {
	private String imei;
	public ArrayList<String> imei_To = null;

	public Communicate(String imei) {
		this.imei = imei;
		if(this.imei.substring(0,1).equals("3"))
		{
			initGroup();//仅对客户端初始化分组
			
		}
	}

	@SuppressWarnings("unchecked")
	public HashSet<String> getValibleClient() {
		return (HashSet<String>) MemecachedOperate.get("client");
	}

	@SuppressWarnings("unchecked")
	public HashSet<String> getValibleDTU() {
		return (HashSet<String>) MemecachedOperate.get("DTU");
	}

	public String randomMsg() {
		Random random=new Random();
		Double data=200+random.nextDouble()*200;
		return data.intValue()+"";
	}

	private int communicated=1;
	public void sendMsg(IoSession session,String imei_F)
	{
		if(this.imei_To!=null)
		{
			if(communicated==1)
			{
				for (String string : imei_To) {
					Message message=new MessageImpl("C1", this.imei, string,
							"P");
					session.write(message);
					communicated++;
				}
			}
			else {
				Message message=new MessageImpl("C1", this.imei, imei_F,
						"P");
				session.write(message);
			}
			
		}
		else {
			Message message=new MessageImpl("D1", this.imei, imei_F,
					randomMsg());
			session.write(message);
		}
	}

	/**
	 * 初始化发送数据分组 仅对客户端有用 DTU只能被动接收
	 */
	public void initGroup() {
		HashSet<String> allDTUHashSet = getValibleDTU();
		imei_To=new ArrayList<>();
		List<String> list = CommonUtil.setTolist(allDTUHashSet);
		if (list.size() > 10) {
			List<Integer> indexsIntegers=CommonUtil.getRandomIntArray(10, list.size());
			for (int i = 0; i < 10; i++) {
				imei_To.add(list.get(indexsIntegers.get(i)));
			}
		} else {
				imei_To=(ArrayList<String>)list;
		}
	}

	@Override
	public String toString() {
		return this.imei_To.toString();
	}
}
