/*
 * MainActivity
 *
 * Version 1.0
 *
 * 2014-03-25
 *
 * Copyright notice
 */
package com.jason.diner;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

/**
 * 应用的主Activity，处理整个应用的结构
 * 
 * @author Jason
 * 
 */
public class MainActivity extends FragmentActivity {

	public FragmentManager fragmentManager;
	public Resources resources;

	// Drawer相关变量
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private ArrayList<HashMap<String, Object>> mMenuTitles;

	//
	private Fragment fragment;
	private SearchView searchView;
	private SearchManager searchManager;

	//
	private MenuItem searchItem;
	private int lastSelectItemPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		new Document(this);

		fragmentManager = getSupportFragmentManager();
		lastSelectItemPosition = -1;

		setContentView(R.layout.main_activity);

		getActionBar().setBackgroundDrawable(
				this.getResources().getDrawable(R.color.background_color_deep));

		resources = getResources();
		mTitle = mDrawerTitle = getTitle();
		mMenuTitles = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> map0 = new HashMap<String, Object>();
		map0.put("menuImage", android.R.drawable.ic_menu_search);
		map0.put("menuText", "搜索");
		mMenuTitles.add(map0);

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("menuImage", android.R.drawable.ic_menu_agenda);
		map1.put("menuText", "主页");
		mMenuTitles.add(map1);

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("menuImage", android.R.drawable.ic_menu_sort_by_size);
		map2.put("menuText", "菜单");
		mMenuTitles.add(map2);

		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("menuImage", android.R.drawable.ic_menu_info_details);
		map3.put("menuText", "关于");
		mMenuTitles.add(map3);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		mDrawerList.setAdapter(new MyDrawerAdapter(this, mMenuTitles));

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			@Override
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();

			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();

			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
	}

	public void closeSearchAction() {
		searchItem.collapseActionView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchItem = menu.findItem(R.id.action_search);
		searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				Document.MainDoc().server.prompt = query;
				selectItem(0);
				Fragment currentFragment = Document.MainDoc().currentFragment;
				if (currentFragment == null
						|| !currentFragment.getTag()
								.equals(FRAGMENT_TAG.SEARCH)) {
					fragment = new SearchFragment();
					Document.MainDoc().currentFragment = fragment;
					FragmentTransaction fragmentTransaction = fragmentManager
							.beginTransaction();
					fragmentTransaction.replace(R.id.content_frame, fragment,
							FRAGMENT_TAG.SEARCH.toString());
					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.commit();
					searchView.clearFocus();
				} else {
					SearchFragment searchFragment = (SearchFragment) currentFragment;
					searchFragment.updateHttp();
				}

				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}
		});

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_search).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		case R.id.action_search:
			item.collapseActionView();
			return true;
		case android.R.id.home:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	public void setChecked(int position, boolean checked) {
		Test.info("SelectItem pos=", "" + position);
		mDrawerList.setItemChecked(position, checked);
	}

	public void selectItem(int position) {


		if (lastSelectItemPosition == position) {
			setChecked(position, true);
			setTitle((String) (mMenuTitles.get(position).get("menuText")));
			mDrawerLayout.closeDrawer(mDrawerList);
			lastSelectItemPosition = position;
			return;
		}

		FRAGMENT_TAG tag = FRAGMENT_TAG.GUIDE;
		switch (position) {
		case 0:
			fragment = new GuideFragment();
			tag = FRAGMENT_TAG.GUIDE;
			break;
		case 1:
			fragment = new MainFragment();
			tag = FRAGMENT_TAG.MAIN;
			break;
		case 2:
			fragment = new ShowFragment();
			tag = FRAGMENT_TAG.SHOW;
			break;
		case 3:
			fragment = new AboutFragment();
			tag = FRAGMENT_TAG.ABOUT;
			break;
		default:
			fragment = new GuideFragment();
			tag = FRAGMENT_TAG.GUIDE;
			break;
		}
		Bundle args = new Bundle();

		args.putInt("MENU_ID", position);
		fragment.setArguments(args);

		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.content_frame, fragment,
				tag.toString());
		Document.MainDoc().currentFragment = fragment;
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();

		setChecked(position, true);
		setTitle((String) (mMenuTitles.get(position).get("menuText")));
		mDrawerLayout.closeDrawer(mDrawerList);
		lastSelectItemPosition = position;
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this).setTitle("确认退出吗？")
				.setIcon(android.R.drawable.ic_menu_info_details)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 点击“确认”后的操作
						MainActivity.this.finish();

					}
				})
				.setNegativeButton("返回", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 点击“返回”后的操作,这里不设置没有任何操作
					}
				}).show();
		// super.onBackPressed();
	}
}

/**
 * Drawer的适配器类
 * 
 * @author Jason
 * 
 */
class MyDrawerAdapter extends BaseAdapter {

	// 要使用到的数据源
	private ArrayList<HashMap<String, Object>> data = null;

	private LayoutInflater inflater;
	private Context context;

	public MyDrawerAdapter(Context context,
			ArrayList<HashMap<String, Object>> data) {
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);

	}

	// item的总行数
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data == null ? 0 : data.size();
	}

	// item对象
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return (position >= 0 && position < data.size()) ? data.get(position)
				: null;
	}

	// item的id
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HashMap<String, Object> map = (HashMap<String, Object>) getItem(position);
		convertView = inflater.inflate(R.layout.main_activity_drawer_list_item,
				null);
		ImageView menuImage = (ImageView) convertView
				.findViewById(R.id.menuImage);
		TextView menuText = (TextView) convertView.findViewById(R.id.menuText);

		menuImage.setBackgroundResource((Integer) (map.get("menuImage")));
		menuText.setText((String) map.get("menuText"));

		return convertView;

	}

}