package com.northmeter.meshble.jnjutils;

/**
 * Created by dyd on 2018/4/23.
 */

public class JniUtilsST {
    static {
        System.loadLibrary("javaST");
    }

    public native byte[] securityAesCMAC(byte[] key0,byte[] msg0);

    public native byte[] securityKSaltGeneration(byte[] m0);

    public native byte[] securityK1derivation( byte[] n0, byte[] p0, byte[] s0);

    public native byte[] securityK2derivation( byte[] n0, byte[] p0);

    public native byte[] securityK3derivation( byte[] n0);

    public native byte[] securityK4derivation( byte[] n0);

    public native byte[] securityAesCcmEncrypt2( byte[] key0, byte[] Nonce0, byte[] dat0, byte a_dat0, byte mic0);

    public native byte[] securityAesCcmDecrypt2( byte[] key0, byte[] Nonce0, byte[] dat0, byte a_dat0, byte mic0);

    
}
