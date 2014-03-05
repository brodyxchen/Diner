package com.jason.diner;

//import android.app.Fragment;
//import android.app.FragmentManager;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jason.Interface.UIInterface;
import com.jason.Task.ImageLoadTask;
import com.jason.Task.MyAsyncTask;

public class SearchFragment extends Fragment implements UIInterface {

	private ListView searchList;
	private MySearchAdapter searchAdapter;
	private View rootView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void updateData(String json) {
		// TODO Auto-generated method stub
		if(!Helper.json2Search(json, Document.MainDoc().search)){
			Toast.makeText(Document.MainDoc().mainActivity.activity, "请求数据异常，请重试！",
				     Toast.LENGTH_SHORT).show();
		}else{
			for (ShopInfo item : Document.MainDoc().search.searchList) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(ShopInfoKey.shopId, item.shopId);
				map.put(ShopInfoKey.shopImage, item.shopImage);
				map.put(ShopInfoKey.shopName, item.shopName);
				map.put(ShopInfoKey.shopAddress, item.shopAddress);
				map.put(ShopInfoKey.shopIntroduce, item.shopIntroduce);
				Document.MainDoc().search.searchListBlinding.add(map);
			}
		}

	}

	@Override
	public void updateUI() {
		searchList = (ListView) rootView.findViewById(R.id.searchList);
		searchAdapter = new MySearchAdapter(Document.MainDoc().mainActivity.activity,
				Document.MainDoc().search.searchListBlinding);
		searchList.setAdapter(searchAdapter);
		searchList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

						Document.MainDoc().shop = Document.MainDoc().search.searchList.get(position);
						Fragment fragment = new MainView();

						// //添加参数
						// Bundle args = new Bundle();
						// args.putString(Document.MainDoc().SelectShopId,
						// selectShop.get("shopId"));
						// fragment.setArguments(args);

						// Document.MainDoc().mainActivity.selectItem(0);

						//Document.MainDoc().mainActivity.selectItem(0);
						FragmentManager fragmentManager = getFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager
								.beginTransaction();
						fragmentTransaction.add(R.id.content_frame, fragment);
						// fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
						fragmentTransaction.commit();
					}

				});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.search_shop, container, false);
		
		MyAsyncTask mTask = new MyAsyncTask(this);
		String param = "keyword=" + Document.MainDoc().server.prompt;
		mTask.execute(Document.MainDoc().server.getSearchUrl(param));

		return rootView;
	}
}

class MySearchAdapter extends BaseAdapter {

	// 要使用到的数据源
	private ArrayList<HashMap<String, Object>> data = null;// new
															// ArrayList<HashMap<String,

	private LayoutInflater inflater;
	private Context context;

	public MySearchAdapter(Context context,
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

		ViewHolder holder;
		HashMap<String, Object> map = (HashMap<String, Object>) getItem(position);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.search_shop_item, null);
			holder = new ViewHolder();
			holder.shopImage = (ImageView) convertView.findViewById(R.id.shopImage);
			holder.shopName = (TextView) convertView.findViewById(R.id.shopName);
			holder.shopAddress = (TextView) convertView
					.findViewById(R.id.shopAddress);
			holder.shopIntroduce = (TextView) convertView
					.findViewById(R.id.shopIntroduce);
			convertView.setTag(holder);
		} else{
			holder = (ViewHolder)convertView.getTag();
		}

		holder.shopName.setText((String) map.get(ShopInfoKey.shopName));
		holder.shopAddress.setText((String) map.get(ShopInfoKey.shopAddress));
		holder.shopIntroduce.setText((String) map.get(ShopInfoKey.shopIntroduce));

		try {
			String address = (String) map.get(ShopInfoKey.shopImage);
			Bitmap bitmap = Document.MainDoc().imageCache.getImage(address);// 从缓存中取图片

			if (bitmap != null) {
				holder.shopImage.setImageBitmap( Helper.toRoundCorner(bitmap));
			} else {// 缓存没有就设置为默认图片，并且从网络异步下载
				holder.shopImage.setImageBitmap(Helper.toRoundCorner(Helper.Drawable2Bitmap(R.drawable.ic_launcher)));
				ImageLoadTask imageLoadTask = new ImageLoadTask();
				String url = Document.MainDoc().server.url;
				imageLoadTask.execute(url, address, this);// 执行异步任务
			}
		} catch (Exception e) {
			Test.error("SearchFragment.MySearchAdapter.getView()", e.toString());
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
