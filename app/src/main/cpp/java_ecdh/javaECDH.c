#include <jni.h>
#include <stdint.h>
#include "com_northmeter_meshble_jnjutils_JniUtilsECDH.h"
#include "uECC.h"


/**
 * 引用标签第一个是.h的文件名家后缀，方法名一定要和.h文件中的方法名称一样
 */

JNIEXPORT jstring Java_com_northmeter_meshble_jnjutils_JniUtilsECDH_getString
  (JNIEnv *env, jobject obj)
  {
     return (*env)->NewStringUTF(env, "这是我测试的jni");
  }

JNIEXPORT jint JNICALL Java_com_northmeter_meshble_jnjutils_JniUtilsECDH_add
        (JNIEnv *env, jclass jc, jint a, jint b)
{
    return a+b;
}

/*
 * Class:     javaECDH
 * Method:    uECC_makemake_key
 * Signature: ([B[B)I
 */
JNIEXPORT jbyteArray JNICALL Java_com_northmeter_meshble_jnjutils_JniUtilsECDH_makeKey
        (JNIEnv *env, jclass jc)
{
    int sizeByte = 64+32;

    jbyteArray data = (*env)->NewByteArray(env, sizeByte);
    if (data == NULL) {
        return NULL; //  out of memory error thrown
    }

    // creat bytes from byte
    jbyte *bytes = (*env)->GetByteArrayElements(env, data, 0);

    uint8_t keys[64+32];
    if (!uECC_make_key(keys, keys+32, uECC_secp256r1())) {
        //printf("error:create publick kye and private key failed\n");
        return NULL;
    }
    for (int i = 0; i < sizeByte; i++) {
        bytes[i] = keys[i];
    }

    // move from the temp structure to the java structure
    (*env)->SetByteArrayRegion(env, data, 0, sizeByte, bytes);

    return data;
}

/*
 * Class:     javaECDH
 * Method:    uECC_shared_secret
 * Signature: ([B[B[B)I
 */
JNIEXPORT jbyteArray JNICALL Java_com_northmeter_meshble_jnjutils_JniUtilsECDH_sharedSecret
        (JNIEnv *env, jclass jc, jbyteArray pub, jbyteArray pri)
{
    int sizeByte = 32;

    uint8_t publickey[64], privatekey[32];


    if (!uECC_shared_secret(publicKey, privateKey, secretKey, uECC_secp256r1())) {
        //printf("error:generate secret key failed\n");
        return NULL;
    }

    jbyteArray secret = (*env)->NewByteArray(env, sizeByte);
    if (secret == NULL) {
        return NULL; //  out of memory error thrown
    }uint8_t* publicKey  = (uint8_t*)(*env)->GetByteArrayElements(env, pub, 0);
    uint8_t* privateKey = (uint8_t*)(*env)->GetByteArrayElements(env, pri, 0);
    uint8_t secretKey[32] = {0};
    jbyte *bytes = (*env)->GetByteArrayElements(env, secret, 0);
    for (int i = 0; i < sizeByte; i++) {
        bytes[i] = secretKey[i];
    }
    (*env)->SetByteArrayRegion(env, secret, 0, sizeByte, bytes);
    return secret;
}
