package com.songxu.interfaces;

import java.io.Serializable;


/**
 * 消息类接口
 * 在系统内部 信息以Message形式流转
 * @author songxu
 *
 */
public interface Message extends Serializable
{
	/**
	 * 包装消息
	 * @param receive
	 * @return
	 */
	public Message wrapMessage(String receive);
	/**
	 * 返回消息的字符串
	 * @return
	 */
	public String  getString();
	/**
	 * 获得消息状态码
	 * @return
	 */
	public String  getStatus();
	/**
	 * 获得消息来源
	 * @return
	 */
	public String  getIMEI_F();
	/**
	 * 获得消息去向
	 * @return
	 */
	public String  getIMEI_T();
	/**
	 * 获得消息内容
	 * @return
	 */
	public String  getMsg();
	/**
	 * 获取消息校验码
	 * @return
	 */
	public String  getCheck();
	/**
	 * 检验数据是否合法
	 * @return
	 */
	public boolean checkDataValible();

}
