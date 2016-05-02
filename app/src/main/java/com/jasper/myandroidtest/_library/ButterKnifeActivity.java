package com.jasper.myandroidtest._library;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.Toast;

import com.jasper.myandroidtest.R;

import java.util.Date;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <strong>Butter Knife</strong><br/>
 * 项目地址：https://github.com/JakeWharton/butterknife<br/>
 */
public class ButterKnifeActivity extends Activity {
    @Bind(R.id.tv_time)TextView tvTime;
    @BindString(R.string.title_activity_butter_knife)String title;

    @OnClick(R.id.btn)
    void btnClick() {
        Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
        tvTime.setText(DateFormat.getTimeFormat(this).format(new Date()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_butter_knife);
        ButterKnife.bind(this);   //关键代码
    }
}
