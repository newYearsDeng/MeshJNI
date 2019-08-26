package com.northmeter.meshble.bledemo;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.northmeter.meshble.R;


public class ServicesAdapter extends BaseAdapter {
	private final static String TAG = "ServicesAdapter";

	List<BluetoothGattService> servicesList;
	private Context context;

	public ServicesAdapter(Context context,List<BluetoothGattService> servicesList){
		this.context = context;
		this.servicesList = servicesList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return servicesList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(position < 0){
			return servicesList.get(0);
		}
		if(position > servicesList.size() -1){
			return servicesList.get(servicesList.size()-1);
		}
		return servicesList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
		// 濡傛灉鏄涓�娆℃樉绀鸿椤甸潰(瑕佽寰椾繚瀛樺埌viewholder涓緵涓嬫鐩存帴浠庣紦瀛樹腑璋冪敤)
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_services, null);
			holder.tv_item_servicesname = (TextView) convertView.findViewById(R.id.tv_item_servicesname);
			holder.tv_item_uuid = (TextView) convertView.findViewById(R.id.tv_item_uuid);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		String deviceName = servicesList.get(position).toString();
		String uuid = servicesList.get(position).getUuid().toString();
		holder.tv_item_servicesname.setText(deviceName);
		holder.tv_item_uuid.setText(uuid);

		return convertView;
	}

	public void refreshData(List<BluetoothGattService> servicesList){
		this.servicesList = servicesList;
		notifyDataSetChanged();
	}




	private class ViewHolder {

		TextView tv_item_servicesname;
		TextView tv_item_uuid;

	}

}
