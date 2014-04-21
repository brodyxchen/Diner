/*
 * Helper
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
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * 辅助类（Json转换，图片转换，圆角图片处理，合法URL检测等）
 * @author Jason
 *
 */
public class Helper {

	private Helper() {}

	
	/**
	 * 图片转换
	 * @param drawable 图片
	 * @return 图片资源
	 */
	public static Bitmap Drawable2Bitmap(int drawable) {
		Bitmap bmp = BitmapFactory.decodeResource(
				Document.MainDoc().mainActivity.resources, drawable);

		return bmp;
	}

	/**
	 * 圆角图片处理
	 * @param bitmap 原始图片
	 * @return 圆角图片
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap) {
		int pixels = 70;
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * Jason转SearchInfo
	 * @param json jason字符串
	 * @param search searchInfo对象
	 * @return	是否成功
	 */
	public static boolean json2Search(String json, SearchInfo search) {
		if (json == null){
			Document.MainDoc().error = null;
			return false;
		}

		search.clear();
		ArrayList<ShopInfo> searchList = search.searchList;
		try {
			JSONObject tempJson = new JSONObject(json);
			Iterator<String> tempIter = tempJson.keys();
			String tempKey = tempIter.next();
			
			if(tempKey.trim().equals("error")){
				Document.MainDoc().error = "error";
				return false;
			}
			JSONArray arr = (JSONArray) tempJson.get(tempKey);

			for (int i = 0; i < arr.length(); i++) {
				JSONObject temp = (JSONObject) arr.get(i);

				String shopId = temp.getString(ShopInfo.KEYS.SHOP_ID);
				String shopImage = temp.getString(ShopInfo.KEYS.SHOP_IMAGE);
				String shopName = temp.getString(ShopInfo.KEYS.SHOP_NAME);
				String shopAddress = temp.getString(ShopInfo.KEYS.SHOP_ADDRESS);
				String shopIntroduce = temp
						.getString(ShopInfo.KEYS.SHOP_INTRODUCE);

				ShopInfo shopInfo = new ShopInfo();
				shopInfo.shopId = shopId;
				shopInfo.shopImage = shopImage;
				shopInfo.shopName = shopName;
				shopInfo.shopAddress = shopAddress;
				shopInfo.shopIntroduce = shopIntroduce;

				searchList.add(shopInfo);
			}
			return true;
		} catch (Exception e) {
			search.clear();
			Document.MainDoc().error = null;
			return false;
		}

	}

	/**
	 * Jason转ShopInfo
	 * @param json jason字符串
	 * @param shop ShopInfo对象
	 * @return	是否成功
	 */
	public static boolean json2Shop(String json, ShopInfo shop) {
		if (json == null){
			Document.MainDoc().error = null;
			return false;
		}

		try {
			shop.clear();
			
			JSONObject tempJson = new JSONObject(json);
			Iterator<String> tempIter = tempJson.keys();
			String tempKey = tempIter.next();
			if(tempKey.trim().equals("error")){
				Document.MainDoc().error = "error";
				return false;
			}
			JSONObject jsonShopInfo = (JSONObject) tempJson.get(tempKey);
			
			shop.shopId = jsonShopInfo.getString(ShopInfo.KEYS.SHOP_ID);
			shop.shopImage = jsonShopInfo.getString(ShopInfo.KEYS.SHOP_IMAGE);
			shop.shopName = jsonShopInfo.getString(ShopInfo.KEYS.SHOP_NAME);
			shop.shopAddress = 
					jsonShopInfo.getString(ShopInfo.KEYS.SHOP_ADDRESS);
			shop.shopIntroduce = jsonShopInfo
					.getString(ShopInfo.KEYS.SHOP_INTRODUCE);

			JSONArray jsonShopTopList = jsonShopInfo
					.getJSONArray(ShopInfo.KEYS.TOP_LIST);

			
			
			for (int i = 0; i < jsonShopTopList.length(); i++) {
				JSONObject jsonDish = (JSONObject) jsonShopTopList.get(i);
				DishInfo dishInfo = new DishInfo();
				dishInfo.dishId = 
						jsonDish.getString(DishInfo.KEYS.DISH_ID);
				dishInfo.dishImage = 
						jsonDish.getString(DishInfo.KEYS.DISH_IMAGE);
				dishInfo.dishName = 
						jsonDish.getString(DishInfo.KEYS.DISH_NAME);
				dishInfo.dishTaste = 
						jsonDish.getString(DishInfo.KEYS.DISH_TASTE);
				dishInfo.dishFood = 
						jsonDish.getString(DishInfo.KEYS.DISH_FOOD);
				dishInfo.dishCooking = 
						jsonDish.getString(DishInfo.KEYS.DISH_COOKING);
				dishInfo.dishCategory = 
						jsonDish.getString(DishInfo.KEYS.DISH_CATEGORY);
				shop.topList.add(dishInfo);
			}

			return true;

		} catch (Exception e) {
			shop.clear();
			Document.MainDoc().error = null;
			return false;
		}
	}

	/**
	 * Jason转OrderInfo
	 * @param json jason字符串
	 * @param order OrderInfo对象
	 * @return	是否成功
	 */
	public static boolean json2Order(String json, OrderInfo order) {
		if (json == null){
			Document.MainDoc().error = null;
			return false;
		}

		order.clear();
		HashMap<String, ArrayList<ArrayList<DishInfo>>> dishes = order.dishes;
		try {
			
			JSONObject tempJson = new JSONObject(json);
			Iterator<String> tempIter = tempJson.keys();
			String tempKey = tempIter.next();
			if(tempKey.trim().equals("error")){
				Document.MainDoc().error = "error";
				return false;
			}
			JSONObject jsonDishes = (JSONObject) tempJson.get(tempKey);
			
			Iterator<String> iterator = jsonDishes.keys();
			String key = null;
			JSONArray value = null;
			while (iterator.hasNext()) {
				key = iterator.next();
				value = (JSONArray) jsonDishes.get(key);
				
				ArrayList<ArrayList<DishInfo>> categoryes = 
						new ArrayList<ArrayList<DishInfo>>();
				for(int i = 0; i < value.length(); i++){
					JSONArray jsonCandidate = value.getJSONArray(i);
					ArrayList<DishInfo> candidate = new ArrayList<DishInfo>();
					for(int j = 0; j < jsonCandidate.length(); j++){
						JSONObject jsonDish = jsonCandidate.getJSONObject(j);
						
						DishInfo dish = new DishInfo();
						dish.dishId = 
								jsonDish.getString(DishInfo.KEYS.DISH_ID);
						dish.dishImage = 
								jsonDish.getString(DishInfo.KEYS.DISH_IMAGE);
						dish.dishName = 
								jsonDish.getString(DishInfo.KEYS.DISH_NAME);
						dish.dishFood = 
								jsonDish.getString(DishInfo.KEYS.DISH_FOOD);
						dish.dishTaste = 
								jsonDish.getString(DishInfo.KEYS.DISH_TASTE);
						dish.dishCooking = 
								jsonDish.getString(DishInfo.KEYS.DISH_COOKING);
						dish.dishCategory = 
								jsonDish.getString(DishInfo.KEYS.DISH_CATEGORY);
						candidate.add(dish);
					}
					categoryes.add(candidate);
				}
				dishes.put(key, categoryes);

			}
			
			return true;

		} catch (Exception e) {
			order.clear();
			Document.MainDoc().error = null;
			return false;
		}

	}

	/**
	 * Jason转ConditionInfo
	 * @param json jason字符串
	 * @param condition ConditionInfo对象
	 * @return	是否成功
	 */
	public static boolean json2Condition(String json, ConditionInfo condition) {
		if (json == null){
			Document.MainDoc().error = null;
			return false;
		}

		condition.clear();
		try {
			
			JSONObject tempJson = new JSONObject(json);
			Iterator<String> tempIter = tempJson.keys();
			String tempKey = tempIter.next();
			if(tempKey.trim().equals("error")){
				Document.MainDoc().error = "error";
				return false;
			}
			JSONObject conditonObject = (JSONObject) tempJson.get(tempKey);

			Iterator<String> iterator = conditonObject.keys();
			String key = null;
			JSONArray value = null;
			while (iterator.hasNext()) {
				key = iterator.next();
				value = (JSONArray) conditonObject.get(key);
				ArrayList<String> value2 = new ArrayList<String>();
				for (int i = 0; i < value.length(); i++) {
					value2.add(value.getString(i));
				}
				condition.conditions.put(key, value2);
			}
			return true;
		} catch (Exception e) {
			condition.clear();
			Document.MainDoc().error = null;
			return false;
		}

	}

	/**
	 * RuleInfo转Jason
	 * @param rule RuleInfo对象
	 * @return jason
	 */
	public static String rule2Json(RuleInfo rule) {
		if(rule == null){
			return null;
		}

		try {
			JSONObject jsonObject = new JSONObject();

			jsonObject.put(RuleInfo.KEYS.SHOP_ID, rule.shopId);
			JSONArray staredJsonArray = new JSONArray();
			for (int i = 0; i < rule.staredDishList.size(); i++) {
				staredJsonArray.put(rule.staredDishList.get(i));
			}
			jsonObject.put(RuleInfo.KEYS.STARED_DISH_LIST, staredJsonArray);

			JSONArray categoryJsonArray = new JSONArray();
			for (int i = 0; i < rule.categoryList.size(); i++) {
				categoryJsonArray.put(rule.categoryList.get(i));
			}
			jsonObject.put(RuleInfo.KEYS.CATEGORY_LIST, categoryJsonArray);

			JSONObject conditionObject = new JSONObject();
			Iterator iter = rule.conditionList.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, ArrayList<Boolean>> entry = 
						(Map.Entry<String, ArrayList<Boolean>>) iter.next();
				JSONArray conditionArray = new JSONArray();
				String key = entry.getKey();
				ArrayList<Boolean> value = entry.getValue();
				for (int i = 0; i < value.size(); i++) {
					conditionArray.put(value.get(i));
				}
				conditionObject.put(key, conditionArray);
			}
			jsonObject.put(RuleInfo.KEYS.CONDITION_LIST, conditionObject);

			return jsonObject.toString();

		} catch (Exception e) {
			return null;
		}

	}

	
	/**
	 * ArrayList<DishInfo>转Json
	 * @param list ArrayList<DishInfo>对象
	 * @return jason
	 */
	public static String selectOrder2Json(ArrayList<DishInfo> list) {
		if(list == null){
			return null;
		}

		try {
			
			JSONArray jsonArray = new JSONArray();
			for(int i = 0; i < list.size(); i++){
				DishInfo aDish = list.get(i);
				
				JSONObject jsonObject = new JSONObject();
				
				jsonObject.put(DishInfo.KEYS.DISH_CATEGORY, aDish.dishCategory);
				jsonObject.put(DishInfo.KEYS.DISH_COOKING, aDish.dishCooking);
				jsonObject.put(DishInfo.KEYS.DISH_FOOD, aDish.dishFood);
				jsonObject.put(DishInfo.KEYS.DISH_ID, aDish.dishId);
				jsonObject.put(DishInfo.KEYS.DISH_IMAGE, aDish.dishImage);
				jsonObject.put(DishInfo.KEYS.DISH_NAME, aDish.dishName);
				jsonObject.put(DishInfo.KEYS.DISH_TASTE, aDish.dishTaste);
				
				jsonArray.put(jsonObject);
			}
			
			return jsonArray.toString();


		} catch (Exception e) {
			return null;
		}

	}

}
