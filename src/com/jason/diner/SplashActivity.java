/*
 * SplashActivity
 *
 * Version 1.0
 *
 * 2014-03-25
 *
 * Copyright notice
 */
package com.jason.diner;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

/**
 * 启动界面
 * @author Jason
 *
 */
public class SplashActivity extends Activity {

	private final int SPLASH_DISPLAY_LENGHT = 1500; 	//延迟1.5秒

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash_activity);
		
		//判断是否是第一次使用
		SharedPreferences preferences = getSharedPreferences("app_conf",
				MODE_PRIVATE);
		final boolean isFirstIn = preferences.getBoolean("isFirstIn", true);

		new Handler().postDelayed(new Runnable() {
			public void run() {
				if (isFirstIn) {
					//是第一次进入应用中，切换到SplashActivity
					Intent mainIntent = new Intent(SplashActivity.this,
							NavActivity.class);
					SplashActivity.this.startActivity(mainIntent);
					SplashActivity.this.finish();
				}else{
					//不是第一次进入应用中，切换到应用中去
					Intent mainIntent = new Intent(SplashActivity.this,
							MainActivity.class);
					SplashActivity.this.startActivity(mainIntent);
					SplashActivity.this.finish();
				}
			}

		}, SPLASH_DISPLAY_LENGHT);

	}
}