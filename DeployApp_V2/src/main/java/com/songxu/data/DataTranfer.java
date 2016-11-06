package com.songxu.data;

import java.io.IOException;
import java.net.Socket;

/**
 * ����ת����
 * @author songxu
 *
 */
public interface DataTranfer
{
	public String getData(Socket socket) throws IOException;

	public void sendData(Socket socket,Object msgObject) throws IOException;
}
