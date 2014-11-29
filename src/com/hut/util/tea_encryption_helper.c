#include <stdio.h>
#include <jni.h>
#include <stdlib.h>
#include <stdint.h>
#include "com_hut_util_TeaEncryptionHelper.h"

// Algorithm from: http://en.wikipedia.org/wiki/Tiny_Encryption_Algorithm#Reference_code

JNIEXPORT void JNICALL Java_com_hut_util_TeaEncryptionHelper_cEncrypt(JNIEnv *env, jobject thisObj, jintArray values, jintArray keys){
    jint *v = (jint *)(*env)->GetIntArrayElements(env, values, NULL);
    jint *k = (jint *)(*env)->GetIntArrayElements(env, keys, NULL);

       uint32_t v0=v[0], v1=v[1], sum=0, i;           /* set up */
       uint32_t delta=0x9e3779b9;                     /* a key schedule constant */
       uint32_t k0=k[0], k1=k[1], k2=k[2], k3=k[3];   /* cache key */
       for (i=0; i < 32; i++) {                       /* basic cycle start */
           sum += delta;
           v0 += ((v1<<4) + k0) ^ (v1 + sum) ^ ((v1>>5) + k1);
           v1 += ((v0<<4) + k2) ^ (v0 + sum) ^ ((v0>>5) + k3);
       }                                              /* end cycle */
       v[0]=v0; v[1]=v1;
    (*env)->ReleaseIntArrayElements(env, values, v, 0);
    (*env)->ReleaseIntArrayElements(env, keys, k, 0);
}

JNIEXPORT void JNICALL Java_com_hut_util_TeaEncryptionHelper_cDecrypt(JNIEnv *env, jobject thisObj, jintArray values, jintArray keys){
    jint *v = (jint *)(*env)->GetIntArrayElements(env, values, NULL);
    jint *k = (jint *)(*env)->GetIntArrayElements(env, keys, NULL);

    uint32_t v0=v[0], v1=v[1], sum=0xC6EF3720, i;  /* set up */
    uint32_t delta=0x9e3779b9;                     /* a key schedule constant */
    uint32_t k0=k[0], k1=k[1], k2=k[2], k3=k[3];   /* cache key */
    for (i=0; i<32; i++) {                         /* basic cycle start */
        v1 -= ((v0<<4) + k2) ^ (v0 + sum) ^ ((v0>>5) + k3);
        v0 -= ((v1<<4) + k0) ^ (v1 + sum) ^ ((v1>>5) + k1);
        sum -= delta;
    }                                              /* end cycle */
    v[0]=v0; v[1]=v1;
    (*env)->ReleaseIntArrayElements(env, values, v, 0);
    (*env)->ReleaseIntArrayElements(env, keys, k, 0);
}