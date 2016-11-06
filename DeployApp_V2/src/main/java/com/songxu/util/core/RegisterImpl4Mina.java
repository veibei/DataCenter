package com.songxu.util.core;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;

import com.songxu.core.Server;
import com.songxu.interfaces.Message;
import com.songxu.interfaces.MessageImpl;
import com.songxu.interfaces.Register;
/**
 * 注册类  
 * 配合MINA使用
 * @author songxu
 * @since 2.0
 */
public class RegisterImpl4Mina implements Register
{
	private static Logger logger = Logger.getLogger(RegisterImpl4Mina.class);
	private final Message message;
	private final IoSession session;
	private final Server server;

	public RegisterImpl4Mina(IoSession session, Message message,Server server)
	{
		this.session=session;
		this.message = message;
		this.server=server;
	}

	@Override
	public boolean checkHandleOne()
	{
		String check = message.getCheck();
		Integer integer = Integer.parseInt(check);
		Integer Checked = integer << 4;
		Message messageSend = new MessageImpl("R2", server
				.getIMEI(), message.getIMEI_F(), Checked.toString());
		session.write(messageSend);
		server.getRegisterStep().put(session, 2);
		return true;

	}

	@Override
	public boolean checkHandleTwo()
	{
		Integer checkMsg =Integer.parseInt(message.getMsg()) ;
		
		int checkOne = Integer.parseInt(message.getCheck()) << 4;
		int checktwo = checkOne >> 4;
		if (checkMsg.equals(checktwo))
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	@Override
	public boolean connectionEstablish()
	{
		/**
		 * 建立连接 首先新建一个client线程 关闭temp线程 运行client线程 发送连接成功
		 */

		
		//server.getClientThreadPooL().add(session);//放入所有客户端连接池
		DispatcherImpl4Mina fileterAndDispatcherImpl=new DispatcherImpl4Mina(message, session,server);
		if (fileterAndDispatcherImpl.getClientType() == DispatcherImpl4Mina.DS)
		{
			server.getDTUPool().put(message.getIMEI_F(), session);
			/**
			 * 2015-10-27 新增识别连接建立
			 */
			logger.info("DTU connetion established: "+message.getIMEI_F());
			
		}
		else
		{
			server.getClientPool().put(message.getIMEI_F(), session);
			logger.info("Client connection established:"+message.getIMEI_F());
		}
		/**
		 * 2015-10-26 新增上下行流量统计  此处放入新建的连接，并流量归0
		 */
		server.getRateUpCount().putIfAbsent(message.getIMEI_F(), 0.0d);
		server.getRateDownCount().putIfAbsent(message.getIMEI_F(), 0.0d);
		server.getTempPool().remove(message.getIMEI_F());
		server.getClientThreadPooL().add(session);
		// 发送注册结果
		Message messageSend = new MessageImpl("R4", server.getIMEI(), message.getIMEI_F(), "success");
		
		session.write(messageSend);
		
		return true;

	}

	@Override
	/**
	 * 注册
	 */
	public void regist()
	{
		if (server.getRegisterStep().get(session)==1)
		{
			if (server.getDTUPool().containsKey(message.getIMEI_F())||server.getClientPool().containsKey(message.getIMEI_F()))
			{
				//该连接已存在
				session.close(true);
				server.getRegisterStep().remove(message.getIMEI_F());
			}
			
			//第一次注册时 放入临时连接池
			server.getTempPool().put(message.getIMEI_F(), session);
			checkHandleOne();
		}
		else
		{
			if (checkHandleTwo())
			{
				connectionEstablish();
			}
			else
			{
				this.session.close(true);// 拒绝连接

			}
		}

	}

}
