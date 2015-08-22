package com.jasper.myandroidtest.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;

public class BinderService extends Service {
    private static final String TAG = "BinderService";
    private final IBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        public BinderService getService() {
            //返回当前Service的引用
            return BinderService.this;
        }
    }

    private final Random random = new Random();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public int getRandomNumber() {
        return random.nextInt();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}
