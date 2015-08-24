package com.jasper.myandroidtest.other;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Selection;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.jasper.myandroidtest.R;

import java.util.Date;

public class BroadcastReceiverActivity extends Activity {
    private static final String BROADCAST_SIMPLE = "com.jasper.myandroidtest.BROADCAST_SIMPLE";
    private static final String BROADCAST_ORDER = "com.jasper.myandroidtest.BROADCAST_ORDER";
    private static final String DATA = "data";

    private ToggleButton tgAbort;
    private EditText etLog;

    private SimpleReceiver simpleReceiver;
    private OrderReceiver1 orderReceiver1;
    private OrderReceiver2 orderReceiver2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_receiver);

        etLog = (EditText) findViewById(R.id.et_log);
        tgAbort = (ToggleButton) findViewById(R.id.tg_abort);
        tgAbort.setChecked(false);
        findViewById(R.id.btn_clean).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etLog.setText("");
            }
        });
        findViewById(R.id.btn_simple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BROADCAST_SIMPLE);
                //只在这个应用下发送广播
                intent.setPackage(getPackageName());
                intent.putExtra(DATA, new Date().toString());
                sendBroadcast(intent);
            }
        });
        findViewById(R.id.btn_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BROADCAST_ORDER);
                intent.putExtra(DATA, new Date().toString());
                sendOrderedBroadcast(intent, null);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilterSimple = new IntentFilter(BROADCAST_SIMPLE);
        simpleReceiver = new SimpleReceiver();
        registerReceiver(simpleReceiver, intentFilterSimple);

        IntentFilter intentFilter1 = new IntentFilter(BROADCAST_ORDER);
        intentFilter1.setPriority(1);
        orderReceiver1 = new OrderReceiver1();
        registerReceiver(orderReceiver1, intentFilter1);

        IntentFilter intentFilter2 = new IntentFilter(BROADCAST_ORDER);
        intentFilter2.setPriority(2);
        orderReceiver2 = new OrderReceiver2();
        registerReceiver(orderReceiver2, intentFilter2);

    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(simpleReceiver);
        unregisterReceiver(orderReceiver1);
        unregisterReceiver(orderReceiver2);
    }

    private void appendLog(String msg) {
        if ("".equals(etLog.getText().toString())) {
            etLog.append(msg);
        } else {
            etLog.append("\n" + msg);
        }
        Selection.setSelection(etLog.getText(), etLog.getText().length());
    }

    private class SimpleReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            appendLog("普通广播：" + intent.getStringExtra(DATA));
        }
    }

    private class OrderReceiver1 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            appendLog("有序广播1：" + intent.getStringExtra(DATA));
            if (tgAbort.isChecked()) {
                abortBroadcast();
            }
        }
    }

    private class OrderReceiver2 extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            appendLog("有序广播2：" + intent.getStringExtra(DATA));
            if (tgAbort.isChecked()) {
                abortBroadcast();
            }
        }
    }

}
