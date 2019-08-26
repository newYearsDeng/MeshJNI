package com.northmeter.meshble.bluetooth.blueActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.northmeter.meshble.R;
import com.northmeter.meshble.bluetooth.tools.BluetoothConnectionClient;
import com.northmeter.meshble.bluetooth.tools.BluetoothScanClient;
import com.northmeter.meshble.bluetooth.tools.GattCode;
import com.northmeter.meshble.northmeter.BaseActivity.BaseActivity;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MainActivity extends BaseActivity implements
        View.OnClickListener ,BluetoothAdapter.LeScanCallback{

    private final static int SCANNIN_GREQUEST_CODE = 1;
    private static final int REQUEST_CAMERARESULT=201;
    private String TAG = getClass().getSimpleName();
    private BluetoothConnectionClient mConnectionClient;
    private BluetoothScanClient mScanClient;

    private final int FOUND_DEVICE = 0X01;
    private final int DISCONNECTED = 0X02;
    private final int FOUND_SERVICE = 0X03;
    private final int WRITE_SUCCESS = 0X04;
    private final int WRITE_FAILED = 0X05;
    private final int RECONNECT = 0x06;
    private final int RECEIVE = 0x07;
    private final int SEND = 0x08;
    private final int CONNECTED = 0X09;
    private final int BLUEM_ESSAGE = 0x0a;
    private long exitTime;
    private String receive_msg = "";
    private String tableNum,tableMac;//水表编号,mac
    private String telNum;//用户账户（手机号码）
    private DrawerLayout drawer;
    private SharedPreferences sp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void inteView(Bundle savedInstanceState) {
        init_view();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init_view(){
//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(new NavigationItemListener(this,drawer));

        mScanClient = BluetoothScanClient.getInstance(this, this);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
                            tableNum = result.split("#")[1];
                            tableMac = result.split("#")[2];

                            showDialog("正在开阀");
                            if(mConnectionClient!=null){
                                mConnectionClient.disconnect();//若已存在蓝牙连接，搜索前断开；
                            }
                            return;
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {

    }



    //-*-----------------------------------------------------------
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONNECTED://连接成功
                    Toast.makeText(MainActivity.this, "已连接水表，正在开阀", Toast.LENGTH_LONG).show();
                    //text_blue_flag.setText("已连接水表，正在开阀");
                    break;
                case DISCONNECTED://断开连接
                    stopDialog();
                    Log.e(TAG,"连接断开...");
                    //BlueTooth_UniqueInstance.getInstance().setBooleanConnected(false);
                    Toast.makeText(MainActivity.this, "连接断开！", Toast.LENGTH_LONG).show();
                    //text_blue_flag.setText("连接断开");
                    break;
                case RECONNECT:
                    if (mConnectionClient != null) {
                        mConnectionClient.connect();
                    }/*else{
                        if(mScanClient!=null){
                            mScanClient.startScan();
                        }
                    }*/
                    break;
                case RECEIVE:
                    String data = (String) msg.obj;
                    break;
                case SEND:
                    break;
                case FOUND_DEVICE:
                    mScanClient.stopScan();
//                    mConnectionClient = (BluetoothConnectionClient) msg.obj;
                    if (mConnectionClient != null) {
                        mConnectionClient.disconnect();
                    }
                    System.out.println("找到蓝牙并进行连接 ");
                    mConnectionClient = (BluetoothConnectionClient) msg.obj;
                    mConnectionClient.connect();

                    break;
                case BLUEM_ESSAGE:
                    String blueMsg = (String) msg.obj;
                    if (blueMsg.equals("success")) {
                        Toast.makeText(MainActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (blueMsg.equals("fail")) {
                        Toast.makeText(MainActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    break;

            }
        }
    };
    @SuppressLint("NewApi")
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    gatt.discoverServices();
                }
            }
            if (newState == BluetoothGatt.STATE_DISCONNECTED || status == 133) {
                Log.w(TAG, "onConnectionStateChange: disconnected");
                mHandler.sendEmptyMessage(DISCONNECTED);
                mHandler.sendEmptyMessage(RECONNECT);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);

            BluetoothGattService service = gatt.getService(GattCode.FFF_SERVICE);
            System.out.println("Uuid: " + service.getUuid());

            mConnectionClient.addCharacteristic(3, service.getCharacteristic(GattCode.FFF_3));
            mConnectionClient.addCharacteristic(4, service.getCharacteristic(GattCode.FFF_4));

            Log.w(TAG, "onServicesDiscovered: ");
            mConnectionClient.setCharacteristicNotification(GattCode.DESCRIPTOR,
                    3, true);

            mHandler.sendEmptyMessage(FOUND_SERVICE);

        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.w(TAG, "onCharacteristicWrite: status=" + status);
            if (status == 0) {
                Message msg = mHandler.obtainMessage(SEND);
                msg.obj = characteristic.getValue();
                mHandler.sendMessage(msg);
            } else {
                mHandler.sendEmptyMessage(WRITE_FAILED);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            byte[] value = characteristic.getValue();

        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.d(TAG, "onDescriptorWrite: status=" + status);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                mHandler.sendEmptyMessage(CONNECTED);
            }
        }
    };


    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        Log.e(TAG,"找到的蓝牙："+device.getAddress());
        if(device.getAddress().equals(tableMac)){
            BluetoothConnectionClient c = new BluetoothConnectionClient(
                    device, this, mGattCallback);
            Message msg = mHandler.obtainMessage(FOUND_DEVICE);
            msg.obj = c;
            mHandler.sendMessage(msg);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case BluetoothScanClient.MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // The requested permission is granted.
                    if (mScanClient != null) {
                        mScanClient.startScan();
                    }
                } else {
                    // The user disallowed the requested permission.
                    Toast.makeText(this, R.string.permission_failed, Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
        Toast.makeText(this, R.string.permission_failed, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 10);
    }

}
