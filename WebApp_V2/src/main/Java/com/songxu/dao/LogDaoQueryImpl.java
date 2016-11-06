package com.songxu.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import com.songxu.bean.LogBean;

public class LogDaoQueryImpl implements LogQueryDao
{
	private SessionFactory sessionFactory;

	public LogDaoQueryImpl()
	{

	}

	@Override
	public List<LogBean> getAll()
	{

		Session session = sessionFactory.getCurrentSession();

		Query query = session.createQuery("from LogBean");
		@SuppressWarnings("unchecked")
		List<LogBean> list = (List<LogBean>) query.list();
		for(LogBean bean:list)
		{
			bean.setDateString(bean.getLog_date());
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<LogBean> getSubPage(int index, int perPageCount)
	{
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LogBean.class);
		criteria.addOrder(Order.desc("id"));
		criteria.setFirstResult(index*perPageCount);//第一条记录的位置
		criteria.setMaxResults(perPageCount);//每列的记录数
		List<LogBean> list = (List<LogBean>)criteria.list();
		for(LogBean bean:list)
		{
			bean.setDateString(bean.getLog_date());
		}
		return list;
		
	}

	@Override
	/**
	 * 得到所有的记录数
	 */
	public int getPages(int perPageCount)
	{
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LogBean.class);
		criteria.setProjection(Projections.projectionList().add(
				Projections.rowCount()));
		criteria.setProjection(Projections.projectionList().add(
				Projections.rowCount()));
		@SuppressWarnings("rawtypes")
		List list = criteria.list();
		Long res= (Long) list.get(0);
		return new Integer(res.toString());
	

	}

	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

}
