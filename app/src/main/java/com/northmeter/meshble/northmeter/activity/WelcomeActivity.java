package com.northmeter.meshble.northmeter.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.northmeter.meshble.R;
import com.northmeter.meshble.northmeter.BaseActivity.BaseActivity;

/**
 * Created by dyd on 2018/5/17.
 */

public class WelcomeActivity extends BaseActivity{
    @Override
    protected int getLayoutId() {
        return R.layout.activity_we;
    }

    @Override
    protected void inteView(Bundle savedInstanceState) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    goActivity(LoginActivity.class);
                    finish();
            }
        }, 2000);
    }
}
