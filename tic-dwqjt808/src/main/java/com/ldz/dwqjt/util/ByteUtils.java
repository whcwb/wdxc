package com.ldz.dwqjt.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ByteUtils {

    public static byte[] handle(byte[] src){
        byte[] res = new byte[src.length];
        for (int i = 0; i < src.length; i++) {
            byte b = src[i];
            if (b < 0){
                int t = (int)b;
                t = 256 + t;
                res[i] = (byte)t;
            }else{
                res[i] = b;
            }
        }
        return res;
    }
    public static byte[] parse(byte[] src,  int srcPos,int length){
        ByteArrayInputStream bis = new ByteArrayInputStream(src);
        int temp = 0;
        int position = 0;
        byte[] res = new byte[length];
        int bi = 0;
        int end  = position + length;
        while((temp = bis.read())!=-1){
            if (position >= srcPos && position < end){
                res[bi++] = (byte) temp;
            }
            position ++;
        }
        try {
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
