package com.gshp.mqttpersistencev2;

import android.app.Application;

public class MqttApplication extends Application {
    public static MqttApplication application;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
    }
}
