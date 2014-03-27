/*
 * BaseData
 *
 * Version 1.0
 *
 * 2014-03-25
 *
 * Copyright notice
 */
package com.jason.diner;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseData {}

/**
 * fragment tag 枚举
 * @author Jason
 *
 */
enum FRAGMENT_TAG{
	GUIDE, SEARCH, MAIN, SHOP, RULE, SHOW, SETTING, ABOUT
}



/**
 * 服务器信息类
 * @author Jason
 *
 */
class ServerInfo{
	
	//服务器URL
	public String url;
	
	//请求参数，保存上一次的参数，防止重复请求
	public String paramSearch;
	public String paramShop;
	public String paramRule;
	public String paramOrder;
	
	//搜索关键字，保存上一次的关键字，防止重复搜索
	public String prompt;
	
	//相关页面地址
	private String searchAddress;
	private String shopAddress;
	private String conditionAddress;
	private String orderAddress;
	
	ServerInfo(){
		url = "http://mqstreetball.51a.net222-2.net";
		searchAddress = "/search.php";
		shopAddress = "/selectedShop.php";
		conditionAddress = "/getRule.php";
		orderAddress = "/getList.php";
		
		paramSearch = null;
		paramShop = null;
		paramRule = null;
		paramOrder = null;
	}
	
	/**
	 * 重置保存的参数，网络请求异常时执行
	 */
	public void clearParam(){
		paramSearch = null;
		paramShop = null;
		paramRule = null;
		paramOrder = null;
	}
	
	/**
	 * 返回搜索URL
	 * @param url附加参数 String
	 * @return 完整的URL
	 */
	public String getSearchUrl(String param){
		String getUrl = url + searchAddress;
		if(param != null){
			getUrl += "?" + param;
		}
		return getUrl;
	}
	
	/**
	 * 返回餐馆URL
	 * @param url附加参数 String
	 * @return 完整的URL
	 */
	public String getShopUrl(String param){
		String getUrl = url + shopAddress;
		if(param != null){
			getUrl += "?" + param;
		}
		return getUrl;
	}
	
	/**
	 * 返回条件URL
	 * @param url附加参数 String
	 * @return 完整的URL
	 */
	public String getConditionUrl(String param){
		String getUrl = url + conditionAddress;
		if(param != null){
			getUrl += "?" + param;
		}
		return getUrl;
	}
	
	/**
	 * 返回菜单URL
	 * @param url附加参数 String
	 * @return 完整的URL
	 */
	public String getOrderUrl(String param){
		String getUrl = url + orderAddress;
		if(param != null){
			getUrl += "?" + param;
		}
		return getUrl;
	}
	
}

/**
 * 搜索结果信息类
 * @author Jason
 *
 */
class SearchInfo{
	//搜索结果列表
	public ArrayList<ShopInfo> searchList;
	//用于绑定控件的搜索结果列表
	public ArrayList<HashMap<String, Object>> searchListBlinding;
	
	SearchInfo(){
		searchList = new ArrayList<ShopInfo>();
		searchListBlinding = new ArrayList<HashMap<String, Object>>();
	}
	
	public void clear(){
		searchList.clear();
		searchListBlinding.clear();
	}
}

/**
 * 餐馆信息类
 * @author Jason
 *
 */
class ShopInfo{
	//餐馆相关信息
	public String shopId;
	public String shopImage;
	public String shopName;
	public String shopAddress;
	public String shopIntroduce;
	
	/** 餐馆推荐菜列表 */
	public ArrayList<DishInfo> topList;
	
	/** 餐馆推荐菜绑定列表 */
	public ArrayList<HashMap<String, Object>> topListBlinding;
	
	/** 选择的推荐菜列表 */
	public HashMap<String, Boolean> selectedTopList;
	

	/**
	 * 餐馆信息类 对应的 参数名称类
	 * @author Jason
	 *
	 */
	public static final class KEYS{
		public static final String SHOP_ID = "shopId";
		public static final String SHOP_IMAGE = "shopImage";
		public static final String SHOP_NAME = "shopName";
		public static final String SHOP_ADDRESS = "shopAddress";
		public static final String SHOP_INTRODUCE = "shopIntroduce";
		public static final String TOP_LIST = "topList";
		public static final String TOP_LIST_BLINDING = "topListBlinding";
		public static final String SELECTED_TOP_LIST = "selectedTopList";
	}
	
	
	ShopInfo(){
		shopId = null;
		shopImage = null;
		shopName = null;
		shopAddress = null;
		shopIntroduce = null;
		topList = new ArrayList<DishInfo>();
		topListBlinding = new ArrayList<HashMap<String, Object>>();
		selectedTopList = new HashMap<String, Boolean>();
		
	}

	public void clear(){
		shopId = null;
		shopImage = null;
		shopName = null;
		shopAddress = null;
		shopIntroduce = null;
		topList.clear();
		topListBlinding.clear();
		selectedTopList.clear();
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		
		String s = shopId + shopImage + shopName + shopAddress + shopIntroduce;
		for(DishInfo item : topList){
			s += item.toString();
		}
		return s;
	}
	
	
}



/**
 * 菜品信息类
 * @author Jason
 *
 */
class DishInfo{
	//菜品相关信息
	public String dishId;
	public String dishImage;
	public String dishName;
	public String dishTaste;		//口味：酸甜苦辣
	public String dishFood;			//主材：猪肉，豆腐，面粉，鸡肉
	public String dishCooking;
	public String dishCategory;		//类别：荤素汤
	
	/**
	 * 菜品信息类 对应的 参数名称类
	 * @author Jason
	 *
	 */
	public static final class KEYS{
		public static final String DISH_ID= "dishId";
		public static final String DISH_IMAGE = "dishImage";
		public static final String DISH_NAME = "dishName";
		public static final String DISH_TASTE = "dishTaste";		//口味：酸甜苦辣
		public static final String DISH_FOOD = "dishFood";		//主材：猪肉，豆腐，面粉，鸡肉
		public static final String DISH_COOKING = "dishCooking";
		public static final String DISH_CATEGORY = "dishCategory";		//类别：荤素汤
		public static final String SELECTED = "selected";
	}
	
	DishInfo(){
		dishId =null;
		dishImage = null;
		dishName = null;
		dishTaste = null;
		dishFood = null;
		dishCooking = null;
		dishCategory = null;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return dishId + dishImage + dishName + dishTaste + dishFood +
				dishCooking + dishCategory;
		
	}

}



/**
 * 点菜条件信息类（记录从服务器得到的所有规则）
 * @author Jason
 *
 */
class ConditionInfo{
	/** 条件对象 */
	public HashMap<String, ArrayList<String>> conditions;
	
	public void clear(){
		conditions.clear();
	}
	
	ConditionInfo(){
		conditions = new HashMap<String, ArrayList<String>>();
	}
	
}

/**
 * 点菜规则信息类（用户选择的所有规则）
 * @author Jason
 *
 */
class RuleInfo{
	//规则相关信息
	public String shopId;
	public ArrayList<String> staredDishList;
	public ArrayList<String> categoryList;
	public HashMap<String, ArrayList<Boolean>> conditionList;
	
	/**
	 * 点菜规则信息类 对应的 参数名称类
	 * @author Jason
	 *
	 */
	public static final class KEYS{
		public static final String SHOP_ID = "shopId";
		public static final String STARED_DISH_LIST = "staredDishList";
		public static final String CATEGORY_LIST= "categoryList";
		public static final String CONDITION_LIST = "conditionList";
	}
	
	RuleInfo(){
		shopId = null;
		staredDishList = new ArrayList<String>();
		categoryList = new ArrayList<String>();
		conditionList = new HashMap<String, ArrayList<Boolean>>();
	}
	
	public void clear(){
		shopId = null;
		staredDishList.clear();
		categoryList.clear();
		conditionList.clear();
	}
}



/**
 * 菜单信息类
 * @author Jason
 *
 */
class OrderInfo{
	//菜单信息
	public ConditionInfo ruleInfo;
	
	/** 请求的菜单 */
	public HashMap<String, ArrayList<ArrayList<DishInfo>>> dishes;
	
	/** 每个分组数量 */
	public HashMap<String, Integer> categoryCount;
	
	/** 菜单绑定 */
	public ArrayList<ArrayList<DishInfo>> dishesBlinding;
	
	OrderInfo(){
		ruleInfo = new ConditionInfo();
		dishes = new HashMap<String, ArrayList<ArrayList<DishInfo>>>();
		categoryCount = new HashMap<String, Integer>();
		dishesBlinding = new ArrayList<ArrayList<DishInfo>>();
	}
	
	public void clear(){
		ruleInfo.clear();
		dishes.clear();
		categoryCount.clear();
		dishesBlinding.clear();
	}
}
