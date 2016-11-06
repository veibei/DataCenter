package com.songxu.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Iterator;
import java.util.Set;
/**
 * ������״̬��ѯ
 * @author songxu
 *
 */
public class CheckClientOnline implements Runnable
{

	private Set<Socket> allClients = null;

	public CheckClientOnline(Set<Socket> clients)
	{
		allClients = clients;
	}

	@Override
	public void run()
	{
		while (true)
		{
			synchronized (allClients)
			{
				System.out.println("��ǰ��������" + allClients.size());
				
				if (allClients.size() != 0)
				{
					Iterator<Socket> iterator = allClients.iterator();
					while (iterator.hasNext())
					{
						Socket socket=iterator.next();
						try
						{
							socket.sendUrgentData(0xFF);//����ͻ����Ƿ��Ѿ��Ͽ�
							
						}
						catch (IOException e)
						{
							iterator.remove();
							try
							{
								System.out.println("��������Ѿ��Ͽ�:"+socket.getRemoteSocketAddress());
								socket.close();
							}
							catch (IOException e1)
							{
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							

						}
					}
				}
			}
			try
			{
				Thread.sleep(3000);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
