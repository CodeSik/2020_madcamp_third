package com.example.madcampweek3.BluetoothService;

import android.Manifest;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.madcampweek3.LocalScan.Friend;
import com.example.madcampweek3.LocalScan.LocalScan;
import com.example.madcampweek3.R;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static com.example.madcampweek3.LocalScan.LocalScan.REQUEST_ENABLE_BT;
import static com.example.madcampweek3.LocalScan.LocalScan.tryFindUser;
import static com.example.madcampweek3.MainActivity.MainActivity.ONGOING_BLUETOOTH;

public class BluetoothService extends Service{
    private BluetoothAdapter bluetoothAdapter;
    @Override
    public void onCreate() {
        Notification notification =
            new Notification.Builder(this, "1234")
                    .setContentTitle("우연히")
                    .setContentText("Bluetooth 탐색 중")
                    .setSmallIcon(R.drawable.pngwing)
                    .build();
        startForeground(ONGOING_BLUETOOTH, notification);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Bluetoothservice starting", Toast.LENGTH_SHORT).show();
        /* Init bluetooth */
        bluetoothAdapter =  BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            return Service.START_NOT_STICKY;
        }
//            if (!bluetoothAdapter.isEnabled()) {
//                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                getContext.startActivityForResult(this, enableBtIntent, REQUEST_ENABLE_BT,);
//            }

        /* Scan bluetooth */
        IntentFilter bluetoothFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        bluetoothFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, bluetoothFilter);

        TimerTask discoverRequest = new TimerTask() {
            @Override
            public void run() {
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3600);
                startActivity(discoverableIntent);
            }
        };

        bluetoothAdapter.startDiscovery();
        Timer timer = new Timer();
        timer.schedule(discoverRequest, 0, 1000 * 3600);
        return START_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                if (name != null) {
                    Log.d("Bluetooth", "Find name: " + name);
                }
                String deviceHardwareAddress = device.getAddress(); // MAC address

                tryFindUser(deviceHardwareAddress, new LocalScan.FindUserResponse() {
                    @Override
                    public void onResponseReceived(String name) {
                        Log.d("Bluetooth Service", name);
                    }
                });
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                bluetoothAdapter.startDiscovery();
            }
        }
    };

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }
}
