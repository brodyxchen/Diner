/*
 * ShopFragment
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

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.Interface.IUpdate;
import com.jason.Task.FragmentLoadTask;
import com.jason.Task.ImageLoadTask;

/**
 * 餐馆界面
 * @author Jason
 *
 */
public class ShopFragment extends Fragment implements IUpdate{

	private ImageView shopImage;
	private TextView shopName, shopAddress, shopIntroduce;
	private ShopInfo shop;
	private View rootView;
	private ListView topList;
	private MyShopAdapter topAdapter;
	private ProgressDialog progressbar;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public void updateData(String json) {
		// TODO Auto-generated method stub

		if(!Helper.json2Shop(json, Document.MainDoc().shop)){
			Toast.makeText(Document.MainDoc().mainActivity,
					"网络连接异常，请检查网络并重试",
				     Toast.LENGTH_SHORT).show();
			Document.MainDoc().server.clearParam();
		}else{
			for (DishInfo item : Document.MainDoc().shop.topList) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(DishInfo.KEYS.DISH_ID, item.dishId);
				map.put(DishInfo.KEYS.DISH_IMAGE, item.dishImage);
				map.put(DishInfo.KEYS.DISH_NAME, item.dishName);
				map.put(DishInfo.KEYS.DISH_TASTE, item.dishTaste);
				map.put(DishInfo.KEYS.DISH_FOOD, item.dishFood);
				map.put(DishInfo.KEYS.DISH_COOKING, item.dishCooking);
				map.put(DishInfo.KEYS.DISH_CATEGORY, item.dishCategory);
				map.put(DishInfo.KEYS.SELECTED, false);
				
				Document.MainDoc().shop.topListBlinding.add(map);
				Document.MainDoc().shop.selectedTopList.put(item.dishId, false);
			}
		}
		
		progressbar.dismiss();
		
	}

	@Override
	public void updateUI() {
		// TODO Auto-generated method stub

		topList = (ListView) rootView.findViewById(R.id.topList);
		topAdapter = new MyShopAdapter(Document.MainDoc().mainActivity,
				Document.MainDoc().shop.topListBlinding);
		topList.setAdapter(topAdapter);
		//topList.smoothScrollToPosition(0);
		
		topList.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
	                long arg3) {
	        	MyShopAdapter.ViewHolder holder = (MyShopAdapter.ViewHolder)arg1.getTag();
	        	CheckBox cb = holder.checkbox;
	        	String dishId = (String)cb.getTag();
	        	cb.setChecked(!cb.isChecked());
				Document.MainDoc().shop.selectedTopList.put(dishId,
						cb.isChecked());
	        }
	    });
		
	}

	@Override
	public void updateHttp(){
		String param = "shopId=" + Document.MainDoc().shop.shopId;
		String oldParam = Document.MainDoc().server.paramShop;
		if(oldParam != null && oldParam.trim().equals(param)){
			updateUI();
		}else{
			Document.MainDoc().server.paramShop = param;
			FragmentLoadTask mTask = new FragmentLoadTask(this);
			mTask.execute(Document.MainDoc().server.getShopUrl(param));
			progressbar = ProgressDialog.show(
					Document.MainDoc().mainActivity,
					"Loading..", "Please wait...", true, false);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.shop_fragment, container, false);
		shop = Document.MainDoc().shop;
		if(shop.shopId == null && shop.shopName == null){
			return rootView;
		}
		shopImage = (ImageView) rootView.findViewById(R.id.shopImage);
		shopName = (TextView) rootView.findViewById(R.id.shopName);
		shopAddress = (TextView) rootView.findViewById(R.id.shopAddress);
		shopIntroduce = (TextView) rootView.findViewById(R.id.shopIntroduce);
		
		shopName.setText(shop.shopName);
		shopAddress.setText(shop.shopAddress);
		shopIntroduce.setText(shop.shopIntroduce);
		
		Bitmap bitmap = Document.MainDoc().imageCache.getImage(shop.shopImage);
		if(bitmap == null){
			shopImage.setImageBitmap(
					Helper.toRoundCorner(
							Helper.Drawable2Bitmap(R.drawable.icon)));
		}else{
			shopImage.setImageBitmap( Helper.toRoundCorner(bitmap));
		}


		updateHttp();
		
		
		return rootView;
	}

	

	
}

/**
 * 推荐菜列表适配器
 * @author Jason
 *
 */
class MyShopAdapter extends BaseAdapter {

	// 要使用到的数据源
	private ArrayList<HashMap<String, Object>> data = null;
	private Context context;
	private LayoutInflater inflater;

	public MyShopAdapter(Context context,
			ArrayList<HashMap<String, Object>> data) {
		this.data = data;
		this.context = context;
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
		ViewHolder holder;
		HashMap<String, Object> map = (HashMap<String, Object>) getItem(position);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.shop_fragment_item, null);
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
			holder.checkbox = 
					(CheckBox) convertView.findViewById(R.id.dishSelect);
			
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
		
		String dishId = (String)map.get(DishInfo.KEYS.DISH_ID);

		holder.checkbox.setChecked(
				Document.MainDoc().shop.selectedTopList.get(dishId));
		holder.checkbox.setTag(dishId);
//		holder.checkbox.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				CheckBox cb = (CheckBox)arg0;
//				String dishId = (String)cb.getTag();
//				Document.MainDoc().shop.selectedTopList.put(dishId,
//						cb.isChecked());
//
//			}
//		});
		
		
		String address = (String) map.get(DishInfo.KEYS.DISH_IMAGE);
		Bitmap bitmap = Document.MainDoc().imageCache.getImage(address);// 从缓存中取图片
		if (bitmap != null) {
			holder.dishImage.setImageBitmap( Helper.toRoundCorner(bitmap));
		} else {
			holder.dishImage.setImageBitmap(
					Helper.toRoundCorner(
							Helper.Drawable2Bitmap(
									R.drawable.default_dish)));
			if(!Document.MainDoc().imageCache.getDownloading(address)){
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
		CheckBox checkbox;
		
	}

}
