package com.jason.diner;

import java.util.ArrayList;
import java.util.HashMap;

import com.jason.diner.RuleFragment.ViewPair;

public class BaseData {

	
}

class ServerInfo{
	public String url;
	public String searchAddress;
	public String shopAddress;
	public String conditionAddress;
	public String orderAddress;
	
	public String prompt;
	ServerInfo(){
		url = "http://172.20.143.97/myorder";
		searchAddress = "/search.php";
		shopAddress = "/selectedShop.php";
		conditionAddress = "/getRule.php";
		orderAddress = "/getList.php";
	}
	
	public String getSearchUrl(String param){
		String getUrl = url + searchAddress;
		if(param != null){
			getUrl += "?" + param;
		}
		return getUrl;
	}
	public String getShopUrl(String param){
		String getUrl = url + shopAddress;
		if(param != null){
			getUrl += "?" + param;
		}
		return getUrl;
	}
	public String getConditionUrl(String param){
		String getUrl = url + conditionAddress;
		if(param != null){
			getUrl += "?" + param;
		}
		return getUrl;
	}
	public String getOrderUrl(String param){
		String getUrl = url + orderAddress;
		if(param != null){
			getUrl += "?" + param;
		}
		return getUrl;
	}
	
}

class SearchInfo{
	public ArrayList<ShopInfo> searchList;
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

class ShopInfo{
	public String shopId;
	public String shopImage;
	public String shopName;
	public String shopAddress;
	public String shopIntroduce;
	
	public ArrayList<DishInfo> topList;
	public ArrayList<HashMap<String, Object>> topListBlinding;
	public HashMap<String, Boolean> selectedTopList;
	
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

class ShopInfoKey{
	public static String shopId = "shopId";
	public static String shopImage = "shopImage";
	public static String shopName = "shopName";
	public static String shopAddress = "shopAddress";
	public static String shopIntroduce = "shopIntroduce";
	public static String topList = "topList";
	public static String topListBlinding = "topListBlinding";
	public static String selectedTopList = "selectedTopList";
}



class DishInfo{
	public String dishId;
	public String dishImage;
	public String dishName;
	public String dishTaste;		//¿ÚÎ¶£ºËáÌð¿àÀ±
	public String dishFood;			//Ö÷²Ä£ºÖíÈâ£¬¶¹¸¯£¬Ãæ·Û£¬¼¦Èâ
	public String dishCooking;
	public String dishCategory;		//Àà±ð£º»çËØÌÀ
	
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
		return dishId + dishImage + dishName + dishTaste + dishFood + dishCooking + dishCategory;
		
	}

}

class DishInfoKey{
	public static String dishId= "dishId";
	public static String dishImage = "dishImage";
	public static String dishName = "dishName";
	public static String dishTaste = "dishTaste";		//¿ÚÎ¶£ºËáÌð¿àÀ±
	public static String dishFood = "dishFood";		//Ö÷²Ä£ºÖíÈâ£¬¶¹¸¯£¬Ãæ·Û£¬¼¦Èâ
	public static String dishCooking = "dishCooking";
	public static String dishCategory = "dishCategory";		//Àà±ð£º»çËØÌÀ
	
	public static String selected = "selected";
}


class ConditionInfo{
	public HashMap<String, ArrayList<String>> conditions;
	
	public void clear(){
		conditions.clear();
	}
	
	ConditionInfo(){
		conditions = new HashMap<String, ArrayList<String>>();
	}
	
}

class RuleInfo{
	public String shopId;
	public ArrayList<String> staredDishList;
	public ArrayList<String> categoryList;
	public HashMap<String, ArrayList<Boolean>> conditionList;
	
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

class RuleInfoKey{
	public static String shopId = "shopId";
	public static String staredDishList = "staredDishList";
	public static String categoryList= "categoryList";
	public static String conditionList = "conditionList";
}


class OrderInfo{
	public ConditionInfo ruleInfo;
	public HashMap<String, ArrayList<ArrayList<DishInfo>>> dishes;
	public HashMap<String, Integer> categorySize;
	public ArrayList<ArrayList<DishInfo>> dishesBlinding;
	
	OrderInfo(){
		ruleInfo = new ConditionInfo();
		dishes = new HashMap<String, ArrayList<ArrayList<DishInfo>>>();
		categorySize = new HashMap<String, Integer>();
		dishesBlinding = new ArrayList<ArrayList<DishInfo>>();
	}
	
	public void clear(){
		ruleInfo.clear();
		dishes.clear();
		categorySize.clear();
		dishesBlinding.clear();
	}
}
