package com.dreamtale.pintrestlike.utils;


public class UIConfig
{
    public final static int MODE_GRID_VIEW = 0;
    
    public final static int MODE_PINTEREST_VIEW = 1;
    
    private int uiMode = MODE_GRID_VIEW;
    
    public static UIConfig sConfig = new UIConfig();
    
    private UIConfig()
    {
    }
    
    public static UIConfig getInstance()
    {
        return sConfig;
    }
    
    public void setMode(int mode)
    {
        uiMode = mode;
    }
    
    public int getUIMode()
    {
        return uiMode;
    }
    
}
