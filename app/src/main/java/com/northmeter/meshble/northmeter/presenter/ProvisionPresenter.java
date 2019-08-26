package com.northmeter.meshble.northmeter.presenter;

import com.northmeter.meshble.jnjutils.JniUtilsECDH;
import com.northmeter.meshble.northmeter.I.IProvisionPresenter;

/**
 * Created by dyd on 2018/4/26.
 * 邀请客户端
 * 1.调用ecdh里面的makeKey方法计算出来公钥publicKey和私钥privateKey
 * 2.交换公钥publicKe
 * 3.调用sharedSecret方法，交换回来的公钥+自己的私钥计算confirmation
 * 4.发送confirmation以及接受confirmation进行校验
 * AuthenticationValue：蓝牙设备上的随机数，使用键盘输入或者二维码扫描的形式；
 * Random不能直接使用蓝牙发送；
 */

public class ProvisionPresenter implements IProvisionPresenter {
    JniUtilsECDH jniUtilsECDH = new JniUtilsECDH();

    //获得publicKey和privateKey
    public byte[] getKey(){
        byte[] makeKey = jniUtilsECDH.makeKey();//publicKey和privateKey，前32位为publicKey，后面的是privateKey；
        return makeKey;
    }
    //用shareKey把自己的私钥和获得的公钥计算SecretKey
    public byte[] ShareKey(byte[] privateKey ,byte[] publicKey){
        byte[] secretKey = jniUtilsECDH.sharedSecret(publicKey,privateKey);
        return secretKey;
    }

}
