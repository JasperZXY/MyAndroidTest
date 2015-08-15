package com.jasper.myandroidtest.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jasper.myandroidtest.R;

public class SimpleActivity extends Activity {
    private static final int REQUEST_INPUT = 1;
    private static final String EDITTEXT_DATA = "et_data";

    private EditText et;
    private TextView tvState;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        //根据重力感应进行横竖屏切换设置，而不是默认的，因为有的手机可能禁止了横竖屏切换
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        et = (EditText) findViewById(R.id.et);
        tvState = (TextView) findViewById(R.id.tv_state);
        tvResult = (TextView) findViewById(R.id.tv_result);

        //startActivityForResult的应用
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), InputActivity.class), REQUEST_INPUT);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EDITTEXT_DATA, et.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            tvState.setText(savedInstanceState.getString(EDITTEXT_DATA, "获取失败"));
            //由于et有ID，所以这里就不调用et.setText()方法了
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_INPUT) {
            if (resultCode == RESULT_OK) {
                tvResult.setText("结果：" + data.getStringExtra(InputActivity.DATA));
            } else {
                tvResult.setText("取消了输入");
            }
        }
    }
}
