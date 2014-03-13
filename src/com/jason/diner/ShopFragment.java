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
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.Interface.UIInterface;
import com.jason.Task.ImageLoadTask;
import com.jason.Task.MyAsyncTask;

public class ShopFragment extends Fragment implements UIInterface{

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
			Toast.makeText(Document.MainDoc().mainActivity, "没有推荐菜",
				     Toast.LENGTH_SHORT).show();
		}else{
			for (DishInfo item : Document.MainDoc().shop.topList) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(DishInfoKey.dishId, item.dishId);
				map.put(DishInfoKey.dishImage, item.dishImage);
				map.put(DishInfoKey.dishName, item.dishName);
				map.put(DishInfoKey.dishTaste, item.dishTaste);
				map.put(DishInfoKey.dishFood, item.dishFood);
				map.put(DishInfoKey.dishCooking, item.dishCooking);
				map.put(DishInfoKey.dishCategory, item.dishCategory);
				map.put(DishInfoKey.selected, false);
				
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
			shopImage.setImageBitmap(Helper.toRoundCorner(Helper.Drawable2Bitmap(R.drawable.ic_launcher)));
		}else{
			shopImage.setImageBitmap( Helper.toRoundCorner(bitmap));
		}

		String param = "shopId=" + Document.MainDoc().shop.shopId;
		String oldParam = Document.MainDoc().server.paramShop;
		if(oldParam != null && oldParam.trim().equals(param)){
			updateUI();
		}else{
			Document.MainDoc().server.paramShop = param;
			MyAsyncTask mTask = new MyAsyncTask(this);
			mTask.execute(Document.MainDoc().server.getShopUrl(param));
			progressbar = ProgressDialog.show(Document.MainDoc().mainActivity, "Loading..", "Please wait...", true, false);
		}

		
		
		return rootView;
	}

}

class MyShopAdapter extends BaseAdapter {

	// 要使用到的数据源
	private ArrayList<HashMap<String, Object>> data = null;// new
															// ArrayList<HashMap<String,
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
			holder.dishImage = (ImageView) convertView.findViewById(R.id.dishImage);
			holder.dishName = (TextView) convertView.findViewById(R.id.dishName);
			holder.dishTaste = (TextView) convertView
					.findViewById(R.id.dishTaste);
			holder.dishFood = (TextView) convertView
					.findViewById(R.id.dishFood);
			holder.dishCooking = (TextView) convertView
					.findViewById(R.id.dishCooking);
			holder.dishCategory = (TextView) convertView
					.findViewById(R.id.dishCategory);
			holder.checkbox = (CheckBox) convertView.findViewById(R.id.dishSelect);
			
			convertView.setTag(holder);
		} else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.dishName.setText((String) map.get(DishInfoKey.dishName));
		holder.dishTaste.setText((String) map.get(DishInfoKey.dishTaste));
		holder.dishFood.setText((String) map.get(DishInfoKey.dishFood));
		holder.dishCooking.setText((String) map.get(DishInfoKey.dishCooking));
		holder.dishCategory.setText((String) map.get(DishInfoKey.dishCategory));
		
		String dishId = (String)map.get(DishInfoKey.dishId);

		holder.checkbox.setChecked(Document.MainDoc().shop.selectedTopList.get(dishId));
		holder.checkbox.setTag(dishId);
		holder.checkbox.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CheckBox cb = (CheckBox)arg0;
				String dishId = (String)cb.getTag();
				Document.MainDoc().shop.selectedTopList.put(dishId, cb.isChecked());

			}
		});
		
		
		
		try {
			String address = (String) map.get(DishInfoKey.dishImage);
			Bitmap bitmap = Document.MainDoc().imageCache.getImage(address);// 从缓存中取图片
			if (bitmap != null) {
				holder.dishImage.setImageBitmap( Helper.toRoundCorner(bitmap));
			} else {
				holder.dishImage.setImageBitmap(Helper.toRoundCorner(Helper.Drawable2Bitmap(R.drawable.ic_launcher)));
				ImageLoadTask imageLoadTask = new ImageLoadTask();
				String url = Document.MainDoc().server.url;
				imageLoadTask.execute(url, address, this);// 执行异步任务
			}
		} catch (Exception e) {
			Test.error("ShopFragment.MyShopAdapter.getView()", e.toString());
		}

		return convertView;

	}
	
	class ViewHolder{
		ImageView dishImage;
		TextView dishName;
		TextView dishFood;
		TextView dishTaste;
		TextView dishCooking;
		TextView dishCategory;
		CheckBox checkbox;
		
	}

}
