package com.gshp.mqttpersistencev2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    Intent serviceIntent;
    private MqttConnectionManagerService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = new MqttConnectionManagerService();
        serviceIntent = new Intent(this, service.getClass());

        if (!isServiceRunning(service.getClass())) {
            startService(serviceIntent);
        }
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //startService(new Intent(this, MqttConnectionManagerService.class));
    }

    @Override
    protected void onDestroy() {
        stopService(serviceIntent);
        super.onDestroy();
    }
}