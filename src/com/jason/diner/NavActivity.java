package com.jason.diner;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class NavActivity extends Activity {

	private ViewPager mViewPager;// 声明ViewPager对象
	private PagerTitleStrip mPagerTitleStrip;// 声明动画标题
	private ImageView mPageImg;// 动画图片
	private int currIndex = 0;// 当前页面
	private ImageView mPage0, mPage1, mPage2;// 声明导航图片对象

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
		mPagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pagertitle);

		mPage0 = (ImageView) findViewById(R.id.page0);
		mPage1 = (ImageView) findViewById(R.id.page1);
		mPage2 = (ImageView) findViewById(R.id.page2);

		// 将要分页显示的View装入数组中
		int[] welcomImg = { R.drawable.welcome0, R.drawable.welcome1,
				R.drawable.welcome2};

		LayoutInflater mLi = LayoutInflater.from(this);
		final ArrayList<View> views = new ArrayList<View>();
		final ArrayList<String> titles = new ArrayList<String>();
		for (int i = 0; i < COUNT; i++) {
			View view = mLi.inflate(R.layout.nav_welcome, null);
			LinearLayout lay = (LinearLayout) view
					.findViewById(R.id.welcomeImg);
			lay.setBackgroundResource(welcomImg[i]);
			views.add(view);
			titles.add("" + i);
		}

		// 填充ViewPager的数据适配器，我们重写即可
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
			public void destroyItem(View container, int position, Object object) {
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

		mViewPager.setAdapter(mPagerAdapter);// 与ListView用法相同，设置重写的Adapter。这样就实现了ViewPager的滑动效果。
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		public void onPageSelected(int arg0) {// 参数arg0为选中的View

			Animation animation = null;// 声明动画对象
			switch (arg0) {
			case 0: // 页面一
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.point_dark));// 进入第一个导航页面，小圆点为选中状态，下一个页面的小圆点是未选中状态。
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.point_light));
				if (currIndex == arg0 + 1) {
					animation = new TranslateAnimation(arg0 + 1, arg0, 0, 0);// 圆点移动效果动画，从当前View移动到下一个View
				}
				break;
			case 1: // 页面二
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.point_dark));// 当前View
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.point_light));// 上一个View
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.point_light));// 下一个View
				if (currIndex == arg0 - 1) {// 如果滑动到上一个View
					animation = new TranslateAnimation(arg0 - 1, arg0, 0, 0); // 圆点移动效果动画，从当前View移动到下一个View

				} else if (currIndex == arg0 + 1) {// 圆点移动效果动画，从当前View移动到下一个View，下同。

					animation = new TranslateAnimation(arg0 + 1, arg0, 0, 0);
				}
				break;
			case 2:
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.point_dark));
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.point_light));

				if (currIndex == arg0 - 1) {
					animation = new TranslateAnimation(arg0 - 1, arg0, 0, 0);
				}
				break;
			}
			currIndex = arg0;// 设置当前View
			animation.setFillAfter(true);// True:设置图片停在动画结束位置
			animation.setDuration(300);// 设置动画持续时间
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