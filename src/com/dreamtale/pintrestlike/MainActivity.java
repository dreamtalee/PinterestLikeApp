package com.dreamtale.pintrestlike;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

public class MainActivity extends Activity
{
    private final static String LOG_TAG = "MainActivity";
    
    // ScrollView layout
    @SuppressWarnings("unused")
    private PinterestScrollView scrollView = null;
    private LinearLayout firstRow = null;
    private LinearLayout secondRow = null;
    
    private MenuItem searchItem = null;
    
    // GridView layout
    private GridView gridView = null;
    private ImageAdapter adapter = null;
    
    private int leftHeight = 0;
    private int rightHeight = 0;
    
    private Handler parserHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            Log.d(LOG_TAG, "message object is " + msg.obj);
//            if (null != msg.obj)
//            {
//                ArrayList<ImageInfo> infos = (ArrayList<ImageInfo>)msg.obj;
                
                ArrayList<ImageInfo> infos = ImageInfoProvider.getInstance().getDataList();
                
                if (null == infos || infos.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "No valid result!", Toast.LENGTH_SHORT).show();
                }
                
                if (UIConfig.MODE_GRID_VIEW == UIConfig.getInstance().getUIMode())
                {
                    if (null == adapter)
                    {
                        adapter = new ImageAdapter(MainActivity.this, infos);
                    }
                    adapter.setDataList(infos);
                    gridView.setAdapter(adapter);
                }
                else
                {
                  addViews(infos);
                }
//            }
        };
    };
    
    private OnItemClickListener gridItemSelectedListener = new OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id)
        {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra(IntentConstant.EXTRA_IMAGE_DETAIL_FRAGMENT_POSITION, position);
            startActivity(intent);
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (UIConfig.MODE_GRID_VIEW == UIConfig.getInstance().getUIMode())
        {
            setContentView(R.layout.activity_gridview);
            initlizeViewGrid();
        }
        else
        {
            setContentView(R.layout.activity_main);
            initlizeViewScroll();
        }
        queryKeyword("girl");
    }
    
    private void queryKeyword(final String keyword)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                ArrayList<ImageInfo> list = ImageParser.getImageInfos(ImageParser.getQueryUrl(keyword));
//                Message msg = Message.obtain();
//                msg.obj = list;
//                parserHandler.sendMessage(msg);
                ImageInfoProvider.getInstance().setDataList(list);
                parserHandler.sendEmptyMessage(0);
            }
        }, "parser thread").start();
    }
    
    @Override
    protected void onDestroy()
    {
        Log.d(LOG_TAG, "Clear cache and destroy activity");
        CacheManager.getInstance().clearCache();
        super.onDestroy();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = new SearchView(this);
        searchView.setQueryHint("input key word");
        searchView.setOnQueryTextListener(queryTextListener);
        searchItem.setActionView(searchView);
        return true;
    }
    
    private OnQueryTextListener queryTextListener = new OnQueryTextListener()
    {
        
        @Override
        public boolean onQueryTextSubmit(String query)
        {
            try
            {
                queryKeyword(URLEncoder.encode(query, "UTF-8"));
            }
            catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
            searchItem.collapseActionView();
            return true;
        }
        
        @Override
        public boolean onQueryTextChange(String newText)
        {
            return false;
        }
    };

    private void initlizeViewScroll()
    {
        scrollView = (PinterestScrollView)findViewById(R.id.main);
        firstRow = (LinearLayout)findViewById(R.id.firstrow);
        secondRow = (LinearLayout)findViewById(R.id.secondrow);
    }
    
    private void initlizeViewGrid()
    {
        gridView = (GridView)findViewById(R.id.grid);
        gridView.setOnItemClickListener(gridItemSelectedListener);
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                
            }
        });
    }
    
    private void addViews(ArrayList<ImageInfo> infos)
    {
        int size = infos.size();
//        final int itemWidth = (getScreenWidth() - 500) / 2;
        final int itemWidth = firstRow.getMeasuredWidth() - 20;
        for (int i = 0; i < size; i++)
        {
            ItemView item = new ItemView(this);
            item.setBackgroundResource(R.drawable.background);
            item.setImageResource(R.drawable.empty_photo);
            ImageInfo info = infos.get(i);
            item.setTag(info.getThumbUrl());
            final int height = (int)(((float)info.getWidth() / (float)info.getHeight()) * (float)itemWidth);
            int j = add2WhichRow(height);
            if (0 == j)
            {
                firstRow.addView(item);
                item.setImageInfo(info);
            }
            else
            {
                secondRow.addView(item);
                item.setImageInfo(info);
            }
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)item.getLayoutParams();
            params.width = itemWidth;
            Log.d(LOG_TAG, "the json info.getWidth()" + info.getWidth() + "info.getHeight() "+ info.getHeight()+" is " + (float)info.getWidth() / (float)info.getHeight());
            params.height = height;
            Log.d(LOG_TAG, "the json height is " + params.height);
            params.bottomMargin = 10;
            params.leftMargin = 10;
            params.rightMargin = 10;
        }
    }
    
    private int add2WhichRow(int height)
    {
        if (leftHeight - rightHeight > height / 2)
        {
            rightHeight = rightHeight + height;
            return 1;
        }
        else
        {
            leftHeight = leftHeight + height;
            return 0;
        }
    }
    
    public int getScreenWidth()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        return point.y;
    }
}
