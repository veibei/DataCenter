package com.songxu.interfaces;


/**
 * 2015-10-21 移动到interfaces包下面  为了反序列化的需求
 * @author songxu
 *
 */
public class MessageImpl implements Message
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6362385614438035455L;
	private String status;
	private String IMEI_F;
	private String IMEI_T;
	private String msg;
	public MessageImpl()
	{
		status="";
		IMEI_F="";
		IMEI_T="";
		msg="";
	}
	
	public MessageImpl(String status, String iMEI_F, String iMEI_T, String msg)
	{
		super();
		this.status = status;
		IMEI_F = iMEI_F;
		IMEI_T = iMEI_T;
		this.msg = msg;
	}

	@Override
	/**
	 * 2015-10-10修改 replace函数
	 */
	public Message wrapMessage(String receive)
	{
		if(receive.length()<34)
		{
			return null;
		}
		else {
			receive=receive.replaceAll("/r|/n", "").trim();
			status=receive.substring(0,2);
			IMEI_F=receive.substring(2, 18);
			IMEI_T=receive.substring(18,34);
			if(receive.length()==34)
			{
				msg="";
			}
			else {
				msg=receive.substring(34);
				/*//最后两位是/r/n
				try
				{
					msg=receive.substring(34,receive.length()-2);
				}
				catch (Exception e)
				{
					msg=receive.substring(34,receive.length()-1);
				}*/
				
			}
			return this;
		}
		
	}

	@Override
	public String getString()
	{
		StringBuilder builder=new StringBuilder();
		builder.append(status);
		builder.append(IMEI_F);
		builder.append(IMEI_T);
		builder.append(msg);
		return builder.toString();
	}

	public String getStatus()
	{
		return status;
	}

	public String getIMEI_F()
	{
		return IMEI_F;
	}

	public String getIMEI_T()
	{
		return IMEI_T;
	}

	public String getMsg()
	{
		return msg;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((IMEI_F == null) ? 0 : IMEI_F.hashCode());
		result = prime * result + ((IMEI_T == null) ? 0 : IMEI_T.hashCode());
		result = prime * result + ((msg == null) ? 0 : msg.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		MessageImpl other = (MessageImpl) obj;
		if (IMEI_F == null)
		{
			if (other.IMEI_F != null)
				return false;
		}
		else if (!IMEI_F.equals(other.IMEI_F))
			return false;
		if (IMEI_T == null)
		{
			if (other.IMEI_T != null)
				return false;
		}
		else if (!IMEI_T.equals(other.IMEI_T))
			return false;
		if (msg == null)
		{
			if (other.msg != null)
				return false;
		}
		else if (!msg.equals(other.msg))
			return false;
		if (status == null)
		{
			if (other.status != null)
				return false;
		}
		else if (!status.equals(other.status))
			return false;
		return true;
	}

	@Override
	public String getCheck()
	{
		return getIMEI_F().substring(12);
	}

	@Override
	public boolean checkDataValible()
	{
		String status = getStatus();
		String status_mark = status.substring(0, 1);
		// 状态码必须为R D C 0开头
		if (!(status_mark.equals("R") || status_mark.equals("D") || status_mark.equals("C") || status_mark
				.equals("0")))
		{
			return false;
		}
		else
		{
			// imei 必须为数字
			return true;/* (getIMEI_F().matches("[0-9]+")
					&& getIMEI_T().matches("[0-9]+"));
*/
		}
	}

}
