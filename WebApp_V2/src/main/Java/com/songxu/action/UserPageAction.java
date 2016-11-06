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
	private Integer pageCountlogInIp = null;// ��¼����ֵ �ܵ�ҳ�� ����firstload��ʱ�����
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
			//�����ܵ�ҳ��
			int page=logInIps.size()/ perPageCountlogInIp;
			if (logInIps.size() % perPageCountlogInIp != 0)
			{
				page++;
			}
			pageCountlogInIp = page;

			@SuppressWarnings("unchecked")
			List<LogInIp> list = ( new UserPageDaoImpl()).getSubPage(0,
					perPageCountlogInIp, logInIps);
			session.setAttribute("pageCountlogInIp", pageCountlogInIp);// �ܵ�ҳ��
			session.setAttribute("rowsCountlogInIp", logInIps.size());// �ܵļ�¼��
			session.setAttribute("pagelogInIp", list);// ��¼
			session.setAttribute("currentPagelogInIp", 1);// ��ǰҳ
			session.setAttribute("perPageCountlogInIp", perPageCountlogInIp);// ÿҳ������
			// �����¶���ʾ�İ�ť
			List<Integer> listBtn = new ArrayList<Integer>();

			for (int i = 0; i < page; i++)
			{
				if (i + 1 > 10)
				{
					break;
				}
				listBtn.add(i + 1);

			}
			session.setAttribute("listBtnlogInIp", listBtn);// ��ʾҳ��
			return SUCCESS;
		}
		catch (Exception e)
		{
			return ERROR;
		}
		

	}

	/**
	 * ��ȡָ��ҳ��ķ�ҳ
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
			session.setAttribute("pagelogInIp", list);// ��¼
			session.setAttribute("currentPagelogInIp", index);// ��ǰҳ

			// int pageCount=(int) session.getAttribute("pageCount");
			if (pageCountlogInIp > 10)
			{
				List<Integer> listBtn = new ArrayList<Integer>();
				// ��ǰҳ���ڰ�ť���м�
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
				session.setAttribute("listBtnlogInIp", listBtn);// ��ʾҳ��
			}
			else
			{
				// ʲô������
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
			// �����ܵ�ҳ��
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
			session.setAttribute("pageCountlogInIp", pageCountlogInIp);// �ܵ�ҳ��
			session.setAttribute("rowsCountlogInIp", logInIps.size());// �ܵļ�¼��
			session.setAttribute("pagelogInIp", list);// ��¼
			session.setAttribute("currentPagelogInIp", 1);// ��ǰҳ
			session.setAttribute("perPageCountlogInIp", perPageCountlogInIp);// ÿҳ������

			// �����¶���ʾ�İ�ť
			List<Integer> listBtn = new ArrayList<Integer>();

			for (int i = 0; i < page; i++)
			{
				if (i + 1 > 10)
				{
					break;
				}
				listBtn.add(i + 1);

			}
			session.setAttribute("listBtnlogInIp", listBtn);// ��ʾҳ��
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
