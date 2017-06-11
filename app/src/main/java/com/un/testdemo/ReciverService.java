package com.un.testdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/*import com.cusc.vss.carpad.mqtt.MqttManager;

import org.eclipse.paho.client.mqttv3.MqttException;*/

/**
 * introduce:用于在后台接收一些消息
 * author : mrli
 * date: 6/9/17.
 */

public class ReciverService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }



    @Override
    public void onCreate() {
        super.onCreate();
       /* openMqttConnect();
        subMqttTopic();*/
    }


    public class MyBinder extends Binder {

        public ReciverService getService() {
            return ReciverService.this;
        }

    }

/*
    *//**
     * 打开Mqtt连接
     *//*
    public void openMqttConnect() {
        //TODO:创建MQTT协议
        new Thread(new Runnable() {
            @Override
            public void run() {
               // boolean b = MqttManager.getInstance().creatConnect(URL, userName, password, clientId);
               /// Log.d("MQTT", "isConnected: " + b);
            }
        }).start();
    }


    *//**
     * 发送Mqtt消息
     *//*
    public void sendMqttMsg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean b = MqttManager.getInstance().publish("test", 2, "hello".getBytes());
                Log.d("MQTT", "publish: " + b);
            }
        }).start();
    }


    *//**
     * 订阅Mqtt主题
     *//*
    public void subMqttTopic() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean b = MqttManager.getInstance().subscribe("test", 2);
                Log.d("MQTT", "subscribe: " + b);
            }
        }).start();
    }


    *//**
     * 关闭Mqtt连接
     *//*
    public void closeMqttConnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MqttManager.getInstance().disConnect();
                    Log.d("MQTT", "isdisConnet: ture");
                } catch (MqttException e) {
                }
            }
        }).start();
    }*/


}
