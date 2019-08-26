package com.northmeter.meshble.northmeter.I;

/**
 * Created by dyd on 2018/4/26.
 */

public interface IShowMainMessage {

    /**发送蓝牙数据时回调*/
    public void showBlueToastMessage(String data);

    public void showBlueFailMessage(String data);
}
