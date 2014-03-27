/*
 * ShowFragment
 *
 * Version 1.0
 *
 * 2014-03-25
 *
 * Copyright notice
 */
package com.jason.diner;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.Interface.INotifyImageCompleted;
import com.jason.Interface.IUpdate;
import com.jason.Task.FragmentLoadTask;
import com.jason.Task.ImageLoadTask;

/**
 * 菜单界面
 * 
 * @author Jason
 * 
 */
public class ShowFragment extends Fragment implements IUpdate {

	private ListView menuList;			//菜单列表
	private View rootView;
	private ProgressDialog progressbar;

	@Override
	public void updateData(String json) {
		// TODO Auto-generated method stub
		if (!Helper.json2Order(json, Document.MainDoc().order)) {
			if (Document.MainDoc().error == null) {
				Toast.makeText(Document.MainDoc().mainActivity,
						"网络连接异常，请检查网络并重试！", Toast.LENGTH_SHORT).show();
				Document.MainDoc().server.clearParam();
			}else if(Document.MainDoc().error.equals("error")){
				Toast.makeText(Document.MainDoc().mainActivity,
						"网络参数异常，请检查网络并重试！", Toast.LENGTH_SHORT).show();
				Document.MainDoc().server.clearParam();
			}
		} else {

			//处理列表（每个列表项是ViewPager）
			Iterator iter = Document.MainDoc().order.dishes.entrySet()
					.iterator();
			while (iter.hasNext()) {
				
				//得到每组中的数据（分组为：荤、素、汤）
				Map.Entry<String, ArrayList<ArrayList<DishInfo>>> entry = (Map.Entry<String, ArrayList<ArrayList<DishInfo>>>) iter
						.next();

				String key = entry.getKey();
				ArrayList<ArrayList<DishInfo>> value = entry.getValue();
				Document.MainDoc().order.categoryCount.put(key, value.size());

				//分组头
				ArrayList<DishInfo> dishTags = new ArrayList<DishInfo>();
				DishInfo dishTag = new DishInfo();
				dishTag.dishId = "" + -1;
				dishTag.dishCategory = key;
				dishTags.add(dishTag);
				Document.MainDoc().order.dishesBlinding.add(dishTags);

				//分组内容
				for (int i = 0; i < value.size(); i++) {
					Document.MainDoc().order.dishesBlinding.add(value.get(i));
				}

			}

		}
		progressbar.dismiss();

	}

	@Override
	public void updateUI() {
		// TODO Auto-generated method stub
		MyShowAdapter adapter = new MyShowAdapter(
				Document.MainDoc().mainActivity, Document.MainDoc().order);
		menuList.setAdapter(adapter);
	}

	@Override
	public void updateHttp() {
		String param = Helper.rule2Json(Document.MainDoc().rule);
		param = "rule=" + URLEncoder.encode(param);
		String oldParam = Document.MainDoc().server.paramOrder;
		if (oldParam != null && oldParam.trim().equals(param)) {
			//若不是第一次请求，并且参数一样，直接更新UI，不从服务器请求数据
			updateUI();
		} else {
			
			//否则从服务器请求数据
			Document.MainDoc().server.paramOrder = param;
			FragmentLoadTask mTask = new FragmentLoadTask(this);
			mTask.execute(Document.MainDoc().server.getOrderUrl(param));
			progressbar = ProgressDialog.show(Document.MainDoc().mainActivity,
					"Loading...", "Please wait...", true, false);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.show_fragment, container, false);
		menuList = (ListView) rootView.findViewById(R.id.showList);
		if (Document.MainDoc().rule.shopId == null) {
			return rootView;
		}
		updateHttp();
		return rootView;
	}

}

/**
 * 菜单列表适配器类（每一项是ViewPager）
 * 
 * @author Jason
 * 
 */
class MyShowAdapter extends BaseAdapter implements INotifyImageCompleted{

	// 要使用到的数据源
	private ArrayList<ArrayList<DishInfo>> data;
	private LayoutInflater inflater;
	private Context context;

	public MyShowAdapter(Context context, OrderInfo order) {

		this.context = context;
		this.data = order.dishesBlinding;
		inflater = LayoutInflater.from(context);

	}

	@Override
	public void notifyUpdateImage(){
		this.notifyDataSetChanged();
	}
	
	//item的总行数
	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	//item对象
	@Override
	public Object getItem(int position) {
		return data.get(position);

	}

	//item的id
	@Override
	public long getItemId(int position) {
		return position;
	}

	// 绘制每一个item
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ArrayList<DishInfo> item = (ArrayList<DishInfo>) getItem(position);
		boolean isTag = false;
		if (item.get(0).dishId.trim().equals("-1")) {
			isTag = true;
		}

		//处理分组
		TextView dishTag;
		ViewPager dishPager;
		if (isTag) {
			//当前是分组
			convertView = inflater.inflate(R.layout.show_fragment_item_tag,
					null);
			dishTag = (TextView) convertView.findViewById(R.id.dishTag);
			dishTag.setText(item.get(0).dishCategory);
		} else {
			//当前是内容
			convertView = inflater.inflate(R.layout.show_fragment_item, null);
			dishPager = (ViewPager) convertView.findViewById(R.id.dishPager);

			ArrayList<View> viewList = new ArrayList<View>();
			//处理每一项（ViewPager）
			for (int i = 0; i < item.size(); i++) {
				View viewItem = inflater.inflate(
						R.layout.show_fragment_item_viewpager, null);

				if (i % 2 == 0) {
					viewItem.setBackgroundResource(R.color.background_color_light);
				} else {
					viewItem.setBackgroundResource(R.color.background_gray_normal);
				}

				TextView dishName = (TextView) viewItem
						.findViewById(R.id.dishName);
				TextView dishTaste = (TextView) viewItem
						.findViewById(R.id.dishTaste);
				TextView dishFood = (TextView) viewItem
						.findViewById(R.id.dishFood);
				TextView dishCooking = (TextView) viewItem
						.findViewById(R.id.dishCooking);
				TextView dishCategory = (TextView) viewItem
						.findViewById(R.id.dishCategory);
				TextView dishMark = (TextView) viewItem
						.findViewById(R.id.dishMark);

				dishName.setText(item.get(i).dishName);
				dishTaste.setText(item.get(i).dishTaste);
				dishFood.setText(item.get(i).dishFood);
				dishCooking.setText(item.get(i).dishCooking);
				dishCategory.setText(item.get(i).dishCategory);
				if (i > 0) {
					dishMark.setText("备选");
				}

				
				//异步加载图片
				String address = item.get(i).dishImage;
				Bitmap bitmap = Document.MainDoc().imageCache.getImage(address);// 从缓存中取图片
				ImageView dishImage = (ImageView) viewItem
						.findViewById(R.id.dishImage);
				if (bitmap != null) {
					dishImage.setImageBitmap(Helper.toRoundCorner(bitmap));
				} else {
					dishImage.setImageBitmap(Helper.toRoundCorner(Helper
							.Drawable2Bitmap(R.drawable.default_dish)));
					if (!Document.MainDoc().imageCache.getDownloading(
							address)) {
						ImageLoadTask imageLoadTask = new ImageLoadTask();
						String url = Document.MainDoc().server.url;
						imageLoadTask.execute(url, address, this);// 执行异步任务
						Document.MainDoc().imageCache.putDownloading(address);
					}

				}

				viewList.add(viewItem);

			}
			//绑定ViewPager
			dishPager.setAdapter(new MyItemPagerAdapter(viewList));
			dishPager.setCurrentItem(0);
		}
		return convertView;
	}

}

/**
 * 每一个列表项的ViewPager适配器类（使列表项可横向滑动：备选菜单）
 * 
 * @author Jason
 * 
 */
class MyItemPagerAdapter extends PagerAdapter {
	private ArrayList<View> viewList;

	public MyItemPagerAdapter(ArrayList<View> viewList) {
		this.viewList = viewList;
	}

	@Override
	public int getCount() {
		return viewList.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(viewList.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(viewList.get(position));
		return viewList.get(position);

	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

}
