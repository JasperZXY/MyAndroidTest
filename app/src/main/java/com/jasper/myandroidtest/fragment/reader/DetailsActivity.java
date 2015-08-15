package com.jasper.myandroidtest.fragment.reader;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

public class DetailsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //根据重力感应进行横竖屏切换设置，而不是默认的，因为有的手机可能禁止了横竖屏切换
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        if (savedInstanceState == null) {
            DetailsFragment details = new DetailsFragment();
            details.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
        }
    }
}