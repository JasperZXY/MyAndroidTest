package com.jasper.myandroidtest.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jasper.myandroidtest.R;

/**
 * Activity、Fragment跟Service通信，通过Broadcast进行数据的接收。
 * 这个例子要说明几个问题
 * 1、Intent的调用要用显式的，不然会报错
 * 2、可同时注册监听多个广播
 * 3、Fragment跟Service与Activity跟Service通信差不多
 * 4、可同时发送多个startService请求
 */
public class ServiceActivity extends Activity {
    private static final String TAG = "ServiceActivity";
    private SeekBar seekBar;
    private MyReceiver myReceiver1;
    private MyReceiver myReceiver2;
    private Intent serviceIntent;
    private TextView tv1;
    private TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        /**
         * android 5.0的会抛异常 Service Intent must be explicit
         * android 5.0以下的版本只是打了warn日志 Implicit intents with startService are not safe
         * 还是得显式调用
         */
//        serviceIntent = new Intent(MyService.MY_SERVICE_ACTION);
        serviceIntent = new Intent(getApplicationContext(), MyService.class);

        getFragmentManager().beginTransaction().add(R.id.layout, new FragmentPage()).commit();

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setProgress(0);
                serviceIntent.putExtra(MyService.TYPE, ServiceType.PROGRESSBAR);
                startService(serviceIntent);

                serviceIntent.putExtra(MyService.TYPE, ServiceType.TEST2);
                startService(serviceIntent);
            }
        });

        serviceIntent.putExtra(MyService.TYPE, ServiceType.TEST1);
        startService(serviceIntent);

        //监听广播
        myReceiver1 = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyService.MY_SERVICE_RECIVER);
        intentFilter.addCategory(ServiceType.PROGRESSBAR);
        intentFilter.addCategory(ServiceType.TEST1);
//        intentFilter.addCategory(ServiceType.TEST2);
        registerReceiver(myReceiver1, intentFilter);

        myReceiver2 = new MyReceiver();
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(MyService.MY_SERVICE_RECIVER);
        intentFilter2.addCategory(ServiceType.TEST2);
        registerReceiver(myReceiver2, intentFilter2);
    }

    @Override
    protected void onDestroy() {
        //停止服务
        stopService(serviceIntent);
        //注销广播
        unregisterReceiver(myReceiver1);
        unregisterReceiver(myReceiver2);
        super.onDestroy();
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getStringExtra(MyService.TYPE)) {
                case ServiceType.PROGRESSBAR:
                    seekBar.setProgress(intent.getIntExtra("process", 0));
                    break;
                case ServiceType.TEST1:
                    tv1.setText(intent.getStringExtra("data"));
                    break;
                case ServiceType.TEST2:
                    tv2.setText(intent.getStringExtra("data"));
                    break;
            }
        }
    }

}
