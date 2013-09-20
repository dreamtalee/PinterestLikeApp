package com.dreamtale.pintrestlike;

import java.util.ArrayList;

public class ImageInfoProvider
{
    private static ImageInfoProvider sInfoProvider = null;
    
    private ArrayList<ImageInfo> dataList = null;
    
    private ImageInfoProvider()
    {
    }
    
    public static ImageInfoProvider getInstance()
    {
        if (null == sInfoProvider)
        {
            sInfoProvider = new ImageInfoProvider();
        }
        return sInfoProvider;
    }
    
    public void setDataList(ArrayList<ImageInfo> list)
    {
        dataList = list;
    }
    
    public ArrayList<ImageInfo> getDataList()
    {
        return dataList;
    }
}
