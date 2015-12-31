package com.jasper.myandroidtest.other;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.service.notification.NotificationListenerService;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.jasper.myandroidtest.R;

public class TipActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);

        findButtonAndSetOnClickListenr((ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0));


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
            case R.id.btn_ring:
                Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(),
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                ringtone.play();
                break;

            case R.id.btn_vibrator:
                //权限 <uses-permission android:name="android.permission.VIBRATE" />
                Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                vib.vibrate(300);
                break;

            case R.id.btn_led:
                //网上的基本都是通过Notification实现的，但我都没试成功
                Toast.makeText(getApplicationContext(), "没实现", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
