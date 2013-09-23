package com.dreamtale.pintrestlike.utils;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageUtils
{
    private final static String LOG_TAG = "ImageUtils";
    
    public static Bitmap decodeBitmap(Resources res, int id, float reqWidth, float reqHeight)
    {
        BitmapFactory.Options ops = new BitmapFactory.Options();
        ops.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, id, ops);
        
        ops.inSampleSize = calculateSampleSize(ops, reqWidth, reqHeight);
        ops.inJustDecodeBounds = false;
        
        return BitmapFactory.decodeResource(res, id, ops);
    }
    
    public static Bitmap decodeBitmap(InputStream is, float reqWidth)
    {
        BitmapFactory.Options ops = new BitmapFactory.Options();
        ops.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, ops);
        
        try
        {
            is.reset();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        ops.inSampleSize = calculateSampleSize(ops, reqWidth, reqWidth / ops.outWidth * ops.outHeight);
        ops.inSampleSize = 2;
        ops.inJustDecodeBounds = false;
        
        return BitmapFactory.decodeStream(is, null, ops);
    }
    
    public static Bitmap decodeBitmap(byte[] array, float reqWidth)
    {
        BitmapFactory.Options ops = new BitmapFactory.Options();
        ops.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(array, 0, array.length, ops);
        
//        try
//        {
//            is.reset();
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }
        
        ops.inSampleSize = calculateSampleSize(ops, reqWidth, reqWidth / ops.outWidth * ops.outHeight);
        ops.inSampleSize = 2;
        ops.inJustDecodeBounds = false;
        
        return BitmapFactory.decodeByteArray(array, 0, array.length, ops);
    }
    
    public static int calculateSampleSize(BitmapFactory.Options ops, float reqWidth, float reqHeight)
    {
        float width = ops.outWidth;
        float height = ops.outHeight;
        int sampleSize = 1;
        
        if (width > reqWidth || height > reqHeight)
        {
            int widthRatio = Math.round(width / reqWidth);
            int heightRation = Math.round(height / reqHeight);
            sampleSize = widthRatio < heightRation ? widthRatio : heightRation;
        }
        Log.d(LOG_TAG, "the sample size is " + sampleSize);
        return  sampleSize;
    }
}
