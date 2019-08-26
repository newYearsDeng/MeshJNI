package com.northmeter.meshble.bledemo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BleHelper {

    private final static String TAG = "BleHelper";

    /**
     * 扫描结果的callback
     */
    private BleScanResultCallback resultCallback;
    /**
     * 设备连接状态监听的callback
     */
    private BleConnectionCallback connectionStateCallback;
    /**
     * ble读写订阅发现服务等结果的callback
     *
     */
    private BleReadOrWriteCallback bleReadOrWriteCallback;
    /**
     * 收到数据时调用的的callback
     *
     */
    private  GetDataCallback dataCallback;
    /**
     * 蓝牙的适配器
     */
    private BluetoothAdapter mBtAdapter;
    /**
     * 上下文
     */
    private Context context;
    /**
     * gatt的集合
     */
    private Map<String, BluetoothGatt> mBluetoothGatts;
    /**
     * 蓝牙的service列表
     */
    List<BluetoothGattService> servicesList;
    /**
     * 写到描述者的内容
     */
    public static final UUID DESC_CCC = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    /**
     * 用于写数据（发送数据）的特征字
     */
    private BluetoothGattCharacteristic mGattCharacteristicWrite = null;
    /**
     * 用于读数据（通过轮询收数据）的特征字
     */
    private BluetoothGattCharacteristic mGattCharacteristicRead = null;
    /**
     * 当前连接的设备的address
     */
    private static String deviceAddress = "";
    /**
     * 重连的次数
     */
    int reconnectCount = 0;
    /**
     * 是否进行重连
     */
    private boolean canreconntect = false;
    /**
     * gatt连接callback
     */
	private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

		@Override
        public void onConnectionStateChange(final BluetoothGatt gatt, int status,
                                            int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.e(TAG, "onConnectionStateChange()       status:" + status + "    newState:" + newState);
            String address = gatt.getDevice().getAddress();
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //如果是主动断开的 则断开
                if (newState == BluetoothProfile.STATE_CONNECTED) {

                    reconnectCount = 0;
                    Log.e(TAG, "连接成功");
                    deviceAddress = gatt.getDevice().getAddress();
                    if(connectionStateCallback != null){
                        connectionStateCallback.onConnectionStateChange(status,newState);
                    }


                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    disconnect(address);
                    deviceAddress = "";
                    Log.e(TAG, "断开成功");
                    if(connectionStateCallback != null){
                        connectionStateCallback.onConnectionStateChange(status,newState);
                    }
                }
            }else{
                //如果是收到莫名其妙的断开状态的话 则重连一次
                if (newState != BluetoothProfile.STATE_CONNECTED && canreconntect) {
                    //先断开原有的连接
                    disconnect(address);
                    if(reconnectCount >= 2){
                        //重连三次不成功就失败
                        if(connectionStateCallback != null){
                            connectionStateCallback.onConnectionStateChange(status,newState);
                        }
                        reconnectCount = 0;
                        canreconntect = false;
                        deviceAddress = "";
                        return;
                    }
                    //再次重新连接
                    boolean connect_resule = requestConnect(address,connectionStateCallback,true);
                    reconnectCount++;
                    Log.e(TAG,"正在尝试重新连接："+connect_resule);
                }else{
                    deviceAddress = "";
                    if(connectionStateCallback != null){
                        connectionStateCallback.onConnectionStateChange(status,newState);
                    }
                    disconnect(address);

                }

            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.e(TAG, "onServicesDiscovered()       status:" + status);;
            //  Log.d(TAG, "onServicesDiscovered " + address + " status " + status);
            if (status != BluetoothGatt.GATT_SUCCESS) {
                if(bleReadOrWriteCallback != null){
                    bleReadOrWriteCallback.onDiscoverServicesFail(status);
                }
                return;
            }
            //获取服务列表
            servicesList = getSupportedGattServices(gatt);
            if(bleReadOrWriteCallback != null){
                bleReadOrWriteCallback.onServicesDiscovered(status);
            }
        }

		@Override
        public void onDescriptorWrite(BluetoothGatt gatt,
                                      BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.e(TAG, "onDescriptorWrite()       status:" + status);
        }
		@Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.e(TAG, "onCharacteristicChanged()");
            Log.e(TAG, "onCharacteristicChanged()");
            String address = gatt.getDevice().getAddress();
            long starttime = System.currentTimeMillis();
//            Log.d(TAG, "onCharacteristicChanged " + address);
//            Log.d(TAG, new String(Hex.encodeHex(characteristic.getValue())));
            byte[] bytes = characteristic.getValue();
            if(dataCallback != null){
                dataCallback.onGetData(characteristic.getUuid().toString(),bytes);
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(String.format("%02x", bytes[i]));
                Log.d(TAG," time :" + starttime + "     bytes：" + i + "     =" + Integer.toHexString(bytes[i]));
            }
            System.out.println("出来的数据：" + sb.toString());
            Log.d(TAG, "出来的数据：" + sb.toString());

        }
		@Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);

            Log.e(TAG, "onCharacteristicWrite()");
            String address = gatt.getDevice().getAddress();
            //   XuLog.e(TAG, "onCharacteristicWrite()       status:" + status);
//            Log.d(TAG, "onCharacteristicWrite " + address + " status " + status);
            if (status != BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "onCharacteristicWrite()检测到不可以写数据    status：" + status);

                return;
            }
            Log.e(TAG, "onCharacteristicWrite()检测到可以写数据");
            Log.e(TAG,"写出的数据："+CodeUtil.bytesToHexString(characteristic.getValue()));
//            XuToast.show(AppContext.getInstance(),"写出的数据："+new String(Hex.encodeHex(characteristic.getValue())));
        }
        
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status){
        	System.out.println("读取到返回数据************"+status);
        	// we got response regarding our request to fetch characteristic value
            if (status == BluetoothGatt.GATT_SUCCESS) {
            	// and it success, so we can get the value            	
            	System.out.println("======"+CodeUtil.bytesToHexString(characteristic.getValue()));
            }
        }

    };

    /**
     * 扫描ble设备的callback
     */
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi,
                             byte[] scanRecord) {
            if(resultCallback != null){
                resultCallback.onFindDevice(device,rssi,scanRecord);
            }

        }
    };

    public BleHelper() {

    }
    /**
     *初始化ble操作类
     * @param context:上下文  建议用持久的
     */
	public boolean init(Context context) {
        this.context = context;
        final BluetoothManager bluetoothManager = (BluetoothManager) context
                .getSystemService(Context.BLUETOOTH_SERVICE);
        mBtAdapter = bluetoothManager.getAdapter();
        if (mBtAdapter == null) {
            return false;
        }
        mBluetoothGatts = new HashMap<String, BluetoothGatt>();
        return true;
    }
    /**
     *开启扫描
     * @param resultCallback:扫描结果的calllback
     */
	public boolean startScan(BleScanResultCallback resultCallback) {
        this.resultCallback = resultCallback;
        try {
            return mBtAdapter.startLeScan(mLeScanCallback);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "使用BLE扫描API开始扫描出错了 错误:" + e.getMessage());
            return false;
        }

    }
    /**
     *停止扫描
     */
	public void stopScan() {
        try {
            mBtAdapter.stopLeScan(mLeScanCallback);
            resultCallback = null;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "使用的BLE扫描API停止扫描出错了 错误:" + e.getMessage());
        }
    }
    /**
     *断开连接
     */
    public synchronized void disconnect(String address) {
        if (mBluetoothGatts.containsKey(address)) {
            BluetoothGatt gatt = mBluetoothGatts.remove(address);
            if (gatt != null) {
                gatt.disconnect();
                gatt.close();
                Log.e(TAG, "执行到了设备断开的指令");
            }
        }
    }
    /**
     *发起连接（供外部调用）
     * @param address:目标设备的address地址
     * @param connectionStateCallback:设备连接状态的callback
     * @param canreconntect:是否启用重连机制 true：重连三次 false：不进行重连
     */
	public boolean requestConnect(String address,BleConnectionCallback connectionStateCallback,boolean canreconntect) {
        this.connectionStateCallback = connectionStateCallback;
        this.canreconntect = canreconntect;
        BluetoothGatt gatt = mBluetoothGatts.get(address);
        if (gatt != null && gatt.getServices().size() == 0) {
            return false;
        }
        return connect(address);
    }
    /**
     *连接到设备
     * @param address:目标设备的address地址
     */
	private boolean connect(String address) {
        BluetoothDevice device = mBtAdapter.getRemoteDevice(address);
        BluetoothGatt gatt = device.connectGatt(context, false, mGattCallback);
        //为了防止重复的gatt，连接成功先检查是否有重复的，有则断开
        BluetoothGatt last = mBluetoothGatts.remove(address);
        if(last != null){
            last.disconnect();
            last.close();
        }
        if (gatt == null) {
            mBluetoothGatts.remove(address);
            return false;
        } else {
            // TODO: if state is 141, it can be connected again after about 15
            // 将连接的gatt存起来
            mBluetoothGatts.put(address, gatt);
            return true;
        }
    }
    /**
     *蓝牙适配器是否正常
     */
    public boolean adapterEnabled() {
        if (mBtAdapter != null) {
            return mBtAdapter.isEnabled();
        }
        return false;
    }
    /**
     * gatt的获取服务列表
     */
	public List<BluetoothGattService> getSupportedGattServices(BluetoothGatt mBluetoothGatt) {
        if (mBluetoothGatt == null)
            return null;
        return mBluetoothGatt.getServices();   //此处返回获取到的服务列表
    }
    /**
     * 获取服务（必须先连接成功）
     * @param address：目标设备的address
     */
	public boolean discoverServices(String address,BleReadOrWriteCallback bleReadOrWriteCallback) {
        this.bleReadOrWriteCallback = bleReadOrWriteCallback;
        BluetoothGatt gatt = mBluetoothGatts.get(address);
        if (gatt == null) {
            return false;
        }

        boolean ret = gatt.discoverServices();
        if (!ret) {
            disconnect(address);
        }
        return ret;
    }
    /**
     * 检查指定UUID的服务是否存在（必须先获取服务列表成功）
     */
    public boolean serviceIsExisted(String uuid){
        for (BluetoothGattService sevice : servicesList) {
            if (sevice.getUuid().equals(UUID.fromString(uuid))) {
             return true;
            }
        }
        return false;
    }




    /**
     * 订阅并写描述者  (指定UUID的service下的所有Notification的特征字)
     */
	public boolean characteristicNotification(String uuid){
        BluetoothGatt gatt = mBluetoothGatts.get(deviceAddress);
        if (gatt == null) {
            Log.e(TAG, "BluetoothAdapter not initialized");
            System.out.println(TAG+" BluetoothAdapter not initialized");
            return false;
        }
        BluetoothGattService service = getService(UUID.fromString(uuid));
        if(service == null){
            Log.e(TAG, "service is null");
            System.out.println(TAG+" service is null");
            return false;
        }
        System.out.println(TAG+" 注册消息监听");
        List<BluetoothGattCharacteristic> gattCharacteristics = service.getCharacteristics();
        for (int j = 0; j < gattCharacteristics.size(); j++) {
            BluetoothGattCharacteristic chara = gattCharacteristics.get(j);
            //判断是否有可通知的特征字，有则进行订阅写描述者
            if ( (chara.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                boolean b = gatt.setCharacteristicNotification(chara, true);
                BluetoothGattDescriptor descriptor = chara.getDescriptor(DESC_CCC);
                Log.e(TAG, "订阅结果:" + b + "        characteristic:" + chara.getUuid());
                System.out.println(TAG+"订阅结果:" + b + "        characteristic:" + chara.getUuid());
                System.out.println("descriptor  "+descriptor);
                if (descriptor != null) {
                    byte[] val_set = null;
                    val_set = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE;
                    if (!descriptor.setValue(val_set)) {
                        Log.e(TAG, "descriptor.setValue失败" );
                        System.out.println(TAG+"descriptor.setValue失败");
                        continue;
                    }
                    boolean f = gatt.writeDescriptor(descriptor);
                    Log.e(TAG, "写描述者结果:" + f + "        characteristic:" + chara.getUuid());
                    System.out.println(TAG+"写描述者结果:" + f + "        characteristic:" + chara.getUuid());
                }

            }

        }
        return true;
    }
//    // 可读
//    if ((charaProp & BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
//
//    }
//// 可写，注：要 & 其可写的两个属性
//    if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0
//            || (charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE) > 0) {
//
//    }
//// 可通知，可指示
//    if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0
//            || (charaProp & BluetoothGattCharacteristic.PROPERTY_INDICATE) > 0) {
//
//    }
    /**
     *获取指定uuid的service（必须先连接成功）
     */
	public BluetoothGattService getService( UUID uuid) {
        try {
            if(servicesList == null || servicesList.size() < 1){
                return null;
            }
            BluetoothGattService service = null;
            for(BluetoothGattService temp : servicesList){
                if(temp.getUuid().equals(uuid)){
                    service = temp;
                }
            }
            if (service == null) {
                return null;
            } else {
                return service;
            }
        } catch (Exception e) {
            return null;
        }

    }
    /**
     * 初始化用以写数据的特征字
     * @param serviceUuid： 特征字所在的服务的UUID
     * @param characteristicUuid： 特征字的UUID
     */
	public void initWritetCharacteristic(String serviceUuid,String characteristicUuid){
        mGattCharacteristicWrite = getService( UUID.fromString(serviceUuid)).getCharacteristic(UUID.fromString(characteristicUuid));
        //setWriteType使用WRITE_TYPE_DEFAULT可以在底层自动分包 不用应用层分
        mGattCharacteristicWrite.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
    }
    /**
     * 初始化用以读数据的特征字
     * @param serviceUuid： 特征字所在的服务的UUID
     * @param characteristicUuid： 特征字的UUID
     */
	public void initReadCharacteristic(String serviceUuid,String characteristicUuid){
        mGattCharacteristicRead = getService( UUID.fromString(serviceUuid)).getCharacteristic(UUID.fromString(characteristicUuid));

    }
    /**
     * 发送数据的接口
     * @param str： 数据内容（必须是16进制的）
     */
	public boolean sendByBLE(String str) {
        try {
            if (TextUtils.isEmpty(deviceAddress)) {
//            UIUitls.toast("设备未连接");
                Log.d(TAG, "设备未连接");
                return false;
            }
            if(mGattCharacteristicWrite == null){
                Log.e(TAG,"gattCharacteristic为空");
                return false;
            }
            byte[] data = CodeUtil.strtoByteArray(str);
      
            mGattCharacteristicWrite.setValue(data);
            boolean writeresult = AppContext.getInstance().getmBle().requestWriteCharacteristic(deviceAddress);
            Log.e(TAG,"writeresult:"+writeresult+"     data:"+str);
            return  writeresult;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     *写数据
     * @param address：目标设备的address
     */
    public boolean requestWriteCharacteristic(String address) {
        BluetoothGatt gatt = mBluetoothGatts.get(address);
        if (gatt == null || mGattCharacteristicWrite == null) {
            if (gatt == null) {
                Log.e(TAG, "发送BLE数据失败:gatt = null");
                System.out.println(TAG+"发送BLE数据失败:gatt = null");
            }

            if (mGattCharacteristicWrite == null) {
                Log.e(TAG, "发送BLE数据失败:characteristic = null");
                System.out.println(TAG+"发送BLE数据失败:characteristic = null");
            }
            return false;
        }

        Log.d(TAG, "data：" + CodeUtil.bytesToString(mGattCharacteristicWrite.getValue()));
        boolean result = false;
        try {
            result = gatt.writeCharacteristic(mGattCharacteristicWrite);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return result;
    }


    public boolean readResulr() {
        try {
            if (TextUtils.isEmpty(deviceAddress)) {
//            UIUitls.toast("设备未连接");
                Log.d(TAG, "设备未连接");
                return false;
            }
            if(mGattCharacteristicRead == null){
                Log.e(TAG,"gattCharacteristic为空");
                return false;
            }
            boolean readresult = AppContext.getInstance().getmBle().readCharacteristic(deviceAddress);
            Log.e(TAG,"readresult:"+readresult);
            return  readresult;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean readCharacteristic(String address){
         boolean result = false;
    	 BluetoothGatt gatt = mBluetoothGatts.get(address);
    	 if (gatt == null || mGattCharacteristicRead == null) {
             if (gatt == null) {
                 System.out.println(TAG+"读取BLE数据失败:gatt = null");
             }

             if (mGattCharacteristicRead == null) {
                 Log.i(TAG,"读取BLE数据失败:characteristic = null");
             }
         }else{
        	Log.i(TAG,"读取特征值数据");
    	    try {
                result = gatt.readCharacteristic(mGattCharacteristicRead);
            } catch (Exception e) {
                e.printStackTrace();
            }
        	 
         }
         return result;
    }

    
    
    public List<BluetoothGattService> getServicesList() {
        return servicesList;
    }

    public void setServicesList(List<BluetoothGattService> servicesList) {
        this.servicesList = servicesList;
    }

    public GetDataCallback getDataCallback() {
        return dataCallback;
    }

    public void setDataCallback(GetDataCallback dataCallback) {
        this.dataCallback = dataCallback;
    }
}
