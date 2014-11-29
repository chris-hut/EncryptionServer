package com.hut.util;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by chris on 11/26/14.
 */
public class TeaEncryptionHelper {

    private int[] key;

    public TeaEncryptionHelper(String key){
        this(key.getBytes());
    }

    public TeaEncryptionHelper(byte[] key){
        System.out.println(System.getProperty("java.library.path"));
        System.loadLibrary("tea_encryption_helper");

        // convert key into int key
        this.key = new int[4];
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            ByteBuffer bb = ByteBuffer.wrap(md.digest(key));
            for(int i = 0; i < this.key.length; i++){
                this.key[i] = bb.getInt();
            }

        }catch(NoSuchAlgorithmException e){
            System.out.println("No algorithm here" + e.getMessage());
        }

    }

    public byte[] encrypt(String input){
        return encrypt(input.getBytes());
    }

    public byte[] encrypt(byte[] input){
        // pad input
        int size = (input.length/4) + (input.length % 4);
        byte[] padded = new byte[size*4];
        ByteBuffer bb = ByteBuffer.wrap(padded);
        bb.put(input);

        IntBuffer ib = ByteBuffer.wrap(padded).asIntBuffer();

        int[] buf = new int[size];
        for(int i = 0; i < buf.length; i++){
            buf[i] = ib.get();
        }


        // Cycle two ints in array
        int[] cipherText  = new int[2];
        int[] fullCipher;
        if(buf.length % 2 > 0){
            fullCipher = new int[buf.length + 1];
        }else {
            fullCipher = new int[buf.length];
        }

        IntBuffer anotherIB = IntBuffer.wrap(fullCipher);

        for(int i = 0; i < buf.length; i +=2){
            cipherText[0] = buf[i];
            if(i < buf.length-1){
                cipherText[1] = buf[i+1];
            }else{
                cipherText[1] = 0;
            }

            cEncrypt(cipherText, key);
            anotherIB.put(cipherText);
        }

        ByteBuffer result = ByteBuffer.allocate(fullCipher.length*4);
        IntBuffer intBuffer = result.asIntBuffer();
        intBuffer.put(fullCipher);
        return result.array();
    }

    public byte[] decrypt(String input){
        return decrypt(input.getBytes());
    }

    public byte[] decrypt(byte[] input){
        IntBuffer ib = ByteBuffer.wrap(input).asIntBuffer();
        int[] cipherText = new int[ib.remaining()];
        ib.get(cipherText);

        int[] ptf = new int[2];
        int[] pt = new int[cipherText.length];
        IntBuffer anotherIB = IntBuffer.wrap(pt);

        for(int i = 0; i < cipherText.length; i += 2){
            ptf[0] = cipherText[i];
            ptf[1] = cipherText[i+1];
            cDecrypt(ptf, key);
            anotherIB.put(ptf);
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(pt.length * 4);
        IntBuffer thirdIntBuffer = byteBuffer.asIntBuffer();
        thirdIntBuffer.put(pt);
        return byteBuffer.array();
    }

    public String decryptString(byte[] input){
        return new String(decrypt(input)).trim();
    }

    public String decryptString(String input){
        return decryptString(input.getBytes());
    }

    public native void cEncrypt(int[] v, int[] k);

    public native void cDecrypt(int[] v, int[] k);
}
