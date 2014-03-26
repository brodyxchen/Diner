/*
 * Document
 *
 * Version 1.0
 *
 * 2014-03-25
 *
 * Copyright notice
 */
package com.jason.diner;

import android.support.v4.app.Fragment;

import com.jason.Data.ImageCache;

/**
 * 数据类
 * @author Jason
 *
 */
public class Document {
	
	private static Document mainDoc = null;		//指向自己静态对象的引用（方便其他页面使用）
	public MainActivity mainActivity = null;	//指向MainActivity的引用
	
	public int screenWidth;				//屏幕宽度（ViewPager会用到）
	public ImageCache imageCache;		//图片缓存
	public Fragment currentFragment;	//当前显示的fragment
	
	public ServerInfo server;			//服务器信息
	public SearchInfo search;			//搜索信息
	public ShopInfo shop;				//餐馆信息
	public ConditionInfo condition;		//条件信息（记录需要从服务器得到的所有规则）
	public RuleInfo rule;				//规则信息（用户选择的规则）
	public OrderInfo order;				//菜单信息
	
	/**
	 * 返回 唯一的静态本身对象
	 * @return
	 */
	public static Document MainDoc(){
		return mainDoc;
	}
	
	
	public Document(MainActivity act){
		mainDoc = this;
		mainActivity = act;

		screenWidth = 800;
		imageCache = new ImageCache();
		currentFragment = null;
		
		server = new ServerInfo();
		search = new SearchInfo();
		shop = new ShopInfo();
		rule = new RuleInfo();
		condition = new ConditionInfo();
		order = new OrderInfo();

	}
}
