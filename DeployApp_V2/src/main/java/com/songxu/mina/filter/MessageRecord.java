package com.songxu.mina.filter;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

import com.songxu.interfaces.Message;
/**
 * 发送接记录器
 * @author songxu
 *
 */
public class MessageRecord extends FilterImplment
{
	private static Logger logger=Logger.getLogger(MessageRecord.class);
	
	@Override
	public void init() throws Exception
	{
		logger.info("msg record filter load");
		
	}
	@Override
	public void destroy() throws Exception
	{
		logger.info("msg record filter removed");
	}
	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message) throws Exception
	{
		Message messageR=(Message)message;
		logger.debug("收到消息||发送端:"+messageR.getIMEI_F()+"||接收端："+messageR.getIMEI_T()+"||消息内容："+messageR.getMsg());
		nextFilter.messageReceived(session, message);
	}
	@Override
	public void messageSent(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception
	{
		
		Message messageR=(Message)writeRequest.getMessage();
		logger.debug("发送消息||发送端:"+messageR.getIMEI_F()+"||接收端："+messageR.getIMEI_T()+"||消息内容："+messageR.getMsg());
		nextFilter.messageSent(session, writeRequest);
	}

}
