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
import android.util.Log;

public class Helper {

	private Helper() {

	}

	public static boolean isUrl(String pInput) {
		if (pInput == null) {
			return false;
		}
		String regEx = "^(http|https|ftp)//://([a-zA-Z0-9//.//-]+(//:[a-zA-"
				+ "Z0-9//.&%//$//-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{"
				+ "2}|[1-9]{1}[0-9]{1}|[1-9])//.(25[0-5]|2[0-4][0-9]|[0-1]{1}"
				+ "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)//.(25[0-5]|2[0-4][0-9]|"
				+ "[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)//.(25[0-5]|2[0-"
				+ "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0"
				+ "-9//-]+//.)*[a-zA-Z0-9//-]+//.[a-zA-Z]{2,4})(//:[0-9]+)?(/"
				+ "[^/][a-zA-Z0-9//.//,//?//'///////+&%//$//=~_//-@]*)*$";
		Pattern p = Pattern.compile(regEx);
		Matcher matcher = p.matcher(pInput);
		return matcher.matches();
	}

	public static Bitmap Drawable2Bitmap(int drawable) {
		Bitmap bmp = BitmapFactory.decodeResource(
				Document.MainDoc().mainActivity.resources, drawable);

		return bmp;
	}

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

	public static boolean json2Search(String json, SearchInfo search) {
		if (json == null)
			return false;

		search.clear();
		ArrayList<ShopInfo> searchList = search.searchList;
		try {
			JSONArray arr = new JSONArray(json);
			for (int i = 0; i < arr.length(); i++) {
				JSONObject temp = (JSONObject) arr.get(i);

				String shopId = temp.getString(ShopInfoKey.shopId);
				String shopImage = temp.getString(ShopInfoKey.shopImage);
				String shopName = temp.getString(ShopInfoKey.shopName);
				String shopAddress = temp.getString(ShopInfoKey.shopAddress);
				String shopIntroduce = temp
						.getString(ShopInfoKey.shopIntroduce);

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
			Test.error("Helper.json2Search()", e.toString());
			return false;
		}

	}

	public static boolean json2Shop(String json, ShopInfo shop) {
		if (json == null)
			return false;

		try {
			shop.clear();
			JSONObject jsonShopInfo = new JSONObject(json);
			shop.shopId = jsonShopInfo.getString(ShopInfoKey.shopId);
			shop.shopImage = jsonShopInfo.getString(ShopInfoKey.shopImage);
			shop.shopName = jsonShopInfo.getString(ShopInfoKey.shopName);
			shop.shopAddress = jsonShopInfo.getString(ShopInfoKey.shopAddress);
			shop.shopIntroduce = jsonShopInfo
					.getString(ShopInfoKey.shopIntroduce);

			JSONArray jsonShopTopList = jsonShopInfo
					.getJSONArray(ShopInfoKey.topList);

			
			
			for (int i = 0; i < jsonShopTopList.length(); i++) {
				JSONObject jsonDish = (JSONObject) jsonShopTopList.get(i);
				DishInfo dishInfo = new DishInfo();
				dishInfo.dishId = jsonDish.getString(DishInfoKey.dishId);
				dishInfo.dishImage = jsonDish.getString(DishInfoKey.dishImage);
				dishInfo.dishName = jsonDish.getString(DishInfoKey.dishName);
				dishInfo.dishTaste = jsonDish.getString(DishInfoKey.dishTaste);
				dishInfo.dishFood = jsonDish.getString(DishInfoKey.dishFood);
				dishInfo.dishCooking = jsonDish.getString(DishInfoKey.dishCooking);
				dishInfo.dishCategory = jsonDish
						.getString(DishInfoKey.dishCategory);
				shop.topList.add(dishInfo);
			}

			return true;

		} catch (Exception e) {
			shop.clear();
			Test.error("Helper.json2Shop()", e.toString());
			return false;
		}
	}

	public static boolean json2Order(String json, OrderInfo order) {
		if (json == null)
			return false;

		order.clear();
		HashMap<String, ArrayList<ArrayList<DishInfo>>> dishes = order.dishes;
		try {
			JSONObject jsonDishes = new JSONObject(json);
			Iterator<String> iterator = jsonDishes.keys();
			String key = null;
			JSONArray value = null;
			while (iterator.hasNext()) {
				key = iterator.next();
				value = (JSONArray) jsonDishes.get(key);
				
				ArrayList<ArrayList<DishInfo>> categoryes = new ArrayList<ArrayList<DishInfo>>();
				for(int i = 0; i < value.length(); i++){
					JSONArray jsonCandidate = value.getJSONArray(i);
					ArrayList<DishInfo> candidate = new ArrayList<DishInfo>();
					for(int j = 0; j < jsonCandidate.length(); j++){
						JSONObject jsonDish = jsonCandidate.getJSONObject(j);
						
						DishInfo dish = new DishInfo();
						dish.dishId = jsonDish.getString(DishInfoKey.dishId);
						dish.dishImage = jsonDish.getString(DishInfoKey.dishImage);
						dish.dishName = jsonDish.getString(DishInfoKey.dishName);
						dish.dishFood = jsonDish.getString(DishInfoKey.dishFood);
						dish.dishTaste = jsonDish.getString(DishInfoKey.dishTaste);
						dish.dishCooking = jsonDish.getString(DishInfoKey.dishCooking);
						dish.dishCategory = jsonDish.getString(DishInfoKey.dishCategory);
						candidate.add(dish);
					}
					categoryes.add(candidate);
				}
				dishes.put(key, categoryes);

			}
			
			return true;

		} catch (Exception e) {
			order.clear();
			Test.error("Helper.json2Order()", e.toString());
			return false;
		}

	}

	public static boolean json2Condition(String json, ConditionInfo condition) {
		if (json == null)
			return false;

		condition.clear();
		try {
			JSONObject conditonObject = new JSONObject(json);
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
			Test.error("Helper.json2Condition()", e.toString());
			return false;
		}

	}

	public static String rule2Json(RuleInfo rule) {
		if(rule == null)
			return null;

		try {
			JSONObject jsonObject = new JSONObject();

			jsonObject.put(RuleInfoKey.shopId, rule.shopId);
			JSONArray staredJsonArray = new JSONArray();
			for (int i = 0; i < rule.staredDishList.size(); i++) {
				staredJsonArray.put(rule.staredDishList.get(i));
			}
			jsonObject.put(RuleInfoKey.staredDishList, staredJsonArray);

			JSONArray categoryJsonArray = new JSONArray();
			for (int i = 0; i < rule.categoryList.size(); i++) {
				categoryJsonArray.put(rule.categoryList.get(i));
			}
			jsonObject.put(RuleInfoKey.categoryList, categoryJsonArray);

			JSONObject conditionObject = new JSONObject();
			Iterator iter = rule.conditionList.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry<String, ArrayList<Boolean>> entry = (Map.Entry<String, ArrayList<Boolean>>) iter
						.next();
				JSONArray conditionArray = new JSONArray();
				String key = entry.getKey();
				ArrayList<Boolean> value = entry.getValue();
				for (int i = 0; i < value.size(); i++) {
					conditionArray.put(value.get(i));
				}
				conditionObject.put(key, conditionArray);
			}
			jsonObject.put(RuleInfoKey.conditionList, conditionObject);

			return jsonObject.toString();

		} catch (Exception e) {
			Test.error("Helper.rule2Json()", e.toString());
			return null;
		}

	}

}
