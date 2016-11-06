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
	private Integer pageCount = null;// ��¼����ֵ �ܵ�ҳ�� ����firstload��ʱ�����

	public void setLogDaoQuery(LogQueryDao logDaoQuery)
	{
		this.logDaoQuery = logDaoQuery;
	}

	public String firstLoad() throws Exception
	{
		// �����ܵ�ҳ��
		int rowsCount = logDaoQuery.getPages(10);
		int page = rowsCount / perPageCount;
		if (page % perPageCount != 0)
		{
			page++;
		}
		pageCount = page;
		List<LogBean> list = logDaoQuery.getSubPage(0, perPageCount);
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.setAttribute("pageCount", page);// �ܵ�ҳ��
		session.setAttribute("rowsCount", rowsCount);// �ܵļ�¼��
		session.setAttribute("page", list);// ��¼
		session.setAttribute("currentPage", 1);// ��ǰҳ
		session.setAttribute("perPageCount", perPageCount);// ÿҳ������
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
		session.setAttribute("listBtn", listBtn);// ��ʾҳ��
		return SUCCESS;
	}

	/**
	 * ��ȡָ��ҳ��ķ�ҳ
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
		session.setAttribute("page", list);// ��¼
		session.setAttribute("currentPage", index);// ��ǰҳ

		// int pageCount=(int) session.getAttribute("pageCount");
		if (pageCount > 10)
		{
			List<Integer> listBtn = new ArrayList<Integer>();
			// ��ǰҳ���ڰ�ť���м�
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
			session.setAttribute("listBtn", listBtn);// ��ʾҳ��
		}
		else
		{
			// ʲô������
		}

		return SUCCESS;
	}

	public String changePerPageCount()
	{
		perPageCount= new Integer(ServletActionContext.getRequest()
				.getParameter("perPageCount"));
		// �����ܵ�ҳ��
		int rowsCount = logDaoQuery.getPages(10);
		int page = rowsCount / perPageCount;
		if (page % perPageCount != 0)
		{
			page++;
		}
		pageCount = page;
		List<LogBean> list = logDaoQuery.getSubPage(0, perPageCount);
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.setAttribute("pageCount", page);// �ܵ�ҳ��
		session.setAttribute("rowsCount", rowsCount);// �ܵļ�¼��
		session.setAttribute("page", list);// ��¼
		session.setAttribute("currentPage", 1);// ��ǰҳ
		session.setAttribute("perPageCount", perPageCount);// ÿҳ������

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
		session.setAttribute("listBtn", listBtn);// ��ʾҳ��
		return SUCCESS;
	}

}
