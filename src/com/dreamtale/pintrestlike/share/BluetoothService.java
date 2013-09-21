package com.dreamtale.pintrestlike.share;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

public class BluetoothService
{
    public static final int MSG_BLUETOOTH_READ = 0x01;
    
    private static final String TAG = "BluetoothService";
    
    private static final String SDP_RECORD_NAME = "Pinterest";
    
    private static final UUID SDP_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    
    private BluetoothAdapter adapter = null;
    
    private Handler handler = null;
    
    private AcceptThread acceptThread = null;
    private ConnectThread connectThread = null;
    private DataThread dataThread = null;
    
    public BluetoothService(Handler handler)
    {
        adapter = BluetoothAdapter.getDefaultAdapter();
        this.handler = handler;
    }
    
    public void connect(BluetoothDevice device)
    {
        if (null != connectThread)
        {
            connectThread.cancel();
            connectThread = null;
        }
        
        connectThread = new ConnectThread(device);
        connectThread.start();
    }
    
    public void start()
    {
        Log.d(TAG, "start bluetooth service");
        clearThread();
        acceptThread = new AcceptThread();
        acceptThread.start();
    }
    
    private void manageSocket(BluetoothSocket socket)
    {
        Log.d(TAG, "start manage socket");
        clearThread();
        dataThread = new DataThread(socket);
        dataThread.start();
    }
    
    private void clearThread()
    {
        if (null != acceptThread)
        {
            acceptThread.cancel();
            acceptThread = null;
        }
        if (null != connectThread)
        {
            connectThread.cancel();
            connectThread = null;
        }
        if (null != dataThread)
        {
            dataThread.cancel();
            dataThread = null;
        }
    }
    
    class AcceptThread extends Thread
    {
        BluetoothServerSocket serverSocket = null;
        
        AcceptThread()
        {
            BluetoothServerSocket tmp = null;
            setName("AcceptThread");
            try
            {
                tmp = adapter.listenUsingInsecureRfcommWithServiceRecord(SDP_RECORD_NAME, SDP_UUID);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            serverSocket = tmp;
        }
        
        @Override
        public void run()
        {
            BluetoothSocket socket = null;
            while (true)
            {
                try
                {
                    socket = serverSocket.accept();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    break;
                }
                
                if (null != socket)
                {
                    // Manage the socket.
                    manageSocket(socket);
                    cancel();
                }
            }
        }
        
        public void cancel()
        {
            if (null != serverSocket)
            {
                try
                {
                    serverSocket.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    class ConnectThread extends Thread
    {
        BluetoothDevice device = null;
        BluetoothSocket socket = null;
        
        ConnectThread(BluetoothDevice device)
        {
           setName("ConnectThread");
           BluetoothSocket tmp = null;
           this.device = device;
           try
            {
               tmp = device.createInsecureRfcommSocketToServiceRecord(SDP_UUID);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
//        Method m;
//        try
//        {
//            m = device.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
//            tmp = (BluetoothSocket) m.invoke(device, 1);
//        }
//        catch (NoSuchMethodException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        catch (IllegalArgumentException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        catch (IllegalAccessException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        catch (InvocationTargetException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        socket = tmp;
        }
        
        @Override
        public void run()
        {
            adapter.cancelDiscovery();
            
            try
            {
                socket.connect();
                Log.d(TAG, "---------------connect success -------------------");
            }
            catch (IOException e)
            {
                e.printStackTrace();
                cancel();
                return;
            }
            
            // Manage the socket.
            manageSocket(socket);
        }
        
        public void cancel()
        {
            if (null != socket)
            {
                try
                {
                    socket.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    
    class DataThread extends Thread
    {
        BluetoothSocket socket = null;
        InputStream is = null;
        OutputStream os = null;
        
        DataThread(BluetoothSocket socket)
        {
            setName("DataThread");
            this.socket = socket;
            
            try
            {
                is = socket.getInputStream();
                os = socket.getOutputStream();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                cancel();
            }
        }
        
        @Override
        public void run()
        {
            while (true)
            {
                ByteBuffer data = ByteBuffer.allocate(1024);
                try
                {
                    int count = is.read(data.array());
                    handler.obtainMessage(MSG_BLUETOOTH_READ, count, -1, data);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    cancel();
                    break;
                }
            }
        }
        
        public void write(byte[] data)
        {
            try
            {
                os.write(data);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                cancel();
            }
        }
        
        public void cancel()
        {
            if (null != socket)
            {
                try
                {
                    socket.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
