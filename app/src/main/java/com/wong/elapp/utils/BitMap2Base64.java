package com.wong.elapp.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * 一个将bitMap和base64相互转换的工具类
 */
public class BitMap2Base64 {
    /**
     * bitmap转换成base64
     * @param bitmap
     * @param bitmapQuality
     * @return
     */
    public static String bitmaptoString(Bitmap bitmap,int bitmapQuality){
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,bitmapQuality,bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT );
        return string;
    }

    /**
     * 将base64转化为bitmap
     * @param string
     * @return
     */
    public static Bitmap stringtoBitmap(String string){
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        }catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }
}
