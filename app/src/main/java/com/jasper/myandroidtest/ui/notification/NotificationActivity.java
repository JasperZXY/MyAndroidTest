package com.jasper.myandroidtest.ui.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.utils.Constants;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 注意处理好兼容性，Notification的有些API是要android 4.1才支持的，所以可以使用NotificationCompat
 * 监听Notification的清除用setDeleteIntent，但自定义布局实现不了
 */
public class NotificationActivity extends Activity implements View.OnClickListener {
    public static final int NOTIFY_ID_PRIVATE_MESSAGE = 1000;
    private static final int NOTIFY_ID_CUSTOM = 2000;
    private static final int NOTIFY_ID_PROGRESS = 10_000;
    private static final String INTENT_ACTION_DELETE_NOTIFICATION =
            "com.jasper.myandroidtest.intent_delete_notification";
    private static final String NOTIFY_ID = "notifyId";
    private static final int NOTIFY_CUSTOM_CLICK = 2001;

    private NotificationManager notificationManager;
    private TextView tvMsgCount;
    private EditText etLog;
    private ConcurrentMap<Integer, Thread> threadMap = new ConcurrentHashMap<>();
    private int threadTotal= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        findButtonAndSetOnClickListenr((ViewGroup) findViewById(R.id.layout_main));

        tvMsgCount = (TextView) findViewById(R.id.tv_msg_count);
        findViewById(R.id.layout_msg).setOnClickListener(this);
        etLog = (EditText) findViewById(R.id.et_log);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCount();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(INTENT_ACTION_DELETE_NOTIFICATION);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private void updateCount() {
        int count = PreferenceManager.getDefaultSharedPreferences(this)
                .getInt(Constants.PREFERENCE_PRIVATE_MESSAGE_COUNT, 0);
        if (count <= 0) {
            tvMsgCount.setVisibility(View.GONE);
        } else {
            tvMsgCount.setVisibility(View.VISIBLE);
            if (count > 99) {
                tvMsgCount.setText("99+");
            } else {
                tvMsgCount.setText(count + "");
            }
        }
    }

    private void findButtonAndSetOnClickListenr(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i) instanceof Button) {
                viewGroup.getChildAt(i).setOnClickListener(this);
            } else if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                findButtonAndSetOnClickListenr((ViewGroup) viewGroup.getChildAt(i));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_private_message:
                showNewMessge();
                break;
            case R.id.btn_progress:
                showProgress();
                break;
            case R.id.btn_custom:
                showCustom();
                break;
            case R.id.layout_msg:
                startActivity(new Intent(NotificationActivity.this, NotificationResultActivity.class));
                break;
        }
    }

    private void showNewMessge() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int count = sp.getInt(Constants.PREFERENCE_PRIVATE_MESSAGE_COUNT, 0);
        count ++;
        sp.edit().putInt(Constants.PREFERENCE_PRIVATE_MESSAGE_COUNT, count).commit();
        //在实际的项目，发送通知与接收通知可能不在同一个Activity，可以通过广播通知其他页面更新红点
        updateCount();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setAutoCancel(true)
                .setTicker("你有新私信！")
                .setContentTitle(String.format("你有%s条私信！", count))
                .setContentText("点击查看详细内容!")
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        Intent resultIntent = new Intent(this, NotificationResultActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        int requestCode = (int) (SystemClock.elapsedRealtime() / 1000);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this, requestCode,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        Intent intent = new Intent(INTENT_ACTION_DELETE_NOTIFICATION);
        intent.putExtra(NOTIFY_ID, NOTIFY_ID_PRIVATE_MESSAGE);
        mBuilder.setDeleteIntent(PendingIntent.getBroadcast(
                this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        Notification notification = mBuilder.build();
        notification.number = count;
        notificationManager.notify(NOTIFY_ID_PRIVATE_MESSAGE, notification);
    }

    private void showProgress() {
        threadTotal ++;
        final int notifyId = NOTIFY_ID_PROGRESS + threadTotal;

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setAutoCancel(true);
        mBuilder.setContentTitle("Picture Download:" + notifyId).setContentText("Download in progress");

        Intent intent = new Intent(INTENT_ACTION_DELETE_NOTIFICATION);
        intent.putExtra(NOTIFY_ID, notifyId);
        mBuilder.setDeleteIntent(PendingIntent.getBroadcast(
                this, notifyId, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        Thread thread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i <= 100; i += 5) {
                            if (! threadMap.containsKey(notifyId)) {
                                return;
                            }
                            mBuilder.setProgress(100, i, false);
                            notificationManager.notify(notifyId, mBuilder.build());
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                            }
                        }
                        mBuilder.setContentText("Download complete").setProgress(0, 0, false);
                        notificationManager.notify(notifyId, mBuilder.build());
                    }
                }
        );
        thread.start();
        threadMap.put(notifyId, thread);
    }

    private void showCustom() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_SOUND);

        Notification notification  = mBuilder.build();
        notification.contentView = new RemoteViews(getPackageName(), R.layout.notification_custom);

        Intent intent = new Intent(INTENT_ACTION_DELETE_NOTIFICATION);
        intent.putExtra(NOTIFY_ID, NOTIFY_ID_CUSTOM);
        mBuilder.setDeleteIntent(PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        intent.putExtra(NOTIFY_ID, NOTIFY_CUSTOM_CLICK);
        mBuilder.setContentIntent(PendingIntent.getBroadcast(
                this, NOTIFY_CUSTOM_CLICK, intent, PendingIntent.FLAG_UPDATE_CURRENT));
        notificationManager.notify(NOTIFY_ID_CUSTOM, notification);
    }

    //用于接收Notification删除的通知
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || context == null) {
                return;
            }

            int notyfyId = intent.getIntExtra(NOTIFY_ID, 0);
            if (NOTIFY_ID_PRIVATE_MESSAGE == notyfyId) {
                etLog.append("\n清除：私信");
            } else if (NOTIFY_ID_CUSTOM == notyfyId) {
                etLog.append("\n清除：自定义消息");   ////自定义消息删除操作没有识别
            } else if (NOTIFY_CUSTOM_CLICK == notyfyId) {
                etLog.append("\n点击：自定义消息点击");
            } else if (notyfyId > NOTIFY_ID_PROGRESS && notyfyId <= NOTIFY_ID_PROGRESS + threadTotal) {
                etLog.append("\n清除：下载任务:" + notyfyId);
                Thread thread = threadMap.get(notyfyId);
                if (thread != null) {
                    try {
                        threadMap.remove(notyfyId);
                        thread.join();
                    } catch (Exception e) {
                    }
                }
            } else {
                etLog.append("\n清除：不认识的类型");
            }
        }
    };


}
