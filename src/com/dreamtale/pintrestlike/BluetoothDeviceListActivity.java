package com.dreamtale.pintrestlike;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.Inflater;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BluetoothDeviceListActivity extends Activity
{
    private Button scanBtn = null;
    private ListView deviceListView = null;
    private ProgressBar progressBar = null;
    
    private BluetoothAdapter bluetoothAdapter = null;
    private ArrayList<BluetoothDevice> devices = null;
    private DeviceAdapter deviceAdapter = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list);
        initViews();
        
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        
        initDevices();
    }
    
    private void initViews()
    {
        scanBtn = (Button)findViewById(R.id.scan);
        deviceListView = (ListView)findViewById(R.id.devicelist);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        scanBtn.setOnClickListener(scanListener);
        deviceListView.setOnItemClickListener(itemClickListener);
    }
    
    private void initDevices()
    {
        devices = new ArrayList<BluetoothDevice>();
        deviceAdapter = new DeviceAdapter(this, devices);
        deviceListView.setAdapter(deviceAdapter);
        
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        if (devices.size() > 0)
        {
            devices.addAll(devices);
        }
        else
        {
            deviceListView.setVisibility(View.GONE);
            scanBtn.setVisibility(View.VISIBLE);
        }
    }
    
    private OnClickListener scanListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            scanBtn.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            startDiscovery();
        }
        
    };
    
    private OnItemClickListener itemClickListener = new OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id)
        {
            
        }
    };
    
    private void startDiscovery()
    {
        if (bluetoothAdapter.isDiscovering())
        {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }
    
    @Override
    protected void onStart()
    {
        super.onStart();
        registerBlueToothFound();
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
        unRegisterBlueToothFound();
        bluetoothAdapter.cancelDiscovery();
    }
    
    private void registerBlueToothFound()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }
    
    private void unRegisterBlueToothFound()
    {
        unregisterReceiver(receiver);
    }
    
    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                progressBar.setVisibility(View.GONE);
                deviceListView.setVisibility(View.VISIBLE);
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                devices.add(device);
                deviceAdapter.notifyDataSetChanged();
            }
        }
    };
    
    private class DeviceAdapter extends BaseAdapter
    {
        private Context context = null;
        private ArrayList<BluetoothDevice> dataList = null;
        
        public DeviceAdapter(Context context, ArrayList<BluetoothDevice> dataList)
        {
            this.context = context;
            this.dataList = dataList;
        }
        
        @Override
        public int getCount()
        {
            return dataList.size();
        }

        @Override
        public Object getItem(int position)
        {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder viewHolder = null;
            if (null == convertView)
            {
                convertView = LayoutInflater.from(context).inflate(R.layout.device_item, null);
                viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) convertView.findViewById(R.id.devicename);
                convertView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            
            viewHolder.textView.setText(dataList.get(position).getName());
            
            return convertView;
        }
    }
    
    static class ViewHolder
    {
        TextView textView;
    }
}
