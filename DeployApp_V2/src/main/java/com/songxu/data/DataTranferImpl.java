package com.songxu.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

public class DataTranferImpl implements DataTranfer
{

	@Override
	public String getData(Socket socket) throws IOException
	{
		InputStream inputStream=socket.getInputStream();
		if(inputStream==null)
		{
			return null;
		}
		/**
		 * 可能出现轮询程序还没有轮询到 
		 * 但是Socket远端已经关闭
		 */
		if(inputStream.available()==0)
		{
			return null;
		}
		
		Reader reader=new InputStreamReader(inputStream);
		BufferedReader bufferedReader=new BufferedReader(reader);
		return bufferedReader.readLine();
	}

	@Override
	public void sendData(Socket socket, Object msgObject) throws IOException
	{
		String msg = (String) msgObject;
		OutputStream outputStream=socket.getOutputStream();
		Writer writer=new OutputStreamWriter(outputStream);
		PrintWriter printWriter=new PrintWriter(writer,true);
		printWriter.println(msg);
		printWriter.flush();
		
	}

}
