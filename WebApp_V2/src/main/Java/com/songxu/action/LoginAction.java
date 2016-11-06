package com.songxu.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.songxu.bean.User;
import com.songxu.dao.CheckInDao;
import com.songxu.util.NetworkUtil;

public class LoginAction extends ActionSupport
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CheckInDao checkInDao;
	/**
	 * 仅作为跳转index页面
	 * @return
	 * @throws Exception
	 */
	public String indexPage() throws Exception
	{
		
		return SUCCESS;
	}
	/**
	 * 验证用户名和密码
	 * @return
	 * @throws IOException 
	 */
	public String login() throws IOException
	{
		HttpServletRequest request=ServletActionContext.getRequest();
		String ipAddress=NetworkUtil.getRemoteHost(request);
		HttpServletResponse response=ServletActionContext.getResponse();
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		if(username==null||password==null)
		{
			return ERROR;
		}
		User user;
		if((user=checkInDao.access(username, password,ipAddress))!=null)
		{
			request.getSession().setAttribute("userinfo", user);
			response.getWriter().write("true");
		}
		else {
			response.getWriter().write("false");
		}
		
		return null;
	}
	public String logout()
	{
		HttpServletRequest request=ServletActionContext.getRequest();
		request.getSession().setAttribute("userinfo", null);
		return SUCCESS;
	}

    public String getServerInfo() {
        return SUCCESS;
    }




	public String userPage()
	{
		return SUCCESS;
	}
	public CheckInDao getCheckInDao()
	{
		return checkInDao;
	}
	public void setCheckInDao(CheckInDao checkInDao)
	{
		this.checkInDao = checkInDao;
	}

}
