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
    public static final String NAME = "name";
    public static final int MSG_GET_PROCESS_ID = 1;
    public static final int MSG_SAY_HELLO = 2;
    public static final int MSG_STOP = 3;

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
                case MSG_SAY_HELLO:
                    try {
                        String name = msg.getData().getString(NAME);
                        msgReply.getData().putString(DATA, "Hello " + name);
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
