package com.jasper.myandroidtest.service.aidl;

import android.app.Service;
import android.content.Intent;
import android.graphics.*;
import android.graphics.Point;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.jasper.myandroidtest.utils.ProcessUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyAidlRemoteService extends Service {
    private static final String TAG = "MyAidlRemoteService";
    public MyAidlRemoteService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private final IMyAidlRemoteService.Stub binder = new IMyAidlRemoteService.Stub() {

        @Override
        public int getPid() throws RemoteException {
            return ProcessUtil.getPid();
        }

        @Override
        public List toList(int anInt, boolean aBoolean, float aFloat) throws RemoteException {
            List<String> retList = new ArrayList<>();
            retList.add(Integer.toString(anInt));
            retList.add(Boolean.toString(aBoolean));
            retList.add(Float.toString(aFloat));
            return retList;
        }

        @Override
        public Person getSomeone() throws RemoteException {
            return new Person("Test" + new Random().nextInt(10));
        }

        @Override
        public String multiThread(int i) throws RemoteException {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return String.format("ret [%d]:[%s]", i, Thread.currentThread().getName());
        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}
