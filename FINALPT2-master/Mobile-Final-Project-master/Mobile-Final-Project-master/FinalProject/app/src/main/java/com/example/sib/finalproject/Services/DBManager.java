package com.example.sib.finalproject.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;


public class DBManager extends Service {
    public DBManager() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);


        // we need this for extracting username when 'emitting' steps to the server.
        //prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // We need to initialize an intent filter that will recognize 'ACTION_TIME_TICK'
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);

        // We need to register our local broadcast receiver
        registerReceiver(receiver, filter);

        Log.d("background_service", "BackgroundService Started!");


        //sensor manager allows us to get access to all of the sensors that your device is offering you.
        //mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // START_STICKY -- ? what does it mean? Research it.
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("background_service", "BackgroundService Stopped!");

        //inside onDestroy you need to 'unregister' the broadcast receiver
        unregisterReceiver(receiver);
        super.onDestroy();
    }


    // BroadcastRecevier receiver
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // extracing the string that the action is bringin in
            String action = intent.getAction();


            Log.d("broadcast_service", "action received:" + action.toString());


            // if our action contains "TIME_TICK" we upload to the server via socket
            if (action.contains("TIME_TICK")) {

            }


        }

    };
}
