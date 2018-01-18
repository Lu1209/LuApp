package com.qsqy.notebook;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.qsqy.notebook.sdk.statusbar.Utils;

/**
 * Created by CK on 2018/1/2.
 */

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setStatusTextColor(true, this);
        Utils.setStatusBar(this,false,false);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制为竖屏
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
//        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
//		getSupportActionBar().hide();// 去掉标题栏

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 沉浸式
//            Window window = this.getWindow();
//            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//            View view = this.findViewById(R.id.id_top);
//            Log.d("api","view:"+view);
//            if(view != null){
//                int statusHeight = Utils.getStatusBarHeight(getApplicationContext());
//                ViewGroup.LayoutParams lp = view.getLayoutParams();
//                lp.height = statusHeight;
//                view.setLayoutParams(lp);
//                view.setVisibility(View.VISIBLE);
//            }
//
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 沉浸式
            Window window = this.getWindow();
//            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

            View view = this.findViewById(R.id.id_top);
            Log.d("api","view:"+view);
            if(view != null){
                int statusHeight = Utils.getStatusBarHeight(getApplicationContext());
                ViewGroup.LayoutParams lp = view.getLayoutParams();
                lp.height = statusHeight;
                view.setLayoutParams(lp);
                view.setVisibility(View.VISIBLE);
            }

        }
    }
}
