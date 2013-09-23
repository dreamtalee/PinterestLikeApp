package com.dreamtale.pintrestlike.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.http.conn.ConnectTimeoutException;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dreamtale.pintrestlike.widget.ItemView;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap>
{
    private final static String LOG_TAG = "ImageDownloader";
    
    private ItemView imageView = null;
    
    private String downloadPath;
    
    public ImageDownloader(ItemView imageView)
    {
        this.imageView = imageView;
        downloadPath = (String)imageView.getTag();
    }
    
    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }
    
    @Override
    protected Bitmap doInBackground(String... params)
    {
        HttpURLConnection connection = null;
        try
        {
            Log.d(LOG_TAG, "The download url is " + downloadPath);
            URL url = new URL(downloadPath);
            connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            if (isCancelled())
            {
                Log.d(LOG_TAG, url + " cancled successfully");
                return null;
            }
            int code = connection.getResponseCode();
            if (isCancelled())
            {
                Log.d(LOG_TAG, url + " cancled successfully");
                return null;
            }
            Log.d(LOG_TAG, "The response code is " + code);
            if (code == HttpURLConnection.HTTP_OK)
            {
                BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int count = 0;
                while ((count = is.read(buffer)) != -1)
                {
                    if (isCancelled())
                    {
                        Log.d(LOG_TAG, url + " cancled successfully");
                        return null;
                    }
                    baos.write(buffer, 0, count);
                }
                Bitmap bitmap = ImageUtils.decodeBitmap(baos.toByteArray(), imageView.getMeasuredWidth());
                Log.d(LOG_TAG, "The download bitmap is " + bitmap);
                baos.close();
                if (isCancelled())
                {
                    Log.d(LOG_TAG, url + " cancled successfully");
                    return null;
                }
                return bitmap;
            }
        }
        catch (ConnectTimeoutException e)
        {
            e.printStackTrace();
            Log.d(LOG_TAG, "cause ConnectTimeoutException " + e.toString());
        }
        catch (SocketTimeoutException e)
        {
            e.printStackTrace();
            Log.d(LOG_TAG, "cause SocketTimeoutException " + e.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d(LOG_TAG, "cause Exception " + e.toString());
        }
        finally
        {
            if (null != connection)
            {
                connection.disconnect();
            }
        }
        return null;
    }
    
    @Override
    protected void onPostExecute(Bitmap result)
    {
        if (!imageView.getTag().equals(downloadPath))
        {
            Log.d(LOG_TAG, "The onPostExecute url downloadPath " + downloadPath + " ------------ return -------------");
            return;
        }
        
        if (null != result)
        {
            CacheManager.getInstance().addBitmap2MemoryCache(downloadPath, result);

            imageView.setImageBitmap(result);
        }
        else
        {
            Toast.makeText(imageView.getContext(), "image download failed", Toast.LENGTH_SHORT).show();
        }
    }
}
