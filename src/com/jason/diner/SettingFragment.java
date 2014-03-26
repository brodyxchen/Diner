/*
 * SettingFragment
 *
 * Version 1.0
 *
 * 2014-03-25
 *
 * Copyright notice
 */
package com.jason.diner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

/**
 * 设置界面
 * @author Jason
 *
 */
public class SettingFragment extends Fragment {
	
	private View rootView;
	private RadioGroup radioGroup0;
	private RadioButton radioButton0, radioButton2;
	private EditText editText;
	private Button startWelcome, cancelButton, applyButton;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		rootView = inflater
				.inflate(R.layout.setting_fragment, container, false);

		radioGroup0 = (RadioGroup) rootView
				.findViewById(R.id.SettingServerRadioGroup0);
		radioButton0 = (RadioButton) rootView
				.findViewById(R.id.SettingServerRadioButton0);
		radioButton2 = (RadioButton) rootView
				.findViewById(R.id.SettingServerRadioButton2);
		editText = (EditText) rootView
				.findViewById(R.id.SettingServerEditText0);

		radioGroup0.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				RadioButton rb = (RadioButton) rootView.findViewById(checkedId);
				if (rb.getId() == R.id.SettingServerRadioButton2) {
					editText.setEnabled(true);
					group.clearFocus();
					editText.requestFocus();

				} else {
					editText.setEnabled(false);
				}

			}
		});

		startWelcome = (Button) rootView.findViewById(R.id.startWelcome);
		startWelcome.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent mainIntent = new Intent(Document.MainDoc().mainActivity,
								NavActivity.class);
				Document.MainDoc().mainActivity.startActivity(mainIntent);
			}
		});
		
		
		cancelButton = (Button) rootView.findViewById(R.id.SettingCancel);
		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				radioButton0.setChecked(true);
				editText.setText(null);
			}
		});

		applyButton = (Button) rootView.findViewById(R.id.SettingApply);
		applyButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (radioButton2.isChecked()) {
					String url = editText.getText().toString();
					boolean isUrl = Helper.isUrl(url);
					if (isUrl) {
						Document.MainDoc().server.url = url;
					} else {
						Toast.makeText(Document.MainDoc().mainActivity,
								"无效的URL",
								Toast.LENGTH_SHORT).show();
					}
				}

			}
		});

		return rootView;
	}
	

}
