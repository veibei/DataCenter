package com.songxu.dao;

import java.sql.Timestamp;
import java.util.List;

import com.songxu.aop.AopMethod;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.songxu.bean.LogInIp;
import com.songxu.bean.User;

public class CheckInDaoImpl implements CheckInDao
{
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	@Override
	public boolean containsInMemory(String username)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User access(String username, String password,String ipAddress)
	{
		Session session = sessionFactory.getCurrentSession();
		org.hibernate.Query query = session
				.createQuery("select distinct u from User u where u.username= '"
						+ username+"'");
		@SuppressWarnings("unchecked")
		List<User> result = query.list();
		if (result.size()>0)
		{
			User user=result.get(0);
			if(user.getPassword().equals(password))
			{
				
				//更新登录信息
				LogInIp logInIp=new LogInIp();
				logInIp.setIp(ipAddress);
				Timestamp timestamp=new Timestamp(System.currentTimeMillis());//登录时间戳
				logInIp.setLogTime(timestamp);
				logInIp.setUser(user);
				session.save(logInIp);
				user.setLogtimeTimestamp(timestamp);
				return user;
			}
			else {
				return null;
			}
		}
		else
		{
			/*User user=new User();
			user.setUsername(username);
			user.setPassword(password);
			session.save(user);*/
			return null;
		}
	}

	@Override
	public List<LogInIp> accessLog(int id)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
