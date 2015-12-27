package com.jasper.myandroidtest.ui.notification;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.utils.Constants;

public class NotificationResultActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_result);

        //未读数清零
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit().putInt(Constants.PREFERENCE_PRIVATE_MESSAGE_COUNT, 0).commit();

        //清除通知
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                .cancel(NotificationActivity.NOTIFY_ID_PRIVATE_MESSAGE);
    }
}
