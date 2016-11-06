package com.songxu.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

import com.songxu.data.DataTranfer;
import com.songxu.data.DataTranferImpl;

public class DataReceive implements Runnable
{

	private Socket clientSocket;
	private InputStream inputStream;
	private OutputStream outputStream;

	public DataReceive(Socket socket)
	{
		try
		{

			clientSocket = socket;
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run()
	{
		while (true)
		{

			if (null != inputStream)
			{
				DataTranfer dataTranfer = new DataTranferImpl();
				try
				{
					if (clientSocket.isClosed())// ���ǲ鿴�������ӵ�״̬ �޷�֪���Է�������
					{

						break;
					}
					// ȡ������
					String getData = dataTranfer.getData(clientSocket);
					if (getData != null)
					{

						System.out.println(clientSocket
								.getRemoteSocketAddress().toString()
								+ ":"
								+ getData);
						
						String string = clientSocket.getLocalAddress()
								+ "�յ�������";
						string="C02201506151909000020150615190900001100DFSHJDSHAKJLHDFSAKJHFHEWUIHFWEUIHFUIWHEFUHDSKLJJDHASFKLJDHSUHWEUIHFUEWHJKLDSHKLJHFSHADFKLJSHAFUISAHUI";
								
								
						dataTranfer.sendData(clientSocket, string);
					}
					
				}
				catch (IOException e)
				{
					System.out.println("���ݽ����쳣");
					e.printStackTrace();

				}

			}

			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
