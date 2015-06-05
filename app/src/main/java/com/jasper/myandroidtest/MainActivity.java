package com.jasper.myandroidtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.jasper.myandroidtest.preference.MyPreferenceActivity;

public class MainActivity extends Activity implements View.OnClickListener {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        findViewById(R.id.btn4fragmentTabHost).setOnClickListener(this);
        findViewById(R.id.btn4fragment_radiogroup).setOnClickListener(this);
        findViewById(R.id.btn4fragment_viewpager).setOnClickListener(this);
        findViewById(R.id.btn4AsyncTask).setOnClickListener(this);
        findViewById(R.id.btn4file_read_write).setOnClickListener(this);
        findViewById(R.id.btn4camera).setOnClickListener(this);
        findViewById(R.id.btn4actionbar).setOnClickListener(this);
        findViewById(R.id.btn4preference).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn4fragmentTabHost:
                context.startActivity(new Intent(context, Tabs1Activity.class));
                break;
            case R.id.btn4fragment_radiogroup:
                context.startActivity(new Intent(context, Tabs2Activity.class));
                break;
            case R.id.btn4fragment_viewpager:
                context.startActivity(new Intent(context, Tabs3Activity.class));
                break;
            case R.id.btn4AsyncTask:
                context.startActivity(new Intent(context, AsyncTaskActivity.class));
                break;
            case R.id.btn4file_read_write:
                context.startActivity(new Intent(context, FileActivity.class));
                break;
            case R.id.btn4camera:
                context.startActivity(new Intent(context, CameraActivity.class));
                break;
            case R.id.btn4actionbar:
                context.startActivity(new Intent(context, MyActionBarActivity.class));
                break;
            case R.id.btn4preference:
                context.startActivity(new Intent(context, MyPreferenceActivity.class));
                break;
        }
    }
}
