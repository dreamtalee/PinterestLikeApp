package com.dreamtale.pintrestlike;

import java.util.Set;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class BluetoothDeviceListDialog extends DialogFragment
{
    private Set<BluetoothDevice> devices = null;
    
    private OnItemClickListener onItemClickListener;
    
    interface OnItemClickListener
    {
        void onitemClick(BluetoothDevice device);
    }
    
    public BluetoothDeviceListDialog(Set<BluetoothDevice> items)
    {
        devices = items;
    }
    
    public void setItemClickListener(OnItemClickListener listener)
    {
        onItemClickListener = listener;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick a device");
        CharSequence[] items = new CharSequence[devices.size()];
        int i = 0;
        for (BluetoothDevice bluetoothDevice : devices)
        {
            items[i++] = bluetoothDevice.getName();
        }
        builder.setItems(items, onClickListener);
        builder.setNegativeButton("cancel", null);
        return builder.create();
    }
    
    private OnClickListener onClickListener = new OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            if (null != onItemClickListener)
            {
                int i = 0;
                for (BluetoothDevice device : devices)
                {
                    if (i== which)
                    {
                        onItemClickListener.onitemClick(device);
                        return;
                    }
                    i++;
                }
                onItemClickListener.onitemClick(null);
            }
        }
    };
}
