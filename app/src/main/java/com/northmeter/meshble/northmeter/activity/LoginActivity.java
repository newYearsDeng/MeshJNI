package com.northmeter.meshble.northmeter.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.northmeter.meshble.R;
import com.northmeter.meshble.northmeter.BaseActivity.BaseActivity;
import com.northmeter.meshble.northmeter.widget.CustomImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dyd on 2018/5/17.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void inteView(Bundle savedInstanceState) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_login, R.id.tv_forget_passwd, R.id.tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login://登录按钮
                goActivity(MainActivity.class);
                break;
            case R.id.tv_forget_passwd://忘记密码
                break;
            case R.id.tv_register://快速注册
                break;
        }
    }
}
