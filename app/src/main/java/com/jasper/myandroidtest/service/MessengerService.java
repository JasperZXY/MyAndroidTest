package com.jasper.myandroidtest.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.jasper.myandroidtest.utils.ProcessUtil;

public class MessengerService extends Service {
    private static final String TAG = "MessengerService";
    public static final String DATA = "data";
    public static final int MSG_GET_PROCESS_ID = 1;
    public static final int MSG_ADD = 2;
    public static final int MSG_MULTI_THREAD = 3;
    public static final int MSG_STOP = 100;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Message msgReply = new Message();
            msgReply.what = msg.what;

            switch (msg.what) {
                case MSG_GET_PROCESS_ID:
                    try {
                        msgReply.arg1 = ProcessUtil.getPid();
                        msg.replyTo.send(msgReply);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG_ADD:
                    try {
                        msgReply.getData().putString(DATA,
                                String.format("%d + %d = %d", msg.arg1, msg.arg2, msg.arg1 + msg.arg2));
                        msg.replyTo.send(msgReply);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG_MULTI_THREAD:
                    try {
                        Thread.sleep(500);
                        msgReply.getData().putString(DATA,
                                "ret " + msg.arg1 + ":" + Thread.currentThread().getName());
                        msg.replyTo.send(msgReply);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG_STOP:
                    stopSelf();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private final Messenger messenger = new Messenger(handler);

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}
