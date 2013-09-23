package com.dreamtale.pintrestlike.activity;

import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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

import com.dreamtale.pintrestlike.R;
import com.dreamtale.pintrestlike.share.BluetoothService;

public class BluetoothDeviceListActivity extends Activity
{
    
    private Button scanBtn = null;
    private ListView deviceListView = null;
    private ProgressBar progressBar = null;
    private TextView statusView = null;
    
    private BluetoothService bluetoothService = null;
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
        bluetoothService = BluetoothService.getInstance();
        
        initDevices();
    }
    
    private Handler uiHandler = new Handler()
    {
        public void dispatchMessage(android.os.Message msg)
        {
            if (BluetoothService.MSG_BLUTHTOOTH_STATE_CHANGE == msg.what)
            {
                switch (msg.arg1)
                {
                case BluetoothService.STATE_NONE:
                    statusView.setText("Not connected");
                    break;
                case BluetoothService.STATE_CONNECTING:
                    statusView.setText("Connecting...");
                    break;
                case BluetoothService.STATE_CONNECTED:
                    statusView.setText("Connected");
                    break;
                default:
                    break;
                }
            }
        };
    };
    
    private void initViews()
    {
        scanBtn = (Button)findViewById(R.id.scan);
        deviceListView = (ListView)findViewById(R.id.devicelist);
        progressBar = (ProgressBar)findViewById(R.id.progress);
        statusView = (TextView)findViewById(R.id.connectstatus);
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
            this.devices.addAll(devices);
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
            bluetoothAdapter.cancelDiscovery();
            
            bluetoothService.connect(devices.get(position));
            
//            Intent intent = new Intent();
//            intent.putExtra(IntentConstant.EXTRA_BLUETOOTH_ADDRESS, devices.get(position).getAddress());
//            setResult(RESULT_OK, intent);
//            finish();
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
        bluetoothService.addHandler(uiHandler);
        registerBlueToothFound();
    }
    
    @Override
    protected void onStop()
    {
        super.onStop();
        bluetoothService.removeHandler(uiHandler);
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
