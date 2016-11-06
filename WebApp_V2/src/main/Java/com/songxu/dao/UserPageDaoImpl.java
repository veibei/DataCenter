package com.songxu.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.songxu.bean.LogInIp;
import com.songxu.bean.User;
import com.songxu.util.SortLogInIp;

public class UserPageDaoImpl implements UserPageDao
{
	private  SessionFactory sessionFactory;
	/**
	 * 缓存查询过的list
	 */
	private  ConcurrentHashMap<User, List<LogInIp>>cachedList=new ConcurrentHashMap<>();
	public SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	@Override
	/**
	 * 仅第一次查询时触发  用于缓存
	 */
	public List<LogInIp> getAll( User user)
	{
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("select u from User u where u.username= :userinfo").setString("userinfo", user.getUsername());
		@SuppressWarnings("unchecked")
		User userQuery=(User)query.list().get(0);
		Set<LogInIp> set = userQuery.getLoginip();
		List<LogInIp> list=new ArrayList<LogInIp>();
		for (LogInIp logInIp : set)
		{
			list.add(logInIp);
		}
		list.sort(new SortLogInIp());
		cachedList.put(user, list);
		return list;
	}

	@Override
	public List<LogInIp> getSubPage(int index, int perPageCount)
	{
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 分页
	 * @param index
	 * @param perPageCount
	 * @param list
	 * @return
	 */
	public List getSubPage(int index,int perPageCount,List list)
	{
		List listResult=new ArrayList<>();
		
		for(int i=0;i<perPageCount;i++)
		{
			try
			{
				listResult.add(list.get(index*perPageCount+i));
			}
			catch (Exception e)
			{
				break;
			}
		}
		return listResult;
		
	}
	@Override
	public int getPages(int perPageCount)
	{
		return 0;
	}
	
	@Override
	public List<LogInIp> getAll()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
