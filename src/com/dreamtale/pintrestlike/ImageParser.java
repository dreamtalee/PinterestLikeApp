package com.dreamtale.pintrestlike;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;


public class ImageParser
{
    public final static String WEB_REQUEST_URL = "http://image.baidu.com/i?tn=baiduimagejson&word=%s&rn=100&pn=1";
    
    public final static String JSON_BAIDU_DATA = "data";
    public final static String JSON_BAIDU_DATA_TYPE = "type";
    public final static String JSON_BAIDU_DATA_THUMBURL = "thumbURL";
    public final static String JSON_BAIDU_DATA_OBJURL = "objURL";
    public final static String JSON_BAIDU_DATA_WIDTH = "width";
    public final static String JSON_BAIDU_DATA_HEIGHT = "height";
    
    public static String getQueryUrl(String keyword)
    {
        return String.format(WEB_REQUEST_URL, keyword);
    }
    
    /**
     * Return the JSON query result.
     * 
     * @param requestUrl
     * @return
     */
    private static String queryJSON(String requestUrl)
    {
        if (TextUtils.isEmpty(requestUrl))
        {
            return "";
        }
        
        String result = "";
        HttpURLConnection connection = null;
        try
        {
            URL url = new URL(requestUrl);
            connection = (HttpURLConnection)url.openConnection();
            connection.connect();
            BufferedInputStream bufferedStream = new BufferedInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(bufferedStream));
            StringBuffer jsonStr = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                jsonStr.append(line).append("\n");
            }
            result = jsonStr.toString();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != connection)
            {
                connection.disconnect();
            }
        }
        return result;
    }
    
    public static ArrayList<ImageInfo> getImageInfos(String requestUrl)
    {
        String jsonStr = queryJSON(requestUrl);
        if (TextUtils.isEmpty(jsonStr))
        {
            return null;
        }
        
        ArrayList<ImageInfo> list = new ArrayList<ImageInfo>();
        try
        {
            JSONObject root = new JSONObject(jsonStr);
            JSONArray array = root.getJSONArray(JSON_BAIDU_DATA);
            if (null != array)
            {
                int length = array.length();
                for (int i = 0; i < length; i++)
                {
                    JSONObject item = array.getJSONObject(i);
                    if (null != item && 0 != item.length())
                    {
                        ImageInfo info = new ImageInfo();
                        info.setFileType(item.optString(JSON_BAIDU_DATA_TYPE));
                        info.setThumbUrl(item.optString(JSON_BAIDU_DATA_THUMBURL));
                        info.setFullSizeUrl(item.optString(JSON_BAIDU_DATA_OBJURL));
                        info.setWidth(item.optInt(JSON_BAIDU_DATA_WIDTH));
                        info.setHeight(item.optInt(JSON_BAIDU_DATA_HEIGHT));
                        list.add(info);
                    }
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return list;
    }
}
