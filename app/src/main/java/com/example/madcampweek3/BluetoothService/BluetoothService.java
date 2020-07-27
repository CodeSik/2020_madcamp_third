package com.example.madcampweek3.BluetoothService;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.madcampweek3.R;
import com.example.madcampweek3.RetrofitService.AccountService;
import com.example.madcampweek3.RetrofitService.FriendService;
import com.example.madcampweek3.RetrofitService.PositionService;
import com.example.madcampweek3.RetrofitService.RetrofitClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.madcampweek3.MainActivity.MainActivity.ONGOING_BLUETOOTH;

public class BluetoothService extends Service {
    private BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_CODE_LOCATION = 2;
    private LocationManager locationManager;
    private Number contactID = 0; // TODO: Change contactID
    private String frinedID = null; // TODO: Support multiple friend
    private boolean first = true;

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
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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

        TimerTask bluetoothSearch = new TimerTask() {
            @Override
            public void run() {
                bluetoothAdapter.startDiscovery();
            }
        };


        Timer timer = new Timer();
        timer.schedule(discoverRequest, 0, 1000 * 3600);
        timer.schedule(bluetoothSearch, 0, 1000 * 15);
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

                tryFindUser(deviceHardwareAddress, new FindUserResponse() {
                    @Override
                    public void onResponseReceived(String id) {
                        Log.d("Bluetooth Service", "ID: " + id);
                        frinedID = id;
                        getContactID();
                        getPosition();
                    }
                });
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            }
        }
    };

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    public static void tryFindUser(String address, FindUserResponse findUserResponse) {
        /* Init */
        Retrofit retrofit = RetrofitClient.getInstnce();
        AccountService service = retrofit.create(AccountService.class);

        /* Send macAddress */
        service.findUser(address).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                if (response.body() == null) {
                    try { // Can't find user
                        assert response.errorBody() != null;
                        Log.d("AccountService", "res: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    String res = null;
                    if (response.body().has("userID")) {
                        String temp = response.body().get("userID").toString();
                        res = temp.substring(1, temp.length() - 1);
                    }
                    Log.d("AccountService", "res:" + res);
                    findUserResponse.onResponseReceived(res);
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                Log.d("AccountService", "Failed API call with call: " + call
                        + ", exception: " + t);
            }
        });
    }

    private void getPosition() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                1000, 1, mLocationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000, 1, mLocationListener);
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            locationManager.removeUpdates(this);
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            /* init retrofit */
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            PositionService service = retrofit.create(PositionService.class);
            String address = latitude + "," + longitude;

            service.findRegion(address, getResources().getString(R.string.api_key)).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                    if (response.body() == null) {
                        try { // Failure
                            assert response.errorBody() != null;
                            Log.d("PositionService", "res: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else { // Success
                        if (response.body().has("results")) {
                            JsonArray results = response.body().getAsJsonArray("results");
                            Log.d("PositionService", "res:" + results.get(0));
                            if (results.get(0).getAsJsonObject().has("formatted_address")) {
                                String position = results.get(0).getAsJsonObject().get("formatted_address").toString();
                                Calendar currentTime = Calendar.getInstance();
                                sendMeetInfo(currentTime, position);
                            }
                        }

                    }
                }

                @Override
                public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                    Log.d("AccountService", "Failed API call with call: " + call
                            + ", exception: " + t);
                }
            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//        // ...
//    }
    private void sendMeetInfo(Calendar time, String position) {
        /* Init retrofit */
        Retrofit retrofit = RetrofitClient.getInstnce();
        FriendService service = retrofit.create(FriendService.class);

        /* Get time */
        String contaactTime;
        int hours = time.get(Calendar.HOUR_OF_DAY);
        int date = time.get(Calendar.DAY_OF_YEAR) + (365 * time.get(Calendar.YEAR));
        if (hours >= 8 && hours < 16) {
            contaactTime = "day";
        } else if (hours >= 16 && hours < 24) {
            contaactTime = "dinner";
        } else {
            contaactTime = "night";
        }
        JsonObject body = new JsonObject();
        body.addProperty("id", "test");
        body.addProperty("friendID", frinedID);
        body.addProperty("contactID", contactID);
        body.addProperty("position", position);
        body.addProperty("contactTime", contaactTime);
        body.addProperty("date", date);
        /* Send meetInfo */
        service.addContact(body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                if (response.body() == null) {
                    try { // Failure
                        assert response.errorBody() != null;
                        Log.d("FriendService", "res: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else { // Success
                    Log.d("FriendService", "res: " + response.body().toString());
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                Log.d("FriendService", "Failed API call with call: " + call
                        + ", exception: " + t);
            }
        });
    }

    private void getContactID() {
        if (!first) {
            return;
        }
        first = false;
        /* Init retrofit */
        Retrofit retrofit = RetrofitClient.getInstnce();
        FriendService service = retrofit.create(FriendService.class);
        Calendar time = Calendar.getInstance();
        int date = time.get(Calendar.DAY_OF_YEAR) + (365 * time.get(Calendar.YEAR));


        service.getContactID("test", frinedID, date).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                if (response.body() == null) {
                    try { // Failure
                        assert response.errorBody() != null;
                        Log.d("FriendService", "res: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else { // Success
                    Log.d("FriendService", "res: " + response.body().toString());
                    if (response.body().has("contactID")) {
                        contactID = response.body().get("contactID").getAsNumber();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                Log.d("FriendService", "Failed API call with call: " + call
                        + ", exception: " + t);
            }
        });

    }

    interface FindUserResponse {
        void onResponseReceived(String id);
    }
}
