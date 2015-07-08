package com.jasper.myandroidtest.tabhost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jasper.myandroidtest.R;

public class TabTest1Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_test);

        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setText("tab1");

        final Context context = this;
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, Tabs1Activity.class));
            }
        });
    }
}
