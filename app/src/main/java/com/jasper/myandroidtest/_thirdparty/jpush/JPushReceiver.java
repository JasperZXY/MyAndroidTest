package com.jasper.myandroidtest._thirdparty.jpush;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.jasper.myandroidtest.MyApplication;
import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPushReceiver";
    private NotificationManager nm;
    private NotificationCompat.Builder nBuilder;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (nm == null) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nBuilder = new NotificationCompat.Builder(context);
            nBuilder.setSmallIcon(R.drawable.icon);
            nBuilder.setAutoCancel(true);
        }

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
            preferences.edit().putString(Constants.JPUSH_REG_ID, regId).commit();
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            processNotificationMessage(context, bundle);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    private void processCustomMessage(Context context, Bundle bundle) {
        JPushDetail jPushDetail = new JPushDetail();
        jPushDetail.setActionType("自定义消息");
        jPushDetail.setExtraMsgId(bundle.getString(JPushInterface.EXTRA_MSG_ID));
        jPushDetail.setExtraTitle(bundle.getString(JPushInterface.EXTRA_TITLE));
        jPushDetail.setExtraMessage(bundle.getString(JPushInterface.EXTRA_MESSAGE));
        jPushDetail.setExtraContentType(bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE));
        jPushDetail.setExtraExtra(bundle.getString(JPushInterface.EXTRA_EXTRA));

        nBuilder.setTicker("自定义消息");
        nBuilder.setContentTitle("自定义消息");
        nBuilder.setContentText(bundle.getString(JPushInterface.EXTRA_MESSAGE));
        nBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        Intent intent = new Intent(context, JPushDetailActivity.class);
        intent.putExtra(Constants.JPUSH_DETAIL_KEY, jPushDetail);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        nBuilder.setContentIntent(pendingIntent);
        nm.notify((int) (SystemClock.currentThreadTimeMillis() / 1000), nBuilder.build());
    }

    private void processNotificationMessage(Context context, Bundle bundle) {
        JPushDetail jPushDetail = new JPushDetail();
        jPushDetail.setActionType("通知栏消息");
        jPushDetail.setExtraMsgId(bundle.getString(JPushInterface.EXTRA_MSG_ID));
        jPushDetail.setExtraTitle(bundle.getString(JPushInterface.EXTRA_TITLE));
        jPushDetail.setExtraMessage(bundle.getString(JPushInterface.EXTRA_MESSAGE));
        jPushDetail.setExtraContentType(bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE));
        jPushDetail.setExtraExtra(bundle.getString(JPushInterface.EXTRA_EXTRA));

        Intent intent = new Intent(context, JPushDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.JPUSH_DETAIL_KEY, jPushDetail);
        context.startActivity(intent);
    }
}
