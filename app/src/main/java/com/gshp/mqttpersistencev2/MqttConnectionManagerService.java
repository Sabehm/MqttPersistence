package com.gshp.mqttpersistencev2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttConnectionManagerService extends Service {

    private static final String TAG = "MqttConnectionManagerSe";

    private MqttAndroidClient client;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        connect();
        return START_STICKY;
    }

    public void connect() {
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(getApplication().getApplicationContext(), "tcp://192.168.100.5:1883",
                clientId);
        try {
            IMqttToken token = client.connect();
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws MqttException {
                    Log.d(TAG, "messageArrived: Topic: " + topic + " Message: " + message);
                    if (message.toString().equals("Hola")){
                        String string = "Te escucho";
                        byte[] bytes = string.getBytes();
                        MqttMessage mqttMessage = new MqttMessage(bytes);
                        client.publish("response", mqttMessage);
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });

            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "onSuccess: Successfully connected to broker!!!");
                    try {
                        client.subscribe("test", 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.d(TAG, "onSuccess:  Successfully subscribed to topic!!!");
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                            }
                        });
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "onFailure");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartService");
        broadcastIntent.setClass(this, MqttServiceStartReceiver.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }
}
