package com.jasper.myandroidtest.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 注意AndroidManifest.xml中对该service定义了intent-filter标签，
 * 该值要与MY_ACTION一直，用去其他Activity启动该service
 */
public class MyService extends Service {
    private static final String TAG = "MyService";
    public static final String MY_SERVICE_ACTION = "com.jasper.myandroidtest.myservice";
    public static final String MY_SERVICE_RECIVER = "com.jasper.myandroidtest.myservice.reciver";
    public static final String TYPE = "type";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        doSomething();
    }

    public void doSomething() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MY_SERVICE_RECIVER);
                intent.addCategory(ServiceType.TEST);
                int i = 0;
                while (i++ < 10) {
                    try {
                        intent.putExtra("msg", "test");
                        sendBroadcast(intent);
                        Log.d(TAG, "doSomething " + i);
                        Thread.sleep(300);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void startDownload(final Intent intent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int process = 0;
                int max = 100;
                while (process <= max) {
                    try {
                        intent.putExtra("process", process);
                        process += 20;
                        sendBroadcast(intent);
                        Log.d(TAG, "startDownload send:" + process);
                        Thread.sleep(200);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent intentSend = new Intent(MY_SERVICE_RECIVER);
        intentSend.addCategory(intent.getStringExtra(TYPE));
        switch (intent.getStringExtra(TYPE)) {
            case ServiceType.PROGRESSBAR:
                startDownload(intentSend);
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
