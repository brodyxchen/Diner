package com.jason.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.jason.Interface.UIInterface;
import com.jason.Network.Httper;

public class MyAsyncTask extends AsyncTask<String, Integer, String> {
	// onPreExecute方法用于在执行后台任务前做一些UI操作
	UIInterface fragment;

	public MyAsyncTask(UIInterface fragment) {
		this.fragment = fragment;
	}

	@Override
	protected void onPreExecute() {

	}

	// doInBackground方法内部执行后台任务,不可在此方法内修改UI
	@Override
	protected String doInBackground(String... params) {
		return Httper.get(params[0]);
	}

	// onProgressUpdate方法用于更新进度信息
	@Override
	protected void onProgressUpdate(Integer... progresses) {
		// progressBar.setProgress(progresses[0]);
		// textView.setText("loading..." + progresses[0] + "%");
	}

	// onPostExecute方法用于在执行完后台任务后更新UI,显示结果
	@Override
	protected void onPostExecute(String result) {
		if (result != null) {
			fragment.updateData(result);
			fragment.updateUI();
		}
	}

	// onCancelled方法用于在取消执行中的任务时更改UI
	@Override
	protected void onCancelled() {

	}
}