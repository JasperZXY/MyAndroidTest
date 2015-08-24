package com.jasper.myandroidtest.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.*;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.utils.ProcessUtil;

import java.util.Random;

/**
 * 这个Activity要配合MessengerService一起看，实现用Messenger进行远程通信。
 * 为了实现远程服务调用，在mainfest中对Service的进程进行修改，使Activity跟Service不在同一个进程。
 *
 */
@SuppressLint("LongLogTag")
public class MessengerServiceActivity extends Activity {
    private static final String TAG = "MessengerServiceActivity";
    private EditText etLog;
    //这个Messenger用于发送请求
    private Messenger requestMessenger;
    //这个Messenger用于处理接收的数据
    private Messenger responseMessenger;
    private int serivicePid;

    private final int APPEND_LOG = 1000;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessengerService.MSG_ADD:
                    appendLog(msg.getData().getString(MessengerService.DATA));
                    break;
                case MessengerService.MSG_GET_PROCESS_ID:
                    serivicePid = msg.arg1;
                    appendLog(String.format("Service所在进程：[%d] %s",
                            serivicePid, ProcessUtil.getProcessName(getApplicationContext(), serivicePid)));
                    break;
                case MessengerService.MSG_MULTI_THREAD:
                    appendLog(msg.getData().getString(MessengerService.DATA));
                    break;
                case APPEND_LOG:
                    appendLog((String) msg.obj);
                    break;
                default:
                    appendLog("未知消息：" + msg.what);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_service);

        responseMessenger = new Messenger(handler);

        etLog = (EditText) findViewById(R.id.et_log);
        findViewById(R.id.btn_clean).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etLog.setText("");
            }
        });
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestMessenger == null) {
                    appendLog("未绑定");
                } else {
                    Random random = new Random();
                    Message msg = Message.obtain(null, MessengerService.MSG_ADD,
                            random.nextInt(100), random.nextInt(100));
                    //注意这里的参数，是把应答的Messenger设置进去
                    msg.replyTo = responseMessenger;
                    try {
                        requestMessenger.send(msg);
                    } catch (RemoteException e) {
                        appendLog("error:" + e.getLocalizedMessage());
                    }
                }
            }
        });
        findViewById(R.id.btn_multi_thread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestMessenger == null) {
                    appendLog("未绑定");
                } else {
                    for (int i=1; i<10; i++) {
                        final int a = i;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message message = Message.obtain(null, APPEND_LOG, a, 0);
                                message.obj = "发送请求：" + Thread.currentThread().getName() + " " + a;
                                handler.sendMessage(message);

                                Message msg = Message.obtain(null, MessengerService.MSG_MULTI_THREAD, a, 0);
                                //注意这里的参数，是把应答的Messenger设置进去
                                msg.replyTo = responseMessenger;
                                try {
                                    requestMessenger.send(msg);
                                } catch (RemoteException e) {
                                    message = Message.obtain(null, APPEND_LOG, a, 0);
                                    message.obj = "error:" + e.getLocalizedMessage();
                                    handler.sendMessage(message);
                                }
                            }
                        }).start();
                    }

                }
            }
        });

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
        bindService(new Intent(this, MessengerService.class),
                serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop requestMessenger:" + requestMessenger);
        if (requestMessenger != null) {
//            Message msg = Message.obtain(null, MessengerService.MSG_STOP, 0, 0);
//            try {
//                //发送不到Service端
//                requestMessenger.send(msg);
//            } catch (RemoteException e) {
//                appendLog("error:" + e.getLocalizedMessage());
//            }
            //用上面的方法发送请求发送不到Service，只好用下面的方式
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
            requestMessenger = new Messenger(service);

            //一绑定成功就去获取Service的进程ID
            Message msg = Message.obtain(null, MessengerService.MSG_GET_PROCESS_ID, 0, 0);
            msg.replyTo = responseMessenger;
            try {
                requestMessenger.send(msg);
            } catch (RemoteException e) {
                appendLog("error:" + e.getLocalizedMessage());
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.i(TAG, "onServiceDisconnected " + className);
            requestMessenger = null;
        }
    };


}
