package com.songxu.data;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DataTranferImplV3 implements DataTranfer
{

	@Override
	public String getData(Socket socket) throws IOException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendData(Socket socket, Object msgObject) throws IOException
	{
		DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());
		dataOutputStream.writeInt(1);
		dataOutputStream.writeUTF("172.20.202.33");
		dataOutputStream.writeInt(50116);
		dataOutputStream.writeInt(5000);
		dataOutputStream.writeInt(500);
		dataOutputStream.writeInt(500);
		dataOutputStream.writeInt(100);
		dataOutputStream.writeInt(100);
		Date date=Calendar.getInstance().getTime();
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd hh:mm:ss:SSS");
		System.out.println(dateFormat.format(date));
		dataOutputStream.writeUTF(dateFormat.format(date));

	}

}
