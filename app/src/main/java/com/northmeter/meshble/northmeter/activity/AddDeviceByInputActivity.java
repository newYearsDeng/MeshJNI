package com.northmeter.meshble.northmeter.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.northmeter.meshble.R;
import com.northmeter.meshble.northmeter.BaseActivity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dyd on 2018/5/18.
 * 手动输入添加设备
 */

public class AddDeviceByInputActivity extends BaseActivity {
    @BindView(R.id.actitity_title)
    TextView actitityTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_adddevice_byinput;
    }

    @Override
    protected void inteView(Bundle savedInstanceState) {
        actitityTitle.setText("设备信息");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
