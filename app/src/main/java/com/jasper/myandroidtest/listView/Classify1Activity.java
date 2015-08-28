package com.jasper.myandroidtest.listView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.listView.adapter.Classify1Adapter;

public class Classify1Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify1);

        new PhoneInfo().getData(this);
        ListView listView = (ListView) findViewById(R.id.listView);
        Classify1Adapter adapter = new Classify1Adapter(this, new PhoneInfo().getData(this));
        listView.setAdapter(adapter);
    }


}
