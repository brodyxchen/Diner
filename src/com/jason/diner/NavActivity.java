/*
 * NavActivity
 *
 * Version 1.0
 *
 * 2014-03-26
 *
 * Copyright notice
 */
package com.jason.diner;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 导航页（应用第一次启动时出现）
 * @author Jason
 *
 */
public class NavActivity extends Activity {

	private ViewPager mViewPager;				//ViewPager对象
	private int currIndex = 0;					//当前页面坐标
	private ImageView mPage0, mPage1, mPage2;	//导航图片对象

	private final int COUNT = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nav_activity);

		SharedPreferences preferences = getSharedPreferences("app_conf",
				MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("isFirstIn", false);
		editor.commit();

		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		mPage0 = (ImageView) findViewById(R.id.page0);
		mPage1 = (ImageView) findViewById(R.id.page1);
		mPage2 = (ImageView) findViewById(R.id.page2);

		int[] welcomImg = { R.drawable.welcome0, R.drawable.welcome1,
				R.drawable.welcome2};

		LayoutInflater mLi = LayoutInflater.from(this);
		final ArrayList<View> views = new ArrayList<View>();
		final ArrayList<String> titles = new ArrayList<String>();
		for (int i = 0; i < COUNT; i++) {
			View view = mLi.inflate(R.layout.nav_activity_image, null);
			LinearLayout lay = (LinearLayout) view
					.findViewById(R.id.welcomeImg);
			lay.setBackgroundResource(welcomImg[i]);
			views.add(view);
			titles.add("" + i);
		}

		//重写ViewPager的适配器
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container,
					int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public CharSequence getPageTitle(int position) {
				return titles.get(position);
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};

		mViewPager.setAdapter(mPagerAdapter);
	}

	/**
	 * 页面滑动监听器
	 * @author Jason
	 *
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		/**
		 * 选中某页是的处理方法
		 */
		public void onPageSelected(int arg0) {

			Animation animation = null;		//声明动画对象
			switch (arg0) {
			case 0: //页面0
				//进入第一个导航页面，小圆点为选中状态，下一个页面的小圆点是未选中状态。
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.point_dark));
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.point_light));
				if (currIndex == arg0 + 1) {
					//圆点移动效果动画，从当前View移动到下一个View
					animation = new TranslateAnimation(arg0 + 1, arg0, 0, 0);
				}
				break;
			case 1: //页面1
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.point_dark));		//当前View
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.point_light));		//上一个View
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.point_light));		//下一个View
				
				
				if (currIndex == arg0 - 1) {//如果滑动到上一个View
					//圆点移动效果动画，从当前View移动到下一个View
					animation = new TranslateAnimation(arg0 - 1, arg0, 0, 0); 

				} else if (currIndex == arg0 + 1) {//如果滑动到下一个View
					//圆点移动效果动画，从当前View移动到下一个View
					animation = new TranslateAnimation(arg0 + 1, arg0, 0, 0);
				}
				break;
			case 2: //页面2
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.point_dark));
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.point_light));

				if (currIndex == arg0 - 1) {
					animation = new TranslateAnimation(arg0 - 1, arg0, 0, 0);
				}
				break;
			}
			currIndex = arg0;				//设置当前View
			animation.setFillAfter(true);	//True:设置图片停在动画结束位置
			animation.setDuration(300);		//设置动画持续时间
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

	}

	public void startbutton(View v) {
		Intent mainIntent = new Intent(NavActivity.this, MainActivity.class);
		NavActivity.this.startActivity(mainIntent);
		NavActivity.this.finish();

	}
}