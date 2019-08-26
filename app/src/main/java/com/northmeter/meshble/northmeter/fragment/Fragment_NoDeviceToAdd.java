package com.northmeter.meshble.northmeter.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.northmeter.meshble.R;
import com.northmeter.meshble.northmeter.BaseActivity.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dyd on 2018/5/18.
 * 没有设备时显示设备添加界面
 */

public class Fragment_NoDeviceToAdd extends BaseFragment {

    @BindView(R.id.actitity_title)
    TextView actitityTitle;
    Unbinder unbinder;

    public static Fragment_NoDeviceToAdd newInstance(String msg) {
        Fragment_NoDeviceToAdd fragment = new Fragment_NoDeviceToAdd();
        Bundle bundle = new Bundle();
        bundle.putString("title", msg);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_nodevice_to_add;
    }

    @Override
    protected void startGetArgument(Bundle savedInstanceState) {

    }

    @Override
    protected void finishCreateView(Bundle savedInstanceState) {
        String titles = getArguments().getString("title").toString();
        actitityTitle.setText(titles);
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
