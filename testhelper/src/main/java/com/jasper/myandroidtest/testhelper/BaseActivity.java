package com.jasper.myandroidtest.testhelper;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置允许在主线程访问网络，主要是为了简化操作，更符合服务端的写法
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);
    }
}
