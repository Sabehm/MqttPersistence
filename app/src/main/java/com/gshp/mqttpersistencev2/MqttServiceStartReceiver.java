package com.gshp.mqttpersistencev2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MqttServiceStartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, MqttConnectionManagerService.class));
    }
}