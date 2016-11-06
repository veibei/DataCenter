package com.songxu.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.songxu.bean.LogBean;
import com.songxu.dao.LogQueryDao;

public class LogAction extends ActionSupport
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LogQueryDao logDaoQuery;
	private int perPageCount = 10;
	private Integer pageCount = null;// 记录常量值 总的页数 仅在firstload的时候加载

	public void setLogDaoQuery(LogQueryDao logDaoQuery)
	{
		this.logDaoQuery = logDaoQuery;
	}

	public String firstLoad() throws Exception
	{
		// 计算总的页数
		int rowsCount = logDaoQuery.getPages(10);
		int page = rowsCount / perPageCount;
		if (page % perPageCount != 0)
		{
			page++;
		}
		pageCount = page;
		List<LogBean> list = logDaoQuery.getSubPage(0, perPageCount);
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.setAttribute("pageCount", page);// 总的页数
		session.setAttribute("rowsCount", rowsCount);// 总的记录数
		session.setAttribute("page", list);// 记录
		session.setAttribute("currentPage", 1);// 当前页
		session.setAttribute("perPageCount", perPageCount);// 每页的数量
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
		session.setAttribute("listBtn", listBtn);// 显示页码
		return SUCCESS;
	}

	/**
	 * 获取指定页码的分页
	 * 
	 * @return
	 */
	public String indexPage() throws Exception
	{
		if (null == pageCount)
		{
			firstLoad();

		}

		int index = new Integer(ServletActionContext.getRequest().getParameter(
				"pageIndex"));
		List<LogBean> list = logDaoQuery.getSubPage(index - 1, perPageCount);

		HttpSession session = ServletActionContext.getRequest().getSession();
		session.setAttribute("page", list);// 记录
		session.setAttribute("currentPage", index);// 当前页

		// int pageCount=(int) session.getAttribute("pageCount");
		if (pageCount > 10)
		{
			List<Integer> listBtn = new ArrayList<Integer>();
			// 当前页处于按钮的中间
			if (index > 5)
			{

				if(index+5>=pageCount)
				{
					for(int i=pageCount-9;i<=pageCount;i++)
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
			session.setAttribute("listBtn", listBtn);// 显示页码
		}
		else
		{
			// 什么都不做
		}

		return SUCCESS;
	}

	public String changePerPageCount()
	{
		perPageCount= new Integer(ServletActionContext.getRequest()
				.getParameter("perPageCount"));
		// 计算总的页数
		int rowsCount = logDaoQuery.getPages(10);
		int page = rowsCount / perPageCount;
		if (page % perPageCount != 0)
		{
			page++;
		}
		pageCount = page;
		List<LogBean> list = logDaoQuery.getSubPage(0, perPageCount);
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.setAttribute("pageCount", page);// 总的页数
		session.setAttribute("rowsCount", rowsCount);// 总的记录数
		session.setAttribute("page", list);// 记录
		session.setAttribute("currentPage", 1);// 当前页
		session.setAttribute("perPageCount", perPageCount);// 每页的数量

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
		session.setAttribute("listBtn", listBtn);// 显示页码
		return SUCCESS;
	}

}
