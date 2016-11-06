package com.songxu.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 数据转换类
 * @author songxu
 *
 */
public interface DataTranfer
{
	public String getData(Socket socket) throws IOException;

	public void sendData(Socket socket,Object msgObject) throws IOException;
}
