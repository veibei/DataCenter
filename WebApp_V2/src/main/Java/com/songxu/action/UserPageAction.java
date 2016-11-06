package com.songxu.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.hibernate.SessionFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.songxu.bean.LogBean;
import com.songxu.bean.LogInIp;
import com.songxu.bean.User;
import com.songxu.dao.DCOLDaoImpl;
import com.songxu.dao.LogQueryDao;
import com.songxu.dao.UserPageDao;
import com.songxu.dao.UserPageDaoImpl;

public class UserPageAction extends ActionSupport
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6668750613183669077L;
	/**
	 * 
	 */
	private  SessionFactory sessionFactory;
	public SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	private UserPageDao userPageDao;
	private int perPageCountlogInIp = 10;
	private Integer pageCountlogInIp = null;// 记录常量值 总的页数 仅在firstload的时候加载
	private List<LogInIp> logInIps;
	
	public String firstLoad() throws Exception
	{
		try
		{
			HttpSession session = ServletActionContext.getRequest().getSession();
			
			User user=(User) session.getAttribute("userinfo");
			if(null==user)
			{
				return ERROR;
			}
			logInIps=userPageDao.getAll(user);
			//计算总的页数
			int page=logInIps.size()/ perPageCountlogInIp;
			if (logInIps.size() % perPageCountlogInIp != 0)
			{
				page++;
			}
			pageCountlogInIp = page;

			@SuppressWarnings("unchecked")
			List<LogInIp> list = ( new UserPageDaoImpl()).getSubPage(0,
					perPageCountlogInIp, logInIps);
			session.setAttribute("pageCountlogInIp", pageCountlogInIp);// 总的页数
			session.setAttribute("rowsCountlogInIp", logInIps.size());// 总的记录数
			session.setAttribute("pagelogInIp", list);// 记录
			session.setAttribute("currentPagelogInIp", 1);// 当前页
			session.setAttribute("perPageCountlogInIp", perPageCountlogInIp);// 每页的数量
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
			session.setAttribute("listBtnlogInIp", listBtn);// 显示页码
			return SUCCESS;
		}
		catch (Exception e)
		{
			return ERROR;
		}
		

	}

	/**
	 * 获取指定页码的分页
	 * 
	 * @return
	 */
	public String indexPage() throws Exception
	{
		try
		{
			if (null == pageCountlogInIp || logInIps == null)
			{
				firstLoad();

			}

			int index = new Integer(ServletActionContext.getRequest().getParameter(
					"pageIndexlogInIp"));
			@SuppressWarnings("unchecked")
			List<LogInIp> list = ( new UserPageDaoImpl()).getSubPage(index - 1,
					perPageCountlogInIp, logInIps);

			HttpSession session = ServletActionContext.getRequest().getSession();
			session.setAttribute("pagelogInIp", list);// 记录
			session.setAttribute("currentPagelogInIp", index);// 当前页

			// int pageCount=(int) session.getAttribute("pageCount");
			if (pageCountlogInIp > 10)
			{
				List<Integer> listBtn = new ArrayList<Integer>();
				// 当前页处于按钮的中间
				if (index > 5)
				{

					if(index+5>=pageCountlogInIp)
					{
						for(int i=pageCountlogInIp-9;i<=pageCountlogInIp;i++)
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
				session.setAttribute("listBtnlogInIp", listBtn);// 显示页码
			}
			else
			{
				// 什么都不做
			}

			return SUCCESS;
		}
		catch (Exception e)
		{
			return ERROR;
			
		}
		
	}

	public String changePerPageCount()
	{
		try
		{
			perPageCountlogInIp = new Integer(ServletActionContext.getRequest()
					.getParameter("perPageCountlogInIp"));
			// 计算总的页数
			int rowsCount = logInIps.size() ;
			int page = rowsCount / perPageCountlogInIp;
			if (logInIps.size() % perPageCountlogInIp != 0)
			{
				page++;
			}
			pageCountlogInIp = page;
			@SuppressWarnings("unchecked")
			List<LogInIp> list = ( new UserPageDaoImpl()).getSubPage(0,
					perPageCountlogInIp, logInIps);
			HttpSession session = ServletActionContext.getRequest().getSession();
			session.setAttribute("pageCountlogInIp", pageCountlogInIp);// 总的页数
			session.setAttribute("rowsCountlogInIp", logInIps.size());// 总的记录数
			session.setAttribute("pagelogInIp", list);// 记录
			session.setAttribute("currentPagelogInIp", 1);// 当前页
			session.setAttribute("perPageCountlogInIp", perPageCountlogInIp);// 每页的数量

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
			session.setAttribute("listBtnlogInIp", listBtn);// 显示页码
			return SUCCESS;
		}
		catch (Exception e)
		{
			return ERROR;
		}
		
	}
	public UserPageDao getUserPageDao()
	{
		return userPageDao;
	}

	public void setUserPageDao(UserPageDao userPageDao)
	{
		this.userPageDao = userPageDao;
	}
}
