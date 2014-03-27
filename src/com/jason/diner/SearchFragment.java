/*
 * SearchFragment
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
 * 搜索界面
 * @author Jason
 *
 */
public class SearchFragment extends Fragment implements IUpdate{

	private ListView searchList;			//搜索结果的listView
	private MySearchAdapter searchAdapter;	//搜索的适配器
	private View rootView;
	private ProgressDialog progressbar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void updateData(String json) {
		// TODO Auto-generated method stub
		if(!Helper.json2Search(json, Document.MainDoc().search)){
			if (Document.MainDoc().error == null) {
				Toast.makeText(Document.MainDoc().mainActivity,
						"网络连接异常，请检查网络并重试！", Toast.LENGTH_SHORT).show();
				Document.MainDoc().server.clearParam();
			}else if(Document.MainDoc().error.equals("error")){
				Toast.makeText(Document.MainDoc().mainActivity,
						"网络参数异常，请检查网络并重试！", Toast.LENGTH_SHORT).show();
				Document.MainDoc().server.clearParam();
			}
		}else{
			
			//构造绑定到searchList(ListView)的数据
			for (ShopInfo item : Document.MainDoc().search.searchList) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(ShopInfo.KEYS.SHOP_ID, item.shopId);
				map.put(ShopInfo.KEYS.SHOP_IMAGE, item.shopImage);
				map.put(ShopInfo.KEYS.SHOP_NAME, item.shopName);
				map.put(ShopInfo.KEYS.SHOP_ADDRESS, item.shopAddress);
				map.put(ShopInfo.KEYS.SHOP_INTRODUCE, item.shopIntroduce);
				Document.MainDoc().search.searchListBlinding.add(map);
			}
			
		}
		progressbar.dismiss();
	}

	@Override
	public void updateUI() {
		
		//把数据绑定到searchList
		searchList = (ListView) rootView.findViewById(R.id.searchList);
		searchAdapter = new MySearchAdapter(Document.MainDoc().mainActivity,
				Document.MainDoc().search.searchListBlinding);
		searchList.setAdapter(searchAdapter);
		searchList.smoothScrollToPosition(0);
		searchList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						//切换到选择的项（切换 fragment）
						Document.MainDoc().mainActivity.closeSearchAction();
						Document.MainDoc().shop = 
								Document.MainDoc().search.
								searchList.get(position);
						Fragment fragment = new MainFragment();
						Document.MainDoc().mainActivity.selectItem(1);
					}

				});
	}

	@Override
	public void updateHttp(){
		String param = "keyword=" + Document.MainDoc().server.prompt;
		String oldParam = Document.MainDoc().server.paramSearch;
		if(oldParam != null && oldParam.trim().equals(param)){
			//不是第一次加载并且关键字重复，直接更新UI
			updateUI();
		}else{
			//否则，从服务器请求数据
			Document.MainDoc().server.paramSearch = param;
			FragmentLoadTask mTask = new FragmentLoadTask(this);
			mTask.execute(Document.MainDoc().server.getSearchUrl(param));
			progressbar = ProgressDialog.show(
					Document.MainDoc().mainActivity,
					"Loading...", "Please wait...", true, false);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.search_fragment, container, false);
		
		updateHttp();
		
		return rootView;
	}
	

}

/**
 * 搜索列表的适配器类
 * @author Jason
 *
 */
class MySearchAdapter extends BaseAdapter implements INotifyImageCompleted {

	//要使用到的数据源
	private ArrayList<HashMap<String, Object>> data = null;

	private LayoutInflater inflater;
	private Context context;

	public MySearchAdapter(Context context,
			ArrayList<HashMap<String, Object>> data) {
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);

	}

	@Override
	public void notifyUpdateImage(){
		this.notifyDataSetChanged();
	}
	
	//item的总行数
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data == null ? 0 : data.size();
	}

	//item对象
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return (position >= 0 && position < data.size()) ? data.get(position)
				: null;
	}

	//item的id
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	//绘制每一个item
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder;
		HashMap<String, Object> map = 
				(HashMap<String, Object>) getItem(position);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.search_fragment_item, null);
			holder = new ViewHolder();
			holder.shopImage = 
					(ImageView) convertView.findViewById(R.id.shopImage);
			holder.shopName = 
					(TextView) convertView.findViewById(R.id.shopName);
			holder.shopAddress = (TextView) convertView
					.findViewById(R.id.shopAddress);
			holder.shopIntroduce = (TextView) convertView
					.findViewById(R.id.shopIntroduce);
			convertView.setTag(holder);
		} else{
			holder = (ViewHolder)convertView.getTag();
		}

		//设置UI中显示的数据
		holder.shopName.setText(
				(String) map.get(ShopInfo.KEYS.SHOP_NAME));
		holder.shopAddress.setText(
				(String) map.get(ShopInfo.KEYS.SHOP_ADDRESS));
		holder.shopIntroduce.setText(
				(String) map.get(ShopInfo.KEYS.SHOP_INTRODUCE));

		//异步加载图片
		String address = (String) map.get(ShopInfo.KEYS.SHOP_IMAGE);
		Bitmap bitmap = Document.MainDoc().imageCache.getImage(address);// 从缓存中取图片

		if (bitmap != null) {
			holder.shopImage.setImageBitmap(Helper.toRoundCorner(bitmap));
		} else {// 缓存没有就设置为默认图片，并且从网络异步下载
			holder.shopImage.setImageBitmap(Helper.toRoundCorner(Helper
					.Drawable2Bitmap(R.drawable.icon)));
			if(!Document.MainDoc().imageCache.getDownloading(address)){
				ImageLoadTask imageLoadTask = new ImageLoadTask();
				String url = Document.MainDoc().server.url;
				imageLoadTask.execute(url, address, this);// 执行异步任务
				Document.MainDoc().imageCache.putDownloading(address);
			}
		}

		return convertView;

	}
	
	class ViewHolder{
		ImageView shopImage;
		TextView shopName;
		TextView shopAddress;
		TextView shopIntroduce;
	}

}
