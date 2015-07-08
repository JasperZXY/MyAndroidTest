package com.jasper.myandroidtest.tabhost;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.jasper.myandroidtest.R;

public class TabTest1Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_test);

        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setText("tab1");
    }
}
