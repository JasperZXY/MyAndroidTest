package com.jasper.myandroidtest.testhelper;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import org.apache.log4j.Level;

import java.io.File;

import de.mindpipe.android.logging.log4j.LogConfigurator;

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
        initLog();
    }

    /**
     * 为了在Android中也能用服务端经常用的日志框架self4j，这里需要导入android-logging-log4j-1.0.3.jar、
     * log4j:log4j:1.2.17、org.slf4j:slf4j-api:1.7.21、org.slf4j:slf4j-log4j12:1.7.21
     */
    private void initLog() {
        LogConfigurator logConfigurator = new LogConfigurator();
        logConfigurator.setFileName(Environment.getExternalStorageDirectory()
                + File.separator + "logs" + File.separator + "com.jasper.myandroidtest.helper"
                + File.separator + "log4j.log");
        logConfigurator.setRootLevel(Level.DEBUG);
        logConfigurator.setLevel("org.apache", Level.ERROR);
        logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
        logConfigurator.setMaxFileSize(1024 * 1024 * 2);
        logConfigurator.setImmediateFlush(true);
        logConfigurator.configure();
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
