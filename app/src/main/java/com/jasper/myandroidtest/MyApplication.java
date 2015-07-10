package com.jasper.myandroidtest;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

/**
 *  需要在AndroidManifest.xml中进行相应的配置
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    public void sayHello(Activity activity) {
        Log.i(TAG, "hello:" + activity);
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        Log.i(TAG, "onTerminate");
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        Log.i(TAG, "onLowMemory");
        super.onLowMemory();
    }
}
