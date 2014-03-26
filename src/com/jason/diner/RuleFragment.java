/*
 * RuleFragment
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
import java.util.Iterator;
import java.util.Map;
import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jason.Interface.IUpdate;
import com.jason.Task.FragmentLoadTask;

/**
 * 规则界面
 * @author Jason
 *
 */
public class RuleFragment extends Fragment implements IUpdate {

	private Button submit;			//提交规则（创建菜单）
	private View rootView ;
	
	//存储用户选择的规则信息
	private HashMap<String, ArrayList<ViewPair>> checkBoxList;
	
	private ProgressDialog progressbar;
	
	//几荤几素几汤 的NumberPicker
	private NumberPicker picker1, picker2, picker3;
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	rootView = inflater.inflate(R.layout.rule_fragment, container, false);
    	ShopInfo shop = Document.MainDoc().shop;
		if(shop.shopId == null && shop.shopName == null){
			return rootView;
		}
    	
		//设置NumberPicker
		checkBoxList = new HashMap<String, ArrayList<ViewPair>>();
		picker1 = (NumberPicker) rootView.findViewById(R.id.ruleNumberPicker1);
		picker1.setMaxValue(8);
		picker1.setMinValue(0);
		picker1.setValue(3);
		picker2 = (NumberPicker) rootView.findViewById(R.id.ruleNumberPicker2);
		picker2.setMaxValue(8);
		picker2.setMinValue(0);
		picker2.setValue(2);
		picker3 = (NumberPicker) rootView.findViewById(R.id.ruleNumberPicker3);
		picker3.setMaxValue(3);
		picker3.setMinValue(0);
		picker3.setValue(1);
    	
		submit = (Button)rootView.findViewById(R.id.createShow);
		
		/**
		 * 设置提交按钮的监听事件
		 */
		submit.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("rawtypes")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RuleInfo rule = Document.MainDoc().rule;
				rule.clear();
				rule.shopId = Document.MainDoc().shop.shopId;
				Iterator iter0 = Document.MainDoc().shop.selectedTopList.
									entrySet().iterator();
				while(iter0.hasNext()){
					@SuppressWarnings("unchecked")
					Map.Entry<String, Boolean> entry = 
						(Map.Entry<String, Boolean>) iter0.next();
					if(entry.getValue()){
						rule.staredDishList.add(entry.getKey());
					}
				}
				rule.categoryList.add("" + picker1.getValue());
				rule.categoryList.add("" + picker2.getValue());
				rule.categoryList.add("" + picker3.getValue());
				Iterator iter1 = checkBoxList.entrySet().iterator();
				while(iter1.hasNext()){
					Map.Entry<String, ArrayList<ViewPair>> entry = 
							(Map.Entry<String, ArrayList<ViewPair>>) 
							iter1.next();
					ArrayList<Boolean> tempList = new ArrayList<Boolean>();
					ArrayList<ViewPair> value = entry.getValue();
					for(int i = 0; i < value.size(); i++){
						ViewPair vp = (ViewPair)value.get(i);
						tempList.add(vp.checkBox.isChecked());
					}
					rule.conditionList.put(entry.getKey(), tempList);
				}
				Document.MainDoc().mainActivity.selectItem(2);
			}
		});
		
		//提交到服务器
		updateHttp();
		
		return rootView;
    }
    

    /**
     * 提交到服务器
     */
    @Override
    public void updateHttp(){
		String param = "rule=rule";
		String oldParam = Document.MainDoc().server.paramRule;
		if(oldParam != null && oldParam.trim().equals(param)){
			updateUI();
		}else{
			Document.MainDoc().server.paramRule = param;
			FragmentLoadTask mTask = new FragmentLoadTask(this);
			mTask.execute(Document.MainDoc().server.getConditionUrl(null));
			
			//显示加载条
			progressbar = ProgressDialog.show(
					Document.MainDoc().mainActivity, "Loading...",
					"Please wait...", true, false);
		}
    }
    
	@Override
	public void updateData(String json) {
		// TODO Auto-generated method stub
		if(!Helper.json2Condition(json, Document.MainDoc().condition)){
			Toast.makeText(Document.MainDoc().mainActivity, "网络连接异常，请检查网络并重试",
				     Toast.LENGTH_SHORT).show();
			Document.MainDoc().server.clearParam();
		}
		
		//结束加载条
		progressbar.dismiss();
	}

	/**
	 * 更新UI
	 */
	@Override
	public void updateUI() {
		//第一层布局（最外层）
		RelativeLayout ruleMain = 
				(RelativeLayout) rootView.findViewById(R.id.ruleMain);
		
		//获取从服务器得到的规则条件信息
		ConditionInfo ruleInfo = Document.MainDoc().condition;
		int screemW = Document.MainDoc().screenWidth;
		Iterator iter = ruleInfo.conditions.entrySet().iterator();
		
		int belowId = R.id.ruleArea0;
		int idIndex = 1;
		
		//根据条件信息，自动创建UI布局
		while(iter.hasNext()){
			Map.Entry<String, ArrayList<String>> entry = 
					(Map.Entry<String, ArrayList<String>>) iter.next();
			
			String key = entry.getKey();
			ArrayList<String> value = entry.getValue();
			ArrayList<ViewPair> pairList = new ArrayList<ViewPair>();
			
			//第二层布局
			LinearLayout ruleArea = 
					new LinearLayout(Document.MainDoc().mainActivity);
			ruleArea.setBackgroundResource(R.color.background_gray_light);
			ruleArea.setId(idIndex);
			ruleArea.setOrientation(LinearLayout.VERTICAL);
			RelativeLayout.LayoutParams areaParam = 
					new RelativeLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT,
							ViewGroup.LayoutParams.WRAP_CONTENT);
			areaParam.addRule(RelativeLayout.BELOW, belowId);
			belowId = idIndex;
			idIndex++;
			areaParam.topMargin = 10;
			ruleMain.addView(ruleArea, areaParam);

			//条件（/规则）分组标题
			TextView ruleHead = new TextView(Document.MainDoc().mainActivity);
			
			Resources resource = Document.MainDoc().mainActivity.resources;
			ColorStateList csl = (ColorStateList) resource.getColorStateList(
							R.color.font_color_deep);  
			if (csl != null) {  
				ruleHead.setTextColor(csl);  
			}
			ruleHead.setTextSize(18);
			ruleHead.setText(key);
			LinearLayout.LayoutParams headParam = 
					new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT,
							ViewGroup.LayoutParams.WRAP_CONTENT);
			headParam.leftMargin = 5;
			ruleArea.addView(ruleHead, headParam);
			
			//分组的内容（里面的CheckBox和TextView 每4对一行，防止一行太拥挤）
			int size = value.size();
			int col = 4;
			int row = (int)Math.ceil((double) size / col);
			
			//遍历可能的行数
			for(int i = 0; i < row; i++){
				LinearLayout ruleZone = 
						new LinearLayout(Document.MainDoc().mainActivity);
				LinearLayout.LayoutParams zoneParam = 
						new LinearLayout.LayoutParams(
								ViewGroup.LayoutParams.MATCH_PARENT,
								ViewGroup.LayoutParams.WRAP_CONTENT);
				zoneParam.topMargin = 10;
				ruleZone.setOrientation(LinearLayout.HORIZONTAL);
				ruleArea.addView(ruleZone, zoneParam);
				
				//对每一行的4列，创建CheckBox和TextView对，最后一行不满4对的特殊处理
				for(int j = 0; j < col; j++){
					int index = i*col + j;
					if(index >= size)
						break;
					
					CheckBox cb = new CheckBox(Document.MainDoc().mainActivity);

					LinearLayout.LayoutParams cbParam = 
							new LinearLayout.LayoutParams(
									screemW / col / 3,
									ViewGroup.LayoutParams.WRAP_CONTENT);
					TextView tv = new TextView(Document.MainDoc().mainActivity);
					LinearLayout.LayoutParams tvParam = 
							new LinearLayout.LayoutParams(
									screemW / col / 3 * 2,
									ViewGroup.LayoutParams.WRAP_CONTENT);
					tv.setText(value.get(index));
					tv.setTag(cb);
					tv.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							CheckBox cb = (CheckBox)v.getTag();
							cb.setChecked(!cb.isChecked());
						}
					});
					ColorStateList csl2 = 
							(ColorStateList) resource.getColorStateList(
									R.color.font_gray_normal);  
					if (csl != null) {  
						tv.setTextColor(csl2);  
					}
					
					pairList.add(new ViewPair(cb, tv));
					ruleZone.addView(cb, cbParam);
					ruleZone.addView(tv, tvParam);
					
				}
			}
			
			//UI上的分割线
			View divider = new View(Document.MainDoc().mainActivity);
			LinearLayout.LayoutParams dividerParam = 
					new LinearLayout.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT, 1);
			divider.setBackgroundResource(R.color.divider_gray_normal);
			ruleArea.addView(divider, dividerParam);

			//存储创建的所有CheckBox和TextView
			checkBoxList.put(key, pairList);
			
		}

		
	}

	/**
	 * 把一个CheckBox和一个TextView绑到一起的类
	 * @author Jason
	 *
	 */
    class ViewPair{
    	public CheckBox checkBox;
    	public TextView textView;
    	ViewPair(CheckBox cb, TextView tv){
    		checkBox = cb;
    		textView = tv;
    	}
    	
    }
	
}
