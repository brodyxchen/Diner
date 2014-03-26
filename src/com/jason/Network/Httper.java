/*
 * Httper
 *
 * Version 1.0
 *
 * 2014-03-25
 *
 * Copyright notice
 */
package com.jason.Network;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.jason.diner.Test;

/**
 * 网络请求类
 * @author Jason
 *
 */
public class Httper {

	/**
	 * 请求图片资源
	 * @param url 资源完整URI地址
	 * @return	图片资源
	 */
	public static Bitmap loadImage(String url) {
		Bitmap bitmap = null;
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		InputStream inputStream = null;
		try {
			response = client.execute(new HttpGet(url));
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();
			bitmap = BitmapFactory.decodeStream(inputStream);
		} catch (ClientProtocolException e) {
			Test.error("Httper.loadImage()", e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			Test.error("Httper.loadImage()", e.toString());
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * HTTP Get方法
	 * @param url 服务器URL地址
	 * @return 请求的数据
	 */
	public static String get(String url) {
		Test.info("Httper=", url);
		String result = null;
		try {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 5*1000);
			HttpConnectionParams.setSoTimeout(httpParameters, 10*1000);
			
			HttpClient client = new DefaultHttpClient(httpParameters);
			HttpGet get = new HttpGet(url);
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(),"UTF-8");
			}
			Test.info("Httper2=", result);
			return result;
		} catch (Exception e) {
			Test.error("Httper.get()", e.toString());
			return null;
		}
		
	}


}
