package com.northmeter.meshble.northmeter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.northmeter.meshble.R;
import com.northmeter.meshble.jnjutils.JniUtilsST;
import com.northmeter.meshble.northmeter.BaseActivity.BaseActivity;
import com.northmeter.meshble.northmeter.fragment.Fragment_NoDeviceToAdd;
import com.northmeter.meshble.northmeter.widget.EmptyFragmentPagerAdapter;
import com.northmeter.meshble.northmeter.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener,ViewPager.OnPageChangeListener{

    @BindView(R.id.button)
    Button button;

    public final static int SCANNIN_GREQUEST_CODE = 1;
    public final static int REQUEST_CAMERARESULT = 201;
    @BindView(R.id.vp_empty)
    NoScrollViewPager vpEmpty;
    @BindView(R.id.radio_button_0)
    RadioButton radioButton0;
    @BindView(R.id.radio_button_1)
    RadioButton radioButton1;
    @BindView(R.id.radio_button_2)
    RadioButton radioButton2;
    @BindView(R.id.radio_button_3)
    RadioButton radioButton3;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    private List<Fragment> fragments = new ArrayList<>();
    private EmptyFragmentPagerAdapter adapter;

    private String Tag = "MainActicity";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void inteView(Bundle savedInstanceState) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationItemListener(this, drawer));

        //System.out.println("*************" + new JniUtilsST().securityK4derivation("12313213".getBytes()));

        fragments.add(Fragment_NoDeviceToAdd.newInstance("设备列表"));
        fragments.add(Fragment_NoDeviceToAdd.newInstance("情景模式"));
        fragments.add(Fragment_NoDeviceToAdd.newInstance("分享"));
        fragments.add(Fragment_NoDeviceToAdd.newInstance("我的"));
        adapter = new EmptyFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        vpEmpty.setAdapter(adapter);
        vpEmpty.setOffscreenPageLimit(4);
        vpEmpty.addOnPageChangeListener(this);

        radioGroup.setOnCheckedChangeListener(this);
        radioButton0.setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @OnClick(R.id.button)
    public void onViewClicked() {
        startActivity(new Intent(this, com.northmeter.meshble.bledemo.MainActivity.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_FIRST_USER) {
                    if (data != null) {
                        if (data.hasExtra("result")) {//扫描到水表编号返回数据
                            String result = data.getStringExtra("result").toString();
                            System.out.println("result::" + result);
                            return;
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId){
            case R.id.radio_button_0:
                radioButton0.setChecked(true);
                vpEmpty.setCurrentItem(0);
                break;
            case R.id.radio_button_1:
                radioButton1.setChecked(true);
                vpEmpty.setCurrentItem(1);
                break;
            case R.id.radio_button_2:
                radioButton2.setChecked(true);
                vpEmpty.setCurrentItem(2);
                break;
            case R.id.radio_button_3:
                radioButton3.setChecked(true);
                vpEmpty.setCurrentItem(3);
                break;

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        System.out.println(position);
        switch (position) {
            case 0:
                radioButton0.setChecked(true);
                break;
            case 1:
                radioButton1.setChecked(true);
                break;
            case 2:
                radioButton2.setChecked(true);
                break;
            case 3:
                radioButton3.setChecked(true);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
