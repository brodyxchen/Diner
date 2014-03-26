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
import java.util.HashSet;

import android.graphics.Bitmap;

/**
 * 图片缓存类
 * 
 * @author Jason
 *
 */
public class ImageCache {
	
	//存储图片的数据结构
	private HashMap<String, Bitmap> images;
	
	//存储已经开始下载了的图片连接（防止重复下载）
	private HashSet<String> isDownloading;
	
	public ImageCache(){
		images = new HashMap<String, Bitmap>();
		isDownloading = new HashSet<String>();
	}
	
	/**
	 * 取得图片
	 * @param key String 图片关键字
	 * @return Bitmap 图片
	 */
	public Bitmap getImage(String key){
		return images.get(key);
	}
	
	/**
	 * 存入图片
	 * @param key String 图片关键字
	 * @param value Bitmap 图片
	 */
	public void putImage(String key, Bitmap value){
		images.put(key, value);
	}
	
	/**
	 * 存入已经开始下载的图片连接
	 * @param key 图片URI
	 */
	public void putDownloading(String key){
		isDownloading.add(key);
	}
	
	/**
	 * 是否存入了此图片连接
	 * @param key 图片URI
	 * @return 此图片是否正在下载
	 */
	public boolean getDownloading(String key){
		return isDownloading.contains(key);
	}
	
	/**
	 * 删除图片连接（图片下载失败时）
	 * @param key 图片URI
	 */
	public void removeDownloading(String key){
		isDownloading.remove(key);
	}
}
