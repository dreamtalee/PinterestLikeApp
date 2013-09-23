package com.dreamtale.pintrestlike;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ImageDetailFragment extends Fragment
{
    private static final String LOG_TAG = "ImageDetailFragment";
    
    private ImageInfo info = null;
    private ItemView imageView = null;
    private ImageDownloader downloader = null;
    
    public ImageDetailFragment(ImageInfo info)
    {
        this.info = info;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d(LOG_TAG, "onCreate is called");
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        Log.d(LOG_TAG, "onCreateView is called");
        View view = inflater.inflate(R.layout.image_detail, container, false);
        imageView = (ItemView)view.findViewById(R.id.imageView);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        Log.d(LOG_TAG, "onActivityCreated is called");
        super.onActivityCreated(savedInstanceState);
        imageView.setTag(info.getFullSizeUrl());
        downloader = new ImageDownloader(imageView);
        downloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }
    
    public void setShareURL(String url)
    {
        if (null != downloader)
        {
            downloader.cancel(true);
        }
        imageView.setImageResource(R.drawable.empty_photo);
        imageView.setTag(url);
        downloader = new ImageDownloader(imageView);
        downloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
    }
    
    @Override
    public void onDestroyView()
    {
        if (null != downloader)
        {
            downloader.cancel(true);
        }
        Log.d(LOG_TAG, "onDestroyView is called");
        super.onDestroyView();
    }

    @Override
    public void onDetach()
    {
        Log.d(LOG_TAG, "onDetach is called");
        super.onDetach();
    }

    @Override
    public void onPause()
    {
        Log.d(LOG_TAG, "onPause is called");
        super.onPause();
    }

    @Override
    public void onResume()
    {
        Log.d(LOG_TAG, "onResume is called");
        super.onResume();
    }

    @Override
    public void onStart()
    {
        Log.d(LOG_TAG, "onStart is called");
        super.onStart();
    }

    @Override
    public void onStop()
    {
        Log.d(LOG_TAG, "onStop is called");
        super.onStop();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        Log.d(LOG_TAG, "onViewCreated is called");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState)
    {
        Log.d(LOG_TAG, "onViewStateRestored is called");
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onDestroy()
    {
        Log.d(LOG_TAG, "onDestroy is called");
        if (null != downloader)
        {
            downloader.cancel(true);
        }
        super.onDestroy();
    }

}
