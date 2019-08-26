package com.northmeter.meshble.northmeter.BaseActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.northmeter.meshble.northmeter.helper.ProgressAlertDialog;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dyd
 * 2018/01/29
 */
public abstract class BaseActivity extends AutoLayoutActivity {
    private Unbinder unbinder;

    ProgressAlertDialog mProgress;
    protected abstract int getLayoutId();
    protected abstract void inteView(Bundle savedInstanceState);
    protected Timer timer;
    protected TimerTask timerTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 锁定竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(getLayoutId());

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

//          WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
//          localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);


        }

        unbinder = ButterKnife.bind(this);
        // 初始化控件
        inteView(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    /**
     * activity简单跳转
     */
    public void goActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    /**
     * activity带数据跳转
     */
    public void goActivity(Class<?> cls, Intent intent) {
        intent.setClass(this, cls);
        startActivity(intent);
    }

    /**
     * activity带数据跳转
     */
    public void goActivityWithTitle(Class<?> cls, String title) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    /**开始网络加载动画*/
    protected  void showDialog(String msg){
        mProgress = new ProgressAlertDialog(this);
        mProgress.show(msg);

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if(mProgress.isShowing()){
                    mProgress.dismiss();
                    showToastMain("连接超时...");
                }
            }
        };
        timer.schedule(timerTask,12000);
    }

    /**停止网络加载动画*/
    protected  void stopDialog(){
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if(mProgress==null){
            mProgress = new ProgressAlertDialog(this);
        }
        if(mProgress.isShowing()){
            mProgress.dismiss();
        }
    }

    /**通知MainActivity显示toast*/
    protected  void showToastMain(String message){
    }

}
