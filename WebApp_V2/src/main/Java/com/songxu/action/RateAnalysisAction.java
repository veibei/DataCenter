package com.songxu.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.annotations.JSON;

import com.opensymphony.xwork2.ActionSupport;
import com.songxu.dao.RateAnalysisDao;

public class RateAnalysisAction extends ActionSupport
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3309647137262646200L;
	private RateAnalysisDao rateAnalysisDao;
	private Map mapClient;
	private Map mapDTU;
	private double maxValue;

	@Override
	/**
	 * 仅作跳转用
	 */
	public String execute() throws Exception
	{
		return SUCCESS;
	}

	public String getRAData()
	{
		this.setMapClient(rateAnalysisDao.getClientPercent());
		this.setMapDTU(rateAnalysisDao.getDTUPercent());
		double value1=rateAnalysisDao.getMaxValue(mapClient);
		double value2=rateAnalysisDao.getMaxValue(mapDTU);
		this.setMaxValue(value1>value2?value1:value2);
		return SUCCESS;

	}


	public void setRateAnalysisDao(RateAnalysisDao rateAnalysisDao)
	{
		this.rateAnalysisDao = rateAnalysisDao;
	}

	@JSON(name = "clientMap")
	public Map getMapClient()
	{
		return mapClient;
	}

	public void setMapClient(Map mapClient)
	{
		this.mapClient = mapClient;
	}

	@JSON(name = "DTUMap")
	public Map getMapDTU()
	{
		return mapDTU;
	}

	public void setMapDTU(Map mapDTU)
	{
		this.mapDTU = mapDTU;
	}
	@JSON(name="maxValue")
	public double getMaxValue()
	{
		return maxValue;
	}

	public void setMaxValue(double maxValue)
	{
		this.maxValue = maxValue;
	}
}
