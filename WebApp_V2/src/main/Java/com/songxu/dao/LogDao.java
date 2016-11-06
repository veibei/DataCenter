package com.songxu.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.songxu.bean.LogBean;

public class LogDao
{
	private SessionFactory sessionFactory;
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Throwable.class)  
	public List<LogBean> getLog()
	{
		List<LogBean> resultsBeans=null;
		Session session=sessionFactory.getCurrentSession();
		
		Query query=session.createQuery("from LogBean");
		
		resultsBeans=query.list();
		
		return resultsBeans;
	}
	
	
	

	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

}
