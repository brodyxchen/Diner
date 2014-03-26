/*
 * ImageLoadTask
 *
 * Version 1.0
 *
 * 2014-03-25
 *
 * Copyright notice
 */
package com.jason.Task;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.BaseAdapter;

import com.jason.Network.Httper;
import com.jason.diner.Document;

/**
 * 图片后台加载类
 * @author Jason
 *
 */
public class ImageLoadTask extends AsyncTask<Object, Void, Bitmap> {   
	
	/** 地址 */
    private String address;
    
    /** 适配器 */
    private BaseAdapter adapter;
    
    @Override  
    protected Bitmap doInBackground(Object... params) {  
        String url = (String) params[0];
        address = (String) params[1];
        adapter = (BaseAdapter) params[2];
        Bitmap drawable = Httper.loadImage(url + address);//获取网络图片  
        return drawable;  
    }  

    @Override  
    protected void onPostExecute(Bitmap result) {  
        if (result == null) { 
        	Document.MainDoc().imageCache.removeDownloading(address);
            return;  
        }  
        Document.MainDoc().imageCache.putImage(address, result);//放入缓存  
        adapter.notifyDataSetChanged();
    }

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
	} 
    
    
} 