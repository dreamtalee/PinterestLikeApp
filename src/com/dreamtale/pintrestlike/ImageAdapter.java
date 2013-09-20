package com.dreamtale.pintrestlike;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

public class ImageAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<ImageInfo> dataList = new ArrayList<ImageInfo>();
    private Bitmap defaultBitmap = null;
    private float density = 0.0f;
    
    public ImageAdapter(Context context, ArrayList<ImageInfo> dataList)
    {
        this.context  = context;
        this.dataList = dataList;
        this.density = context.getResources().getDisplayMetrics().density;
        this.defaultBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty_photo);
    }
    
    public void setDataList(ArrayList<ImageInfo> list)
    {
        this.dataList = list;
    }
    
    @Override
    public int getCount()
    {
        return null != dataList ? dataList.size() : 0;
    }

    @Override
    public Object getItem(int position)
    {
        return null != dataList ? dataList.get(position) : null;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ItemView itemView = null;
        if (null == convertView)
        {
            itemView = new ItemView(context);
        }
        else
        {
            itemView = (ItemView)convertView;
        }
        ImageInfo info = dataList.get(position);

        final int itemWidth = (int) ((parent.getMeasuredWidth() - 10 * density) / 2);
        int itemHeight = 0;
        if (UIConfig.MODE_GRID_VIEW == UIConfig.getInstance().getUIMode())
        {
            itemHeight = itemWidth;
        }
        else
        {
            itemHeight = (int)(((float)info.getWidth() / (float)info.getHeight()) * (float)itemWidth);
        }
        AbsListView.LayoutParams params = (AbsListView.LayoutParams)itemView.getLayoutParams();
        if (null == params)
        {
            params = new AbsListView.LayoutParams(itemWidth, itemHeight);
            itemView.setLayoutParams(params);
        }
        else
        {
            params.width = itemWidth;
            params.height = itemHeight;
        }
        itemView.setBackgroundResource(R.drawable.background);
        itemView.setImageBitmap(defaultBitmap);
        itemView.setTag(info.getThumbUrl());
        itemView.setImageInfo(info);
        Log.d("ImageDownloader", "Set tag url is " + info.getThumbUrl());
        
        return itemView;
    }

}
