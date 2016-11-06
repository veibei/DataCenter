package com.songxu.mina.filter;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;


public class FilterImplment implements IoFilter
{

	
	@Override
	public void init() throws Exception
	{
	}

	@Override
	public void destroy() throws Exception
	{
	}

	@Override
	public void onPreAdd(IoFilterChain parent, String name,
			NextFilter nextFilter) throws Exception
	{
	
	}

	@Override
	public void onPostAdd(IoFilterChain parent, String name,
			NextFilter nextFilter) throws Exception
	{

	}

	@Override
	public void onPreRemove(IoFilterChain parent, String name,
			NextFilter nextFilter) throws Exception
	{

	}

	@Override
	public void onPostRemove(IoFilterChain parent, String name,
			NextFilter nextFilter) throws Exception
	{

	}

	@Override
	public void sessionCreated(NextFilter nextFilter, IoSession session)
			throws Exception
	{
		nextFilter.sessionCreated(session);
	}

	@Override
	public void sessionOpened(NextFilter nextFilter, IoSession session)
			throws Exception
	{
		nextFilter.sessionOpened(session);
	}

	@Override
	public void sessionClosed(NextFilter nextFilter, IoSession session)
			throws Exception
	{
		nextFilter.sessionClosed(session);
	}

	@Override
	public void sessionIdle(NextFilter nextFilter, IoSession session,
			IdleStatus status) throws Exception
	{
		nextFilter.sessionIdle(session, status);
	}

	@Override
	public void exceptionCaught(NextFilter nextFilter, IoSession session,
			Throwable cause) throws Exception
	{
		nextFilter.exceptionCaught(session, cause);
	}

	@Override
	public void inputClosed(NextFilter nextFilter, IoSession session)
			throws Exception
	{
		nextFilter.inputClosed(session);
	}

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message) throws Exception
	{
		nextFilter.messageReceived(session, message);
		
		
	}

	@Override
	public void messageSent(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception
	{
		nextFilter.messageSent(session, writeRequest);
	}

	@Override
	public void filterClose(NextFilter nextFilter, IoSession session)
			throws Exception
	{
		nextFilter.filterClose(session);
	}

	@Override
	public void filterWrite(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception
	{
		nextFilter.filterWrite(session, writeRequest);
	}

	

}
