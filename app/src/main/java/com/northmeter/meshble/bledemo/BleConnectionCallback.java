package com.northmeter.meshble.bledemo;

/**
 * Created by 601042 on 2017/5/25.
 */
public interface BleConnectionCallback {
    void onConnectionStateChange(int status, int newState);
    void onFail(int errorCode);
}
