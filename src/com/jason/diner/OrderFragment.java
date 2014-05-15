/*
 * OrderFragment
 *
 * Version 1.0
 *
 * 2014-04-23
 *
 * Copyright notice
 */
package com.jason.diner;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.jason.Interface.INotifyImageCompleted;
import com.jason.Interface.IUpdate;
import com.jason.Network.Httper;
import com.jason.Task.FragmentLoadTask;
import com.jason.Task.ImageLoadTask;
import com.jason.diner.MyShopAdapter.ViewHolder;

/**
 * 菜单界面
 * 
 * @author Jason
 * 
 */
public class OrderFragment extends Fragment{


		
	private View rootView;
	private ListView orderList;			//订单列表
	private Button comment;
	private MyOrderAdapter orderAdapter;	//订单适配器


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.order_fragment, container, false);

		
		
		
		comment = (Button)rootView.findViewById(R.id.comment);
		
		/**
		 * 设置提交按钮的监听事件
		 */
		comment.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("rawtypes")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				final EditText inputArea = new EditText(Document.MainDoc().mainActivity);
				inputArea.setFocusable(true);

		        AlertDialog.Builder builder = new AlertDialog.Builder(Document.MainDoc().mainActivity);
		        builder.setTitle(getString(R.string.comment_head)).setIcon(
		                R.drawable.icon).setView(inputArea).setNegativeButton(
		                getString(R.string.comment_cancel), null);
		        builder.setPositiveButton(getString(R.string.comment_sure),
		                new DialogInterface.OnClickListener() {

		                    public void onClick(DialogInterface dialog, int which) {
		                        
		                    }
		                });
		        builder.show();
			}
		});
	
		
		
		
		
		
		//构造绑定OrderList的数据
		Document.MainDoc().order.ordersBinding.clear();
		for (DishInfo item : Document.MainDoc().order.orders) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(DishInfo.KEYS.DISH_ID, item.dishId);
			map.put(DishInfo.KEYS.DISH_IMAGE, item.dishImage);
			map.put(DishInfo.KEYS.DISH_NAME, item.dishName);
			map.put(DishInfo.KEYS.DISH_TASTE, item.dishTaste);
			map.put(DishInfo.KEYS.DISH_FOOD, item.dishFood);
			map.put(DishInfo.KEYS.DISH_COOKING, item.dishCooking);
			map.put(DishInfo.KEYS.DISH_CATEGORY, item.dishCategory);
			map.put(DishInfo.KEYS.SELECTED, false);
			
			Document.MainDoc().order.ordersBinding.add(map);
		}
		
		
		
		orderList = (ListView) rootView.findViewById(R.id.orderList);
		orderAdapter = new MyOrderAdapter(Document.MainDoc().mainActivity,
				Document.MainDoc().order.ordersBinding);
		orderList.setAdapter(orderAdapter);
		

		
		
		return rootView;
	}


	
}

/**
 * 推荐菜列表适配器
 * @author Jason
 *
 */
class MyOrderAdapter extends BaseAdapter implements INotifyImageCompleted{

	//要使用到的数据源
	private ArrayList<HashMap<String, Object>> data = null;
	private Context context;
	private LayoutInflater inflater;

	public MyOrderAdapter(Context context,
			ArrayList<HashMap<String, Object>> data) {
		this.data = data;
		this.context = context;
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
		return (position >= 0 && position < data.size()) ? data.get(position)
				: null;
	}

	//item的id
	@Override
	public long getItemId(int position) {
		return position;
	}

	//绘制每一个item
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		HashMap<String, Object> map = (HashMap<String, Object>) getItem(position);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.order_fragment_item, null);
			holder = new ViewHolder();
			holder.dishImage = 
					(ImageView) convertView.findViewById(R.id.dishImage);
			holder.dishName = 
					(TextView) convertView.findViewById(R.id.dishName);
			holder.dishTaste = (TextView) convertView
					.findViewById(R.id.dishTaste);
			holder.dishFood = (TextView) convertView
					.findViewById(R.id.dishFood);
			holder.dishCooking = (TextView) convertView
					.findViewById(R.id.dishCooking);
			holder.dishCategory = (TextView) convertView
					.findViewById(R.id.dishCategory);
			
			holder.dishState = (TextView) convertView.findViewById(R.id.dishState);

			
			convertView.setTag(holder);

			
		} else{
			holder = (ViewHolder)convertView.getTag();

		}
		
		holder.dishName.setText((String) map.get(DishInfo.KEYS.DISH_NAME));
		holder.dishTaste.setText((String) map.get(DishInfo.KEYS.DISH_TASTE));
		holder.dishFood.setText((String) map.get(DishInfo.KEYS.DISH_FOOD));
		holder.dishCooking.setText(
				(String) map.get(DishInfo.KEYS.DISH_COOKING));
		holder.dishCategory.setText(
				(String) map.get(DishInfo.KEYS.DISH_CATEGORY));
		
		int rd = (int) (Math.random() * (Document.MainDoc().order.states.length));
		String state = Document.MainDoc().order.states[rd];
		holder.dishState.setText(state);
		
		
		//异步加载图片
		String address = (String) map.get(DishInfo.KEYS.DISH_IMAGE);
		Bitmap bitmap = Document.MainDoc().imageCache.getImage(address);// 从缓存中取图片
		if (bitmap != null) {
			holder.dishImage.setImageBitmap( Helper.toRoundCorner(bitmap));
		} else {
			//先设置成默认图片
			holder.dishImage.setImageBitmap(
					Helper.toRoundCorner(
							Helper.Drawable2Bitmap(
									R.drawable.default_dish)));
			if(!Document.MainDoc().imageCache.getDownloading(address)){
				//若之前没有请求下载，现在就请求下载图片
				ImageLoadTask imageLoadTask = new ImageLoadTask();
				String url = Document.MainDoc().server.url;
				imageLoadTask.execute(url, address, this);// 执行异步任务
				Document.MainDoc().imageCache.putDownloading(address);
			}
			
		}
		
		return convertView;
	}
	
	public class ViewHolder{
		ImageView dishImage;
		TextView dishName;
		TextView dishFood;
		TextView dishTaste;
		TextView dishCooking;
		TextView dishCategory;
		
		TextView dishState;

		
	}


}
