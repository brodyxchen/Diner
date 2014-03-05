package com.jason.Network;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.jason.diner.Test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Httper {

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

	public static String get(String url) {
		String result = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(),"UTF-8");
			}
			return result;
		} catch (Exception e) {
			Test.error("Httper.get()", e.toString());
			return null;
		}
		
	}


}
