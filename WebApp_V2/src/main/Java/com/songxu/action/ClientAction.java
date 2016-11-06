package com.songxu.action;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.songxu.bean.ClientBean;
import com.songxu.bean.LogBean;
import com.songxu.dao.DCOLDao;
import com.songxu.dao.DCOLDaoImpl;
import com.songxu.webservice.ConnectionRemoteSystem;

public class ClientAction extends ActionSupport
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3949733943201479419L;
	private DCOLDao dcolDao;
	private int perPageCountClient = 10;
	private Integer pageCountClient = null;// 记录常量值 总的页数 仅在firstload的时候加载
	private List<ClientBean> ClientBeans;

	public DCOLDao getDcolDao()
	{
		return dcolDao;
	}

	public void setDcolDao(DCOLDao dcolDao)
	{
		this.dcolDao = dcolDao;
	}

	public String firstLoad() throws UnknownHostException, IOException
	{
		ServletContext servletContext = ServletActionContext
				.getServletContext();
		String ip = servletContext.getInitParameter("ip");
		int port = Integer.parseInt(servletContext.getInitParameter("port"));
		Socket socket = new Socket(ip, port);
		ConnectionRemoteSystem connectionRemoteSystem = new ConnectionRemoteSystem(
				socket);

		String result = connectionRemoteSystem.getRemoteDCData();
		this.dcolDao = new DCOLDaoImpl(result);
		connectionRemoteSystem.closeConnection();
		socket.close();

		ClientBeans = dcolDao.getClientBeans();

		int page = ClientBeans.size() / perPageCountClient;
		if (ClientBeans.size() % perPageCountClient != 0)
		{
			page++;
		}
		pageCountClient = page;

		@SuppressWarnings("unchecked")
		List<LogBean> list = ((DCOLDaoImpl) dcolDao).getSubPage(0,
				perPageCountClient, ClientBeans);
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.setAttribute("pageCountClient", pageCountClient);// 总的页数
		session.setAttribute("rowsCountClient", ClientBeans.size());// 总的记录数
		session.setAttribute("pageClient", list);// 记录
		session.setAttribute("currentPageClient", 1);// 当前页
		session.setAttribute("perPageCountClient", perPageCountClient);// 每页的数量
		// 处理下端显示的按钮
		List<Integer> listBtn = new ArrayList<Integer>();

		for (int i = 0; i < page; i++)
		{
			if (i + 1 > 10)
			{
				break;
			}
			listBtn.add(i + 1);

		}
		session.setAttribute("listBtnClient", listBtn);// 显示页码
		return SUCCESS;

	}

	/**
	 * 获取指定页码的分页
	 * 
	 * @return
	 */
	public String indexPage() throws Exception
	{
		if (null == pageCountClient || ClientBeans == null)
		{
			firstLoad();

		}

		int index = new Integer(ServletActionContext.getRequest().getParameter(
				"pageIndexClient"));
		@SuppressWarnings("unchecked")
		List<LogBean> list = ((DCOLDaoImpl) dcolDao).getSubPage(index - 1,
				perPageCountClient, ClientBeans);

		HttpSession session = ServletActionContext.getRequest().getSession();
		session.setAttribute("pageClient", list);// 记录
		session.setAttribute("currentPageClient", index);// 当前页

		// int pageCount=(int) session.getAttribute("pageCount");
		if (pageCountClient > 10)
		{
			List<Integer> listBtn = new ArrayList<Integer>();
			// 当前页处于按钮的中间
			if (index > 5)
			{

				if(index+5>=pageCountClient)
				{
					for(int i=pageCountClient-9;i<=pageCountClient;i++)
					{
						listBtn.add(i);
					}
				}
				else 
				{
					for (int i = index - 5; i < index + 5; i++)
					{
						listBtn.add(i);

					}
				}
				

			}
			else
			{
				for (int i = 1; i < 11; i++)
				{
					listBtn.add(i);

				}
			}
			session.setAttribute("listBtnClient", listBtn);// 显示页码
		}
		else
		{
			// 什么都不做
		}

		return SUCCESS;
	}

	public String changePerPageCount()
	{
		perPageCountClient = new Integer(ServletActionContext.getRequest()
				.getParameter("perPageCountClient"));
		// 计算总的页数
		int rowsCount = ClientBeans.size() ;
		int page = rowsCount / perPageCountClient;
		if (ClientBeans.size() % perPageCountClient != 0)
		{
			page++;
		}
		pageCountClient = page;
		@SuppressWarnings("unchecked")
		List<LogBean> list = ((DCOLDaoImpl) dcolDao).getSubPage(0,
				perPageCountClient, ClientBeans);
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.setAttribute("pageCountClient", pageCountClient);// 总的页数
		session.setAttribute("rowsCountClient", ClientBeans.size());// 总的记录数
		session.setAttribute("pageClient", list);// 记录
		session.setAttribute("currentPageClient", 1);// 当前页
		session.setAttribute("perPageCountClient", perPageCountClient);// 每页的数量

		// 处理下端显示的按钮
		List<Integer> listBtn = new ArrayList<Integer>();

		for (int i = 0; i < page; i++)
		{
			if (i + 1 > 10)
			{
				break;
			}
			listBtn.add(i + 1);

		}
		session.setAttribute("listBtnClient", listBtn);// 显示页码
		return SUCCESS;
	}
}
