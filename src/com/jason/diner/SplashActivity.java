package com.jason.diner;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

	private final int SPLASH_DISPLAY_LENGHT = 1500; // —”≥Ÿ¡˘√Î

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		// getActionBar().hide();

		setContentView(R.layout.splash_activity);
		SharedPreferences preferences = getSharedPreferences("app_conf",
				MODE_PRIVATE);
		final boolean isFirstIn = preferences.getBoolean("isFirstIn", true);

		new Handler().postDelayed(new Runnable() {
			public void run() {
				if (isFirstIn) {
					Intent mainIntent = new Intent(SplashActivity.this,
							NavActivity.class);
					SplashActivity.this.startActivity(mainIntent);
					SplashActivity.this.finish();
				}else{
					Intent mainIntent = new Intent(SplashActivity.this,
							MainActivity.class);
					SplashActivity.this.startActivity(mainIntent);
					SplashActivity.this.finish();
				}
			}

		}, SPLASH_DISPLAY_LENGHT);

	}
}