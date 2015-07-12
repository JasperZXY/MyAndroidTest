package com.jasper.myandroidtest.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import com.jasper.myandroidtest.R;

public class ServiceActivity extends Activity {
    private static final String TAG = "ServiceActivity";
    private SeekBar seekBar;
    private MyReceiver myReceiver;
    private Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekBar.setProgress(0);
                serviceIntent = new Intent(MyService.MY_SERVICE_ACTION);
                serviceIntent.putExtra(MyService.TYPE, ServiceType.PROGRESSBAR);
                startService(serviceIntent);
            }
        });

        //监听广播
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyService.MY_SERVICE_RECIVER);
        intentFilter.addCategory(ServiceType.PROGRESSBAR);
        registerReceiver(myReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        //停止服务
        stopService(serviceIntent);
        //注销广播
        unregisterReceiver(myReceiver);
        super.onDestroy();
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            seekBar.setProgress(intent.getIntExtra("process", 0));
        }
    }

}
