package com.dreamtale.pintrestlike;

import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class DetailActivity extends FragmentActivity
{
    private static final String TAG = "DetailActivity";
    
    private static final int BLUETOOTH_INTENT_ENABLE_REQUEST_CODE = 0x01;
    private static final int BLUETOOTH_INTENT_DISCOVERABLE_REQUEST_CODE = 0x02;
    private static final int BLUETOOTH_INTENT_SCAN_REQUEST_CODE = 0x03;
    
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
    
    @Override
    protected void onStart()
    {
        super.onStart();
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch (id)
        {
        case R.id.action_share:
            if (!isBluetoothEnabled())
            {
                enableBluetooth();
            }
            else
            {
                Intent intent = new Intent();
                intent.setClass(this, BluetoothDeviceListActivity.class);
                startActivityForResult(intent, BLUETOOTH_INTENT_SCAN_REQUEST_CODE);
            }
            break;
        case R.id.action_discoverable:
            makeDeviceDiscoverable();
            break;
        default:
            break;
        }
        return true;
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
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
        case BLUETOOTH_INTENT_ENABLE_REQUEST_CODE:
            if (Activity.RESULT_OK == resultCode)
            {
                Intent intent = new Intent();
                intent.setClass(this, BluetoothDeviceListActivity.class);
                startActivityForResult(intent, BLUETOOTH_INTENT_SCAN_REQUEST_CODE);
                Log.d(TAG, "enable bluetooth success!");
            }
            else
            {
                Log.d(TAG, "enable bluetooth failed!");
            }
            break;
        case BLUETOOTH_INTENT_DISCOVERABLE_REQUEST_CODE:
            
            break;
            
        case BLUETOOTH_INTENT_SCAN_REQUEST_CODE:
            if (Activity.RESULT_OK == resultCode)
            {
                
            }
            break;
        default:
            break;
        }
    }
    
    /**
     * Whether the BlueTooth enabled.
     * 
     * @return  
     */
    private boolean isBluetoothEnabled()
    {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (null == adapter)
        {
            Toast.makeText(this, "No bluetooth support!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return adapter.isEnabled();
        }
    }
    
    private void enableBluetooth()
    {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, BLUETOOTH_INTENT_ENABLE_REQUEST_CODE);
    }
    
    private void makeDeviceDiscoverable()
    {
        if (BluetoothAdapter.getDefaultAdapter().getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE)
        {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivityForResult(intent, BLUETOOTH_INTENT_DISCOVERABLE_REQUEST_CODE);
        }
    }
}
