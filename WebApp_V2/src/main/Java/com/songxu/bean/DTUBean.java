package com.songxu.bean;

public class DTUBean
{
	public static final String ONLINE = "‘⁄œﬂ";
	public static final String OFFLINE = "¿Îœﬂ";
	private String IMEI;
	private String IP;
	private double up;
	private double down;
	
	public DTUBean(String iMEI, String iP, double up, double down, String status)
	{
		super();
		IMEI = iMEI;
		IP = iP;
		this.up = up;
		this.down = down;
		this.status = status;
	}
	public String getDown()
	{
		if(down<1024)
		{
			return down+"bytes";
		}
		else if(down<1024*1024)
		{
			return down/1024+"kb";
		}
		else if(down <1024*1024*1024)
		{
			return down/(1024*1024)+"MB";
		}
		else if(down<1024*1024*1024*1024)
		{
			return down/(1024*1024*1024)+"GB";
		}
		else {
			return "1TB";
		}
	}
	public void setDown(double down)
	{
		this.down = down;
	}
	private String status;
	public String getIMEI()
	{
		return IMEI;
	}
	public void setIMEI(String iMEI)
	{
		IMEI = iMEI;
	}
	public String getIP()
	{
		return IP;
	}
	public void setIP(String iP)
	{
		IP = iP;
	}
	public String getUp()
	{
		if(up<1024)
		{
			return up+"bytes";
		}
		else if(up<1024*1024)
		{
			return up/1024+"kb";
		}
		else if(up <1024*1024*1024)
		{
			return up/(1024*1024)+"MB";
		}
		else if(up<1024*1024*1024*1024)
		{
			return up/(1024*1024*1024)+"GB";
		}
		else {
			return "1TB";
		}
	}
	public void setUp(double up)
	{
		this.up = up;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((IMEI == null) ? 0 : IMEI.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DTUBean other = (DTUBean) obj;
		if (IMEI == null)
		{
			if (other.IMEI != null)
				return false;
		}
		else if (!IMEI.equals(other.IMEI))
			return false;
		return true;
	}
	

}
