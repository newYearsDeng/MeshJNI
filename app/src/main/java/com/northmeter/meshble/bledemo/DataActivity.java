package com.northmeter.meshble.bledemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.northmeter.meshble.R;

/**
 * Created by 601042 on 2017/5/26.
 */
public class DataActivity extends Activity {
    private static final String TAG = "DataActivity";
    Button data_btn_write;
    Button data_btn_notify;
    Button data_btn_readnotify;
    Button data_btn_send;
    Button data_btn_read;
    EditText data_edt_write;
    TextView data_tv_show;
    Handler mHandler = new Handler();
    GetDataCallback callback = new GetDataCallback() {
        @Override
        public void onGetData(String characteristicUUid, final byte[] data) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                   data_tv_show.setText(data_tv_show.getText().toString()+CodeUtil.bytesToString(data));
                }
            });
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_data);
            initView();
            AppContext.getInstance().getmBle().setDataCallback(callback);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void initView() {
        data_btn_write = (Button) findViewById(R.id.data_btn_write);//选择写的特征值
        data_btn_notify = (Button) findViewById(R.id.data_btn_notify);//选择通知的特征值
        data_btn_readnotify = (Button) findViewById(R.id.data_btn_readnotify);//选择读的特征值
        
        data_btn_send = (Button) findViewById(R.id.data_btn_send);//写数据
        data_btn_read = (Button) findViewById(R.id.data_btn_read);//读数据

        data_edt_write = (EditText) findViewById(R.id.data_edt_write);
        data_edt_write.setText("read,elec,1");
        
        data_tv_show = (TextView) findViewById(R.id.data_tv_show);
        
        data_btn_readnotify.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(DataActivity.this,ServicesList.class);
                i.putExtra("type",2);
                startActivity(i);
			}
		});
        data_btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DataActivity.this,ServicesList.class);
                i.putExtra("type",1);
                startActivity(i);
            }
        });
        data_btn_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DataActivity.this,ServicesList.class);
                i.putExtra("type",0);
                startActivity(i);
            }
        });
        data_btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppContext.getInstance().getmBle().sendByBLE(data_edt_write.getText().toString());
            }
        });
        
        data_btn_read.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 AppContext.getInstance().getmBle().readResulr();
			}
		});



    }
}
