package com.jason.Data;

import java.util.HashMap;

import android.graphics.Bitmap;

public class ImageCache {
	public HashMap<String, Bitmap> images;
	public ImageCache(){
		images = new HashMap<String, Bitmap>();
	}
	
	public Bitmap getImage(String key){
		return images.get(key);
	}
	
	public void putImage(String key, Bitmap value){
		images.put(key, value);
	}
}
