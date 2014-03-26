/*
 * MyAsyncTask
 *
 * Version 1.0
 *
 * 2014-03-25
 *
 * Copyright notice
 */
package com.jason.Task;

import android.os.AsyncTask;

import com.jason.Interface.IUpdate;
import com.jason.Network.Httper;

/**
 * 异步任务类
 * @author Jason
 *
 */
public class MyAsyncTask extends AsyncTask<String, Integer, String> {
	
	/** fragment引用 */
	private IUpdate fragment;

	public MyAsyncTask(IUpdate fragment) {
		this.fragment = fragment;
	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected String doInBackground(String... params) {
		return Httper.get(params[0]);
	}

	@Override
	protected void onProgressUpdate(Integer... progresses) {
	}

	@Override
	protected void onPostExecute(String result) {
		if (result != null) {
			fragment.updateData(result);
			fragment.updateUI();
		}else
		{
			fragment.updateData(null);
		}
	}

	@Override
	protected void onCancelled() {

	}
}