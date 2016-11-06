package com.songxu.interfaces;

public interface Configuration
{

	/**
	 * 设置配置文件路径
	 * @param path
	 */
	public void setBuildPath(String path);
	/**
	 * 根据配置文件构建
	 */
	public void build();

}
