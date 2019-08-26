package com.northmeter.meshble.bledemo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.northmeter.meshble.R;

/**
 * Created by 601042 on 2017/5/26.
 */
public class CharacteristicList extends Activity {
    ListView list_services;
    BluetoothGattService mServices;
    List<BluetoothGattCharacteristic> characteristicList = new ArrayList<>();
    CharacteristicAdapter adapter;
    int type = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serviceslist);
        mServices = AppContext.getInstance().getmBle().getService(UUID.fromString(getIntent().getStringExtra("service")));
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        type = getIntent().getIntExtra("type", 0);
        initListview();
    }
	private void initListview() {
        list_services = (ListView) findViewById(R.id.list_services);
        if(mServices != null){
            characteristicList = mServices.getCharacteristics();
        }

        adapter = new CharacteristicAdapter(CharacteristicList.this,characteristicList);
        list_services.setAdapter(adapter);
        list_services.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
			@SuppressLint("NewApi")
			@Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (type){
                    case 1:
                        AppContext.getInstance().getmBle().initWritetCharacteristic(mServices.getUuid().toString(),characteristicList.get(position).getUuid().toString());
                        finish();
                        break;
                    case 2:
                        AppContext.getInstance().getmBle().initReadCharacteristic(mServices.getUuid().toString(), characteristicList.get(position).getUuid().toString());
                        finish();
                        break;
                }
            }
        });
    }
}
