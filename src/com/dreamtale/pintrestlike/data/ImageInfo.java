package com.dreamtale.pintrestlike.data;

public class ImageInfo
{
    private String thumbUrl = null;
    
    private String fullSizeUrl = null;
    
    private String fileType = null;

    private int width = 0;
    
    private int height = 0;
    
    public ImageInfo()
    {
        
    }
    
    public String getThumbUrl()
    {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl)
    {
        this.thumbUrl = thumbUrl;
    }

    public String getFullSizeUrl()
    {
        return fullSizeUrl;
    }

    public void setFullSizeUrl(String fullSizeUrl)
    {
        this.fullSizeUrl = fullSizeUrl;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public String getFileType()
    {
        return fileType;
    }

    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }
}
