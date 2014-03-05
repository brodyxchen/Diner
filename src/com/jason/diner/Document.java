package com.jason.diner;

import java.util.ArrayList;
import java.util.HashMap;

import com.jason.Data.ImageCache;

public class Document {
	private static Document mainDoc = null;
	public MainActivity mainActivity = null;
	
	
	public int screenWidth;
	public ImageCache imageCache;
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
		server = new ServerInfo();
		
		search = new SearchInfo();
		shop = new ShopInfo();
		rule = new RuleInfo();
		condition = new ConditionInfo();
		order = new OrderInfo();

	}
}
