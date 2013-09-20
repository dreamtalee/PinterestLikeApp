package com.dreamtale.pintrestlike;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;

public class PinterestScrollView extends ScrollView
{
    private final static String LOG_TAG = "PinterestScrollView";
    
    public PinterestScrollView(Context context)
    {
        this(context, null);
    }

    public PinterestScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public PinterestScrollView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt)
    {
        Log.d(LOG_TAG, "onScrollChanged y " + t + " old y " + oldt);
        super.onScrollChanged(l, t, oldl, oldt);
    }
    
    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
            boolean clampedY)
    {
        Log.d(LOG_TAG, " scroll y " + scrollY + " clampedy " + clampedY);
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }
}
