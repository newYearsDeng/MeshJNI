package com.northmeter.meshble.bledemo;

import android.bluetooth.BluetoothDevice;

/**
 * Created by 601042 on 2017/5/25.
 */
public interface BleScanResultCallback {
    void onSuccess();
    void onFail();
    void onFindDevice(BluetoothDevice device, int rssi,
                      byte[] scanRecord);
}
