/*
 * IUpdate
 *
 * Version 1.0
 *
 * 2014-03-25
 *
 * Copyright notice
 */
package com.jason.Interface;

/**
 * 更新接口
 * @author Jason
 *
 */
public interface IUpdate {
	
	/** 更新数据 */
	public void updateData(String json);
	
	/** 更新界面 */
	public void updateUI();
	
	/** 请求数据 */
	public void updateHttp();
	
}
