package com.jasper.myandroidtest.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Selection;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.jasper.myandroidtest.R;

/**
 * 与BinderService进行搭配使用
 * Bound Service的使用-Binder
 */
public class BinderServiceActivity extends Activity {
    private static final String TAG = "BinderServiceActivity";
    private EditText etLog;
    private BinderService binderService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_service);

        etLog = (EditText) findViewById(R.id.et_log);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binderService == null) {
                    appendLog("未绑定");
                } else {
                    appendLog(Integer.toString(binderService.getRandomNumber()));
                }
            }
        });
    }

    private void appendLog(String msg) {
        if ("".equals(etLog.getText().toString())) {
            etLog.append(msg);
        } else {
            etLog.append("\n" + msg);
        }
        Selection.setSelection(etLog.getText(), etLog.getText().length());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BinderService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop binderService:" + binderService);
        if (binderService != null) {
            unbindService(serviceConnection);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            binderService = ((BinderService.MyBinder)binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binderService = null;
        }
    };

}
