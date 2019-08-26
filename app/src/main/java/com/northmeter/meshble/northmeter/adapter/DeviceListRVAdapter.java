package com.northmeter.meshble.northmeter.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.northmeter.meshble.R;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * Created by dyd on 2018/5/22.
 */

public class DeviceListRVAdapter extends RecyclerView.Adapter<DeviceListRVAdapter.DeviceListViewHolder>{

    public DeviceListRVAdapter(){}

    public OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void  OnItemClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public DeviceListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_devicelist_item,parent,false);
        DeviceListViewHolder viewHolder  = new DeviceListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final DeviceListViewHolder holder, final int position) {
        holder.device_name.setText("");
        holder.checkbox_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.OnItemClick(holder.checkbox_light,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class DeviceListViewHolder extends RecyclerView.ViewHolder{

        public TextView device_name;
        public CheckBox checkbox_light;
        public DeviceListViewHolder(View itemView) {
            super(itemView);
            device_name = itemView.findViewById(R.id.tv_device_name);
            AutoUtils.autoSize(itemView);
        }
    }
}
