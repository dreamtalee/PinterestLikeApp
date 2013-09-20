package com.dreamtale.pintrestlike;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class DetailActivity extends FragmentActivity
{
    private ViewPager viewPager = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_fragment);
        viewPager = (ViewPager)findViewById(R.id.pager);
        ImagePageAdapter adapter = new ImagePageAdapter(getSupportFragmentManager(), ImageInfoProvider.getInstance().getDataList());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        
        final int position = getIntent().getIntExtra(IntentConstant.EXTRA_IMAGE_DETAIL_FRAGMENT_POSITION, -1);
        if (-1 != position)
        {
            viewPager.setCurrentItem(position);
        }
    }
    
    static class ImagePageAdapter extends FragmentPagerAdapter
    {
        private ArrayList<ImageInfo> dataList = null;
        
        public ImagePageAdapter(android.support.v4.app.FragmentManager fm, ArrayList<ImageInfo> dataList)
        {
            super(fm);
            this.dataList = dataList;
        }

        @Override
        public Fragment getItem(int arg0)
        {
            return new ImageDetailFragment(dataList.get(arg0));
        }

        @Override
        public int getCount()
        {
            return dataList.size();
        }
    }
}
