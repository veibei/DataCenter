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
import com.songxu.bean.DTUBean;
import com.songxu.bean.LogBean;
import com.songxu.dao.DCOLDao;
import com.songxu.dao.DCOLDaoImpl;
import com.songxu.webservice.ConnectionRemoteSystem;

public class DTUAction extends ActionSupport
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3949733943201479419L;
	private DCOLDao dcolDao;
	private int perPageCountDTU = 10;
	private Integer pageCountDTU = null;// ��¼����ֵ �ܵ�ҳ�� ����firstload��ʱ�����
	private List<DTUBean> dtuBeans;

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

		dtuBeans = dcolDao.getDtuBeans();

		int page = dtuBeans.size() / perPageCountDTU;
		if (dtuBeans.size() % perPageCountDTU != 0)
		{
			page++;
		}
		pageCountDTU = page;

		@SuppressWarnings("unchecked")
		List<LogBean> list = ((DCOLDaoImpl) dcolDao).getSubPage(0,
				perPageCountDTU, dtuBeans);
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.setAttribute("pageCountDTU", pageCountDTU);// �ܵ�ҳ��
		session.setAttribute("rowsCountDTU", dtuBeans.size());// �ܵļ�¼��
		session.setAttribute("pageDTU", list);// ��¼
		session.setAttribute("currentPageDTU", 1);// ��ǰҳ
		session.setAttribute("perPageCountDTU", perPageCountDTU);// ÿҳ������
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
		session.setAttribute("listBtnDTU", listBtn);// ��ʾҳ��
		return SUCCESS;

	}

	/**
	 * ��ȡָ��ҳ��ķ�ҳ
	 * 
	 * @return
	 */
	public String indexPage() throws Exception
	{
		if (null == pageCountDTU || dtuBeans == null)
		{
			firstLoad();

		}

		int index = new Integer(ServletActionContext.getRequest().getParameter(
				"pageIndexDTU"));
		@SuppressWarnings("unchecked")
		List<LogBean> list = ((DCOLDaoImpl) dcolDao).getSubPage(index - 1,
				perPageCountDTU, dtuBeans);

		HttpSession session = ServletActionContext.getRequest().getSession();
		session.setAttribute("pageDTU", list);// ��¼
		session.setAttribute("currentPageDTU", index);// ��ǰҳ

		// int pageCount=(int) session.getAttribute("pageCount");
		if (pageCountDTU > 10)
		{
			List<Integer> listBtn = new ArrayList<Integer>();
			// ��ǰҳ���ڰ�ť���м�
			if (index > 5)
			{

				if(index+5>=pageCountDTU)
				{
					for(int i=pageCountDTU-9;i<=pageCountDTU;i++)
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
			session.setAttribute("listBtnDTU", listBtn);// ��ʾҳ��
		}
		else
		{
			// ʲô������
		}

		return SUCCESS;
	}

	public String changePerPageCount()
	{
		perPageCountDTU = new Integer(ServletActionContext.getRequest()
				.getParameter("perPageCountDTU"));
		// �����ܵ�ҳ��
		int rowsCount = dtuBeans.size() ;
		int page = rowsCount / perPageCountDTU;
		if (dtuBeans.size() % perPageCountDTU != 0)
		{
			page++;
		}
		pageCountDTU = page;
		@SuppressWarnings("unchecked")
		List<LogBean> list = ((DCOLDaoImpl) dcolDao).getSubPage(0,
				perPageCountDTU, dtuBeans);
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.setAttribute("pageCountDTU", pageCountDTU);// �ܵ�ҳ��
		session.setAttribute("rowsCountDTU", dtuBeans.size());// �ܵļ�¼��
		session.setAttribute("pageDTU", list);// ��¼
		session.setAttribute("currentPageDTU", 1);// ��ǰҳ
		session.setAttribute("perPageCountDTU", perPageCountDTU);// ÿҳ������

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
		session.setAttribute("listBtnDTU", listBtn);// ��ʾҳ��
		return SUCCESS;
	}
	
}
