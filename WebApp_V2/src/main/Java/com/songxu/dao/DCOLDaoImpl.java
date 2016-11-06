package com.songxu.dao;

import java.util.ArrayList;
import java.util.List;

import com.songxu.bean.ClientBean;
import com.songxu.bean.DTUBean;

import javax.annotation.Resource;

@Resource(name = "dcolDao")
public class DCOLDaoImpl implements DCOLDao
{
	private String result;
	private List<ClientBean> clientBeans;
	private List<DTUBean> dtuBeans;
	public DCOLDaoImpl(String result)
	{
		this.result=result;
		wrapResult();
	}
	/**
	 * 包装结果
	 */
	private void wrapResult()
	{
		clientBeans=new ArrayList<ClientBean>(1000);
		dtuBeans=new ArrayList<DTUBean>(1000);
		
		String []subString=result.split("\\|");
		for (String string : subString)
		{
			if(string.length()>0)
			{
				String []result=string.split(",");
				if(result[0].substring(0,1).equals("2"))
				{
					dtuBeans.add(new DTUBean(result[0], result[1], new Double(result[2]), new Double(result[3]), DTUBean.ONLINE));
				}
				else {
					clientBeans.add(new ClientBean(result[0], result[1], new Double(result[2]), new Double(result[3]), ClientBean.ONLINE));
					
				}
			}
		}
		
		

	}
	@Override
	public List<ClientBean> getClientBeans()
	{
		return this.clientBeans;
	}

	@Override
	public List<DTUBean> getDtuBeans()
	{
		return this.dtuBeans;
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

}
