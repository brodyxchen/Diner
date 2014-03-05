package com.jason.Task;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.BaseAdapter;

import com.jason.Network.Httper;
import com.jason.diner.Document;
import com.jason.diner.Test;

public class ImageLoadTask extends AsyncTask<Object, Void, Bitmap> {   
    String address;
    BaseAdapter adapter;
    
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
            return;  
        }  
        Document.MainDoc().imageCache.putImage(address, result);//放入缓存  
        adapter.notifyDataSetChanged();
    }  
} 