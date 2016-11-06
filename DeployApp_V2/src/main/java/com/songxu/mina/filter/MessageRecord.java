package com.songxu.mina.filter;

import org.apache.log4j.Logger;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

import com.songxu.interfaces.Message;
/**
 * ���ͽӼ�¼��
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
		logger.debug("�յ���Ϣ||���Ͷ�:"+messageR.getIMEI_F()+"||���նˣ�"+messageR.getIMEI_T()+"||��Ϣ���ݣ�"+messageR.getMsg());
		nextFilter.messageReceived(session, message);
	}
	@Override
	public void messageSent(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception
	{
		
		Message messageR=(Message)writeRequest.getMessage();
		logger.debug("������Ϣ||���Ͷ�:"+messageR.getIMEI_F()+"||���նˣ�"+messageR.getIMEI_T()+"||��Ϣ���ݣ�"+messageR.getMsg());
		nextFilter.messageSent(session, writeRequest);
	}

}
