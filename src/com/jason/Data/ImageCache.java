/*
 * ImageCache
 *
 * Version 1.0
 *
 * 2014-03-25
 *
 * Copyright notice
 */

package com.jason.Data;

import java.util.HashMap;

import android.graphics.Bitmap;

/**
 * Í¼Æ¬»º´æÀà
 * 
 * @author Jason
 *
 */
public class ImageCache {
	
	/** ´æ´¢Í¼Æ¬µÄÊý¾Ý½á¹¹ */
	private HashMap<String, Bitmap> images;
	
	public ImageCache(){
		images = new HashMap<String, Bitmap>();
	}
	
	/**
	 * È¡µÃÍ¼Æ¬
	 * @param key String Í¼Æ¬¹Ø¼ü×Ö
	 * @return Bitmap Í¼Æ¬
	 */
	public Bitmap getImage(String key){
		return images.get(key);
	}
	
	/**
	 * ´æÈëÍ¼Æ¬
	 * @param key String Í¼Æ¬¹Ø¼ü×Ö
	 * @param value Bitmap Í¼Æ¬
	 */
	public void putImage(String key, Bitmap value){
		images.put(key, value);
	}
}
