package com.jasper.myandroidtest.service.aidl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.Bundle;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.utils.ProcessUtil;

import java.util.List;
import java.util.Random;

public class MyAidlActivity extends Activity {
    private static final String TAG = "MyAdilActivity";
    private IMyAidlRemoteService myAidlRemoteService;
    private EditText etLog;
    private int serivicePid;
    private Handler handlerAppendLog = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            appendLog((String) msg.obj);
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_adil);

        etLog = (EditText) findViewById(R.id.et_log);
        findViewById(R.id.btn_clean).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etLog.setText("");
            }
        });
        findViewById(R.id.btn_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myAidlRemoteService == null) {
                    appendLog("未绑定");
                } else {
                    Random random = new Random();
                    try {
                        List<String> retList = myAidlRemoteService.toList(random.nextInt(10), random.nextBoolean(), random.nextFloat());
                        appendLog("处理结果：" + retList.toString());
                    } catch (RemoteException e) {
                        appendLog("error:" + e.getLocalizedMessage());
                    }
                }
            }
        });
        findViewById(R.id.btn_person).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myAidlRemoteService == null) {
                    appendLog("未绑定");
                } else {
                    try {
                        appendLog("处理结果：" + myAidlRemoteService.getSomeone());
                    } catch (RemoteException e) {
                        appendLog("error:" + e.getLocalizedMessage());
                    }
                }
            }
        });
        findViewById(R.id.btn_multi_thread).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (myAidlRemoteService == null) {
                            appendLog("未绑定");
                        } else {
                            for (int i = 1; i < 10; i++) {
                                final int a = i;
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Message message = new Message();
                                        message.obj = "发送请求：" + Thread.currentThread().getName() + " " + a;
                                        handlerAppendLog.sendMessage(message);

                                        message = new Message();
                                        try {
                                            message.obj = myAidlRemoteService.multiThread(a);
                                        } catch (RemoteException e) {
                                            message.obj = "multiThread " + a + " error:" + e.getLocalizedMessage();
                                        }
                                        handlerAppendLog.sendMessage(message);
                                    }
                                }).start();
                            }
                        }
                    }
                }

        );

        appendLog(String.format("Activity所在进程：[%d] %s",
                ProcessUtil.getPid(), ProcessUtil.getProcessName(this)));
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
        appendLog("Connecting...");
        //这里只能用隐式的调用方式，所以要setPackage
        Intent intent = new Intent("com.jasper.myandroidtest.service.aidl.IMyAidlRemoteService");
        intent.setPackage("com.jasper.myandroidtest");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop myAidlRemoteService:" + myAidlRemoteService);
        if (myAidlRemoteService != null) {
            if (serivicePid > 0) {
                android.os.Process.killProcess(serivicePid);
            }
            unbindService(serviceConnection);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            myAidlRemoteService = IMyAidlRemoteService.Stub.asInterface(service);

            appendLog("Connected");
            try {
                serivicePid = myAidlRemoteService.getPid();
                appendLog(String.format("Service所在进程：[%d] %s",
                        serivicePid, ProcessUtil.getProcessName(getApplicationContext(), serivicePid)));
            } catch (RemoteException e) {
                appendLog("获取pid错误:" + e.getLocalizedMessage());
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            myAidlRemoteService = null;
        }
    };

}
