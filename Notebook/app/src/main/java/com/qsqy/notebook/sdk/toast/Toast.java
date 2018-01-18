package com.qsqy.notebook.sdk.toast;

import android.app.Activity;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.qsqy.notebook.R;

/**
 * 自定义Toast
 **/

public class Toast {
	/***
	 * 不能超过3.5秒，因为是调用系统的toast，所以超过这个最大值是达不到大于3.5秒效果的。
	 * @param context
	 * @param word
	 * @param time
	 */
	public static void show(final Activity context, final String word, final long time){
		context.runOnUiThread(new Runnable() {
			public void run() {
				final android.widget.Toast toast = android.widget.Toast.makeText(context, word, android.widget.Toast.LENGTH_LONG);
				toast.show();
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					public void run() {
						toast.cancel(); 
					}
				}, time);
			}
		});
	}

	/**
	 * 自定义Toast
	 * @param content
	 */
	public static void showToast(final Activity context, String content, long time) {
		final android.widget.Toast toast = new android.widget.Toast(context);
		View layout = View.inflate(context, R.layout.view_toast, null);
		TextView toastText = (TextView) layout.findViewById(R.id.toast_tv);
		toastText.setText(content);
		toastText.setTextSize(50);
		toast.setView(layout);
//		toast.setGravity(Gravity.CENTER,0,0);//居中
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.TOP, 80, 370);
		toast.show();
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				toast.cancel();
			}
		}, time);
	}
}
