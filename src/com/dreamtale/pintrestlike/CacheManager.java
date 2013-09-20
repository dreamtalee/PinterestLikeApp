package com.dreamtale.pintrestlike;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

public class CacheManager
{
    private final static String LOG_TAG = "CacheManager";
    
    private final int maxMemory = (int)Runtime.getRuntime().maxMemory();
    
    private final int cacheSize = UIConfig.MODE_GRID_VIEW == UIConfig.getInstance().getUIMode() ? maxMemory / 30 : maxMemory / 5;
    
    private LruCache<String, Bitmap> memoryCache = null;
    
    public static CacheManager manager = null;
    
    public static CacheManager getInstance()
    {
        if (null == manager)
        {
            init();
        }
        return manager;
    }
    
    private static synchronized void init()
    {
        if (null == manager)
        {
            manager = new CacheManager();
        }
    }
    
    private CacheManager()
    {
        memoryCache = new LruCache<String, Bitmap>(cacheSize)
        {

            @Override
            public void trimToSize(int maxSize)
            {
                super.trimToSize(maxSize);
            }

            @Override
            protected void entryRemoved(boolean evicted, String key,
                    Bitmap oldValue, Bitmap newValue)
            {
                super.entryRemoved(evicted, key, oldValue, newValue);
            }

            @Override
            protected Bitmap create(String key)
            {
                return super.create(key);
            }

            @Override
            protected int sizeOf(String key, Bitmap value)
            {
                return value.getByteCount();
            }
            
        };
    }
    
    public Bitmap getBitmap(String key)
    {
        return getBitmapFromMemoryCache(key);
    }
    
    public void addBitmap2MemoryCache(String key, Bitmap value)
    {
        if (null == getBitmapFromMemoryCache(key))
        {
            Log.d(LOG_TAG, "addBitmap2MemoryCache");
            memoryCache.put(key, value);
        }
    }
    
    public Bitmap getBitmapFromMemoryCache(String key)
    {
        Bitmap bitmap = memoryCache.get(key);
        if (null != bitmap)
        {
            Log.d(LOG_TAG, "memory cache hit");
        }
        return bitmap;
    }
    
    public void clearCache()
    {
        memoryCache.evictAll();
    }
}
