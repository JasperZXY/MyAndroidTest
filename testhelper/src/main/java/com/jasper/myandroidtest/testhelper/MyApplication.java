package com.jasper.myandroidtest.testhelper;

import android.app.Application;
import android.util.Log;

/**
 *  需要在AndroidManifest.xml中进行相应的配置
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private static MyApplication myApplication = null;

    public static MyApplication getInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();
        myApplication = this;
    }

    @Override
    public void onTerminate() {
        Log.i(TAG, "onTerminate");
        super.onTerminate();
        System.exit(0);
    }

    @Override
    public void onLowMemory() {
        Log.i(TAG, "onLowMemory");
        super.onLowMemory();
    }
}
