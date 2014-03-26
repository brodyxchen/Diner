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
 * Êý¾ÝÀà
 * @author Jason
 *
 */
public class Document {
	private static Document mainDoc = null;
	public MainActivity mainActivity = null;
	
	public int screenWidth;
	public ImageCache imageCache;
	public Fragment currentFragment;
	
	public ServerInfo server;
	public SearchInfo search;
	public ShopInfo shop;
	public RuleInfo rule;
	public ConditionInfo condition;
	public OrderInfo order;
	
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
