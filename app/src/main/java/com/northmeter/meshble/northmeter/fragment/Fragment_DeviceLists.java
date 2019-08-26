package com.northmeter.meshble.northmeter.fragment;

import android.app.Application;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.northmeter.meshble.R;
import com.northmeter.meshble.northmeter.BaseActivity.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dyd on 2018/5/21.
 * 设备列表
 */

public class Fragment_DeviceLists extends BaseFragment {


    @BindView(R.id.listview_chage)
    RecyclerView listviewChage;
    Unbinder unbinder;

    public static Fragment_DeviceLists newInstance(String msg) {
        Fragment_DeviceLists fragment = new Fragment_DeviceLists();
        Bundle budle = new Bundle();
        budle.putString("msg", msg);
        fragment.setArguments(budle);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_device_lists;
    }

    @Override
    protected void startGetArgument(Bundle savedInstanceState) {

    }

    @Override
    protected void finishCreateView(Bundle savedInstanceState) {
        //设置recyclerview管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        listviewChage.setLayoutManager(linearLayoutManager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
