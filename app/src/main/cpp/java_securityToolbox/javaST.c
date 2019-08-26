#include <jni.h>
#include "com_northmeter_meshble_jnjutils_JniUtilsST.h"
#include <string.h>
#include <stdint.h>
#include "base/aes128_ccm.h"
#include "base/aes128_cmac.h"
#include "toolbox/security_kn.h"

JNIEXPORT jbyteArray JNICALL Java_com_northmeter_meshble_jnjutils_JniUtilsST_securityAesCMAC
  (JNIEnv *env, jclass jc, jbyteArray key0, jbyteArray msg0)
  {
    uint8_t* key = (uint8_t*)(*env)->GetByteArrayElements(env, key0, 0);
    uint8_t* msg = (uint8_t*)(*env)->GetByteArrayElements(env, msg0, 0);
    int len_msg  = (*env)->GetArrayLength(env, msg0);
    uint8_t mac[16];
    int len = security_AES_CMAC(key, len_msg, msg, mac);

    jbyteArray mac_ret = (*env)->NewByteArray(env, len);
    if (mac_ret == NULL) {
        return NULL; //  out of memory error thrown
    }
    jbyte *bytes = (*env)->GetByteArrayElements(env, mac_ret, 0);
    for (int i = 0; i < len; i++) {
        bytes[i] = mac[i];
    }
    (*env)->SetByteArrayRegion(env, mac_ret, 0, len, bytes);
    return mac_ret;
  }

/*
 * Class:     javaST
 * Method:    ST_s1_salt_generation
 * Signature: ([B[B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_northmeter_meshble_jnjutils_JniUtilsST_securityKSaltGeneration
  (JNIEnv *env, jclass jc, jbyteArray m0)
  {
    uint8_t* m  = (uint8_t*)(*env)->GetByteArrayElements(env, m0, 0);
    int len_m   = (*env)->GetArrayLength(env, m0);
    uint8_t salt[16];
    int len = security_s1_salt_generation(m, len_m, salt);

    jbyteArray ret = (*env)->NewByteArray(env, len);
    if (ret == NULL) {
        return NULL; //  out of memory error thrown
    }
    jbyte *bytes = (*env)->GetByteArrayElements(env, ret, 0);
    for (int i = 0; i < len; i++) {
        bytes[i] = salt[i];
    }
    (*env)->SetByteArrayRegion(env, ret, 0, len, bytes);
    return ret;
  }

/*
 * Class:     javaST
 * Method:    ST_k1_derivation
 * Signature: ([B[B[B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_northmeter_meshble_jnjutils_JniUtilsST_securityK1derivation
  (JNIEnv *env, jclass jc, jbyteArray n0, jbyteArray p0, jbyteArray s0)
  {
    uint8_t* n = (uint8_t*)(*env)->GetByteArrayElements(env, n0, 0);
    int len_n  = (*env)->GetArrayLength(env, n0);
    uint8_t* p = (uint8_t*)(*env)->GetByteArrayElements(env, p0, 0);
    int len_p  = (*env)->GetArrayLength(env, p0);
    uint8_t* s = (uint8_t*)(*env)->GetByteArrayElements(env, s0, 0);
    k1_result_t k1;
    int len = 16;
    security_k1_derivation(n, len_n, p, len_p, s, &k1);

    jbyteArray ret = (*env)->NewByteArray(env, len);
    if (ret == NULL) {
        return NULL; //  out of memory error thrown
    }
    jbyte *bytes = (*env)->GetByteArrayElements(env, ret, 0);
    for (int i = 0; i < len; i++) {
        bytes[i] = k1.k1[i];
    }
    (*env)->SetByteArrayRegion(env, ret, 0, len, bytes);
    return ret;
  }

/*
 * Class:     javaST
 * Method:    ST_k2_derivation
 * Signature: ([B[B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_northmeter_meshble_jnjutils_JniUtilsST_securityK2derivation
  (JNIEnv *env, jclass jc, jbyteArray n0, jbyteArray p0)
  {
    uint8_t* n = (uint8_t*)(*env)->GetByteArrayElements(env, n0, 0);
    uint8_t* p = (uint8_t*)(*env)->GetByteArrayElements(env, p0, 0);
    int len_p  = (*env)->GetArrayLength(env, p0);
    k2_result_t k2;
    int len = 33;
    security_k2_derivation(n, p, len_p, &k2);

    jbyteArray ret = (*env)->NewByteArray(env, len);
    if (ret == NULL) {
        return NULL; //  out of memory error thrown
    }
    jbyte *bytes = (*env)->GetByteArrayElements(env, ret, 0);
    for (int i = 0; i < len; i++) {
        bytes[i] = ((uint8_t*)&k2)[i];
    }
    (*env)->SetByteArrayRegion(env, ret, 0, len, bytes);
    return ret;
  }

/*
 * Class:     javaST
 * Method:    ST_k3_derivation
 * Signature: ([B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_northmeter_meshble_jnjutils_JniUtilsST_securityK3derivation
  (JNIEnv *env, jclass jc, jbyteArray n0)
  {
    uint8_t* n = (uint8_t*)(*env)->GetByteArrayElements(env, n0, 0);
    k3_result_t k3;
    int len = 8;
    security_k3_derivation(n, &k3);

    jbyteArray ret = (*env)->NewByteArray(env, len);
    if (ret == NULL) {
        return NULL; //  out of memory error thrown
    }
    jbyte *bytes = (*env)->GetByteArrayElements(env, ret, 0);
    for (int i = 0; i < len; i++) {
        bytes[i] = k3.network_id[i];
    }
    (*env)->SetByteArrayRegion(env, ret, 0, len, bytes);
    return ret;
  }

/*
 * Class:     javaST
 * Method:    ST_k4_derivation
 * Signature: ([B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_northmeter_meshble_jnjutils_JniUtilsST_securityK4derivation
  (JNIEnv * env, jclass jc, jbyteArray n0)
  {
    uint8_t* n = (uint8_t*)(*env)->GetByteArrayElements(env, n0, 0);
    k4_result_t k4;
    int len = 1;
    security_k4_derivation(n, &k4);

    jbyteArray ret = (*env)->NewByteArray(env, len);
    if (ret == NULL) {
        return NULL; //  out of memory error thrown
    }
    jbyte *bytes = (*env)->GetByteArrayElements(env, ret, 0);
    bytes[0] = k4;
    (*env)->SetByteArrayRegion(env, ret, 0, len, bytes);
    return ret;
  }

/*
 * Class:     javaST
 * Method:    ST_aes_ccm_encrypt2
 * Signature: ([B[B[BBB)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_northmeter_meshble_jnjutils_JniUtilsST_securityAesCcmEncrypt2
  (JNIEnv *env, jclass jc, jbyteArray key0, jbyteArray Nonce0, jbyteArray dat0, jbyte a_dat0, jbyte mic0)
  {
    uint8_t* key   = (uint8_t*)(*env)->GetByteArrayElements(env, key0, 0);
    uint8_t* Nonce = (uint8_t*)(*env)->GetByteArrayElements(env, Nonce0, 0);
    uint8_t* dat   = (uint8_t*)(*env)->GetByteArrayElements(env, dat0, 0);
    int len_dat    = (*env)->GetArrayLength(env, dat0);
    uint8_t a_dat  = a_dat0;
    uint8_t mic    = mic0;

    security_aes_ccm_encrypt2(key, Nonce, dat, len_dat, a_dat, mic);
    
    jbyteArray ret = (*env)->NewByteArray(env, len_dat);
    if (ret == NULL) {
        return NULL; //  out of memory error thrown
    }
    jbyte *bytes = (*env)->GetByteArrayElements(env, ret, 0);
    for (int i=0; i<len_dat; i++) {
        bytes[i] = dat[i];
    }
    (*env)->SetByteArrayRegion(env, ret, 0, len_dat, bytes);
    return ret;
  }

/*
 * Class:     javaST
 * Method:    ST_aes_ccm_decrypt2
 * Signature: ([B[B[BBB)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_northmeter_meshble_jnjutils_JniUtilsST_securityAesCcmDecrypt2
  (JNIEnv *env, jclass jc, jbyteArray key0, jbyteArray Nonce0, jbyteArray dat0, jbyte a_dat0, jbyte mic0)
  {
    uint8_t* key   = (uint8_t*)(*env)->GetByteArrayElements(env, key0, 0);
    uint8_t* Nonce = (uint8_t*)(*env)->GetByteArrayElements(env, Nonce0, 0);
    uint8_t* dat   = (uint8_t*)(*env)->GetByteArrayElements(env, dat0, 0);
    int len_dat    = (*env)->GetArrayLength(env, dat0);
    uint8_t a_dat  = a_dat0;
    uint8_t mic    = mic0;

    security_aes_ccm_decrypt2(key, Nonce, dat, len_dat, a_dat, mic);
    
    jbyteArray ret = (*env)->NewByteArray(env, len_dat);
    if (ret == NULL) {
        return NULL; //  out of memory error thrown
    }
    jbyte *bytes = (*env)->GetByteArrayElements(env, ret, 0);
    for (int i=0; i<len_dat; i++) {
        bytes[i] = dat[i];
    }
    (*env)->SetByteArrayRegion(env, ret, 0, len_dat, bytes);
    return ret;
  }
