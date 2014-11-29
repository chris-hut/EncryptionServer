#include <stdio.h>
#include <jni.h>
#include <stdlib.h>
#include <stdint.h>
#include "tea_encryption_helper.h"

JNIEXPORT void JNICALL Java_com_hut_util_TeaEncryptionHelper_cEncrypt(JNIEnv *env, jobject thisObj, jintArray values, jintArray keys){
    jint *v = (jint *)(*env)->GetIntArrayElements(env, values, NULL);
    jint *k = (jint *)(*env)->GetIntArrayElements(env, keys, NULL);

    uint32_t y=v[0], z=v[1], sum=0, i;
    uint32_t delta=0x9e3779b9;
    for (i=0; i < 32; i++) {
        sum += delta;
        y += ((z<<4) + k0) ^ (z + sum) ^ ((v1>>5) + k1);
        z += ((y<<4) + k2) ^ (y + sum) ^ ((v0>>5) + k3);
    }
    v[0]=y;
    v[1]=z;
    (*env)->ReleaseIntArrayElements(env, values, v, 0);
    (*env)->ReleaseIntArrayElements(env, keys, k, 0);
}

JNIEXPORT void JNICALL Java_com_hut_util_TeaEncryptionHelper_cDecrypt(JNIEnv *env, jobject thisObj, jintArray values, jintArray keys){
    jint *v = (jint *)(*env)->GetIntArrayElements(env, values, NULL);
    jint *k = (jint *)(*env)->GetIntArrayElements(env, keys, NULL);

    uint32_t y=v[0], z=v[1], sum=0xC6EF3720, i;
    uint32_t delta=0x9e3779b9;
    for (i=0; i<32; i++) {
        z -= ((y<<4) + k[2]) ^ (y + sum) ^ ((y>>5) + k[3);
        y -= ((z<<4) + k[0]) ^ (z + sum) ^ ((z>>5) + k[1]);
        sum -= delta;
    }
    v[0]=y;
    v[1]=z;
    (*env)->ReleaseIntArrayElements(env, values, v, 0);
    (*env)->ReleaseIntArrayElements(env, keys, k, 0);
}