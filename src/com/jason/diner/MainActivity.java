package com.jason.diner;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.SearchManager;
import android.content.Context;
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
import android.util.Log;
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

public class MainActivity extends FragmentActivity {
	public DrawerLayout mDrawerLayout;
	public ListView mDrawerList;
	public ActionBarDrawerToggle mDrawerToggle;
	public CharSequence mDrawerTitle;
	public CharSequence mTitle;
	public ArrayList<HashMap<String, Object>> mMenuTitles;

	private int lastSelectItemPosition;
	public FragmentManager fragmentManager;
	public SearchView searchView;
	public MenuItem menuItem;
	public SearchManager searchManager;
	public Fragment fragment;
	public FragmentActivity activity;
	public Resources resources;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		new Document(this);

		fragmentManager = getSupportFragmentManager();
		lastSelectItemPosition = -1;

		setContentView(R.layout.activity_main);

		getActionBar().setBackgroundDrawable(
				this.getResources().getDrawable(R.color.background_color_deep));

		activity = this;
		resources = getResources();
		mTitle = mDrawerTitle = getTitle();
		mMenuTitles = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> map0 = new HashMap<String, Object>();
		map0.put("menuImage", android.R.drawable.ic_menu_agenda);
		map0.put("menuText", "Home");
		mMenuTitles.add(map0);

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("menuImage", android.R.drawable.ic_menu_sort_by_size);
		map1.put("menuText", "Show");
		mMenuTitles.add(map1);

		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("menuImage", android.R.drawable.ic_menu_manage);
		map2.put("menuText", "Setting");
		mMenuTitles.add(map2);

		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("menuImage", android.R.drawable.ic_menu_info_details);
		map3.put("menuText", "About");
		mMenuTitles.add(map3);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		// mDrawerList.setAdapter(new ArrayAdapter<String>(this,
		// R.layout.drawer_list_item, mMenuTitles));
		//
		mDrawerList.setAdapter(new MyDrawerAdapter(this, mMenuTitles));

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			@Override
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

		searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				// 启动页面
				Document.MainDoc().server.prompt = query;
				
				fragment = new SearchFragment();
				fragmentManager = getSupportFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).commit();

				searchView.clearFocus();

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

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_search).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
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

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	public void selectItem(int position) {
		// update the main content by replacing fragments
		if (lastSelectItemPosition == position) {
			mDrawerLayout.closeDrawer(mDrawerList);
			return;
		}
		// mDrawerList.setItemChecked(position, true);

		switch (position) {
		case 0:
			if (Document.MainDoc().shop.shopName == null) {
				fragment = new GuideFragment();
			} else {
				fragment = new MainView();
			}
			break;
		case 1:
			fragment = new ShowFragment();
			break;
		case 2:
			fragment = new SettingFragment();
			break;
		case 3:
			fragment = new AboutFragment();
			break;
		default:
			fragment = new MainView();
			break;
		}
		Bundle args = new Bundle();
		args.putInt("MENU_ID", position);
		fragment.setArguments(args);

		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.content_frame, fragment);
		// fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		fragmentTransaction.commit();
		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle((String) (mMenuTitles.get(position).get("menuText")));
		mDrawerLayout.closeDrawer(mDrawerList);
		lastSelectItemPosition = position;
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

}

class MyDrawerAdapter extends BaseAdapter {

	// 要使用到的数据源
	private ArrayList<HashMap<String, Object>> data = null;// new
															// ArrayList<HashMap<String,

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
		// TODO Auto-generated method stub
		return position;
	}

	// 绘制每一个item
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = (HashMap<String, Object>) getItem(position);
		convertView = inflater.inflate(R.layout.drawer_list_item, null);
		ImageView menuImage = (ImageView) convertView
				.findViewById(R.id.menuImage);
		TextView menuText = (TextView) convertView.findViewById(R.id.menuText);

		menuImage.setBackgroundResource((Integer) (map.get("menuImage")));
		menuText.setText((String) map.get("menuText"));

		return convertView;

	}

}