package com.songxu.action;

import java.net.Socket;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.songxu.bean.RemoteServerBean;
import com.songxu.bean.User;
import com.songxu.util.NetworkUtil;
import com.songxu.webservice.ConnectionRemoteSystem;

public class ServerControlAction extends ActionSupport
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 403883248229619248L;
	public void start() throws Exception
	{
		
		
		
		ServletContext servletContext=ServletActionContext.getServletContext();
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpServletResponse response=ServletActionContext.getResponse();
		
	
		
		//以下代码防止直接从浏览器访问action
		RemoteServerBean bean=(RemoteServerBean)servletContext.getAttribute("remoteServerBean");
		if(bean.getStatus()==RemoteServerBean.Status_Running)
		{
			return ;
		}
		
		String ip=servletContext.getInitParameter("ip");
    	int port=Integer.parseInt(servletContext.getInitParameter("port"));
    	Socket socket;
    	try
		{
    		socket=new Socket(ip,port);
		}
		catch (Exception e)
		{
			response.getWriter().write("close");
			return;
		}
		
		ConnectionRemoteSystem connectionRemoteSystem=new ConnectionRemoteSystem(socket);
		/**
		 * 2015-10-27 增加传递操作ip及用户名功能
		 */
		HttpSession session = request.getSession();
		User user=(User)session.getAttribute("userinfo");
		
		String remoteAddr=NetworkUtil.getRemoteHost(request);
		if(connectionRemoteSystem.StartServer(remoteAddr,user.getUsername()))
		{
			response.getWriter().write("true");
			
			connectionRemoteSystem.closeConnection();
			socket.close();
			return ;
		}
		else 
		{
			connectionRemoteSystem.closeConnection();
			socket.close();
			response.getWriter().write("false");
			return ;
		}
		
	}
	public void stop() throws Exception
	{
		ServletContext servletContext=ServletActionContext.getServletContext();
		HttpServletRequest request=ServletActionContext.getRequest();
		HttpServletResponse response=ServletActionContext.getResponse();
		HttpSession session = request.getSession();
		
		//以下代码防止直接从浏览器访问action
		
		RemoteServerBean bean=(RemoteServerBean)servletContext.getAttribute("remoteServerBean");
		if(bean.getStatus()==RemoteServerBean.Status_Stopped)
		{
			return;
		}
		String ip=servletContext.getInitParameter("ip");
    	int port=Integer.parseInt(servletContext.getInitParameter("port"));
    	Socket socket;
    	try
		{
    		socket=new Socket(ip,port);
		}
		catch (Exception e)
		{
			response.getWriter().write("close");
			return;
		}
    	ConnectionRemoteSystem connectionRemoteSystem=new ConnectionRemoteSystem(socket);
	
		User user=(User)session.getAttribute("userinfo");
		String remoteAddr=NetworkUtil.getRemoteHost(request);
		if(connectionRemoteSystem.StopServer(remoteAddr,user.getUsername()))
		{
			response.getWriter().write("true");
			
			connectionRemoteSystem.closeConnection();
			socket.close();
			return ;
		}
		else 
		{
			connectionRemoteSystem.closeConnection();
			socket.close();
			response.getWriter().write("false");
			return ;
		}
		
	}

}
