/*
 * MainFragment
 *
 * Version 1.0
 *
 * 2014-03-25
 *
 * Copyright notice
 */
package com.jason.diner;

import java.util.ArrayList;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 主界面
 * @author Jason
 *
 */
public class MainFragment extends Fragment {
	
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentsList;
    private ImageView ivBottomLine;
    private TextView tvTabShop, tvTabRule;

    private int currIndex;
    private int position_one;
    private Resources resources;

    private View rootView;
    private Fragment shopfragment;
    private Fragment ruleFragment;
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        resources = Document.MainDoc().mainActivity.resources;
        currIndex = 0;
	}


	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.main_fragment, container, false);

        InitWidth();
        InitTextView();
        InitViewPager();
        return rootView;
    }
    

    private void InitTextView() {
        tvTabShop = (TextView) rootView.findViewById(R.id.tv_tab_shop);
        tvTabRule = (TextView) rootView.findViewById(R.id.tv_tab_rule);

        tvTabShop.setOnClickListener(new MyOnClickListener(0));
        tvTabRule.setOnClickListener(new MyOnClickListener(1));
    }

    private void InitViewPager() {
        mPager = (ViewPager) rootView.findViewById(R.id.vPager);
        fragmentsList = new ArrayList<Fragment>();

        if(shopfragment == null)
        	shopfragment = new ShopFragment();
        if(ruleFragment == null)
        	ruleFragment = new RuleFragment();
        
        fragmentsList.add(shopfragment);
        fragmentsList.add(ruleFragment);

        mPager.setAdapter(new MyFragmentPagerAdapter(
        		Document.MainDoc().mainActivity.fragmentManager,
        		fragmentsList));
        mPager.setCurrentItem(0);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());

    }

    private void InitWidth() {
        
        DisplayMetrics dm = new DisplayMetrics();
        Document.MainDoc().mainActivity.getWindowManager().
        		getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        Document.MainDoc().screenWidth = screenW;
        ivBottomLine = (ImageView) rootView.findViewById(R.id.iv_bottom_line);
        ivBottomLine.getLayoutParams().width = (int)(screenW / 2.0);

        position_one = (int) (screenW / 2.0);
        
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    };

    public class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
            case 0:
                if (currIndex == 1) {
                    animation = new TranslateAnimation(position_one, 0, 0, 0);
                    tvTabRule.setTextColor(
                    		resources.getColor(R.color.font_color_light));
                }
                tvTabShop.setTextColor(
                		resources.getColor(R.color.font_color_deep));
                break;
            case 1:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(0, position_one, 0, 0);
                    tvTabShop.setTextColor(
                    		resources.getColor(R.color.font_color_light));
                }
                tvTabRule.setTextColor(
                		resources.getColor(R.color.font_color_deep));
                break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);
            animation.setDuration(300);
            ivBottomLine.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}


/**
 * ViewPager适配器类
 * @author Jason
 *
 */
class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragmentsList;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyFragmentPagerAdapter(FragmentManager fm,
    		ArrayList<Fragment> fragments) {
        super(fm);
        this.fragmentsList = fragments;
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    
    @Override
	public Object instantiateItem(ViewGroup container, int position) {
		return super.instantiateItem(container, position);
	}

	@Override
    public Fragment getItem(int arg0) {
        return fragmentsList.get(arg0);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

}