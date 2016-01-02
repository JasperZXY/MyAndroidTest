package com.jasper.myandroidtest.resource;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.jasper.myandroidtest.R;

/**
 * <a href="http://developer.android.com/guide/topics/resources/drawable-resource.html">参考文档</a>
 */
public class DrawableActivity extends Activity {

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawable);

        ((ImageView)findViewById(R.id.iv_level_max_0)).setImageLevel(0);
        ((ImageView)findViewById(R.id.iv_level_max_2)).setImageLevel(2);
        ((ImageView)findViewById(R.id.iv_level_max_6)).setImageLevel(6);

        ((ImageView)findViewById(R.id.iv_level_min_0)).setImageLevel(0);
        ((ImageView)findViewById(R.id.iv_level_min_3)).setImageLevel(3);
        ((ImageView)findViewById(R.id.iv_level_min_5)).setImageLevel(5);
    }

}
