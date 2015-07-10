package com.jasper.myandroidtest.listView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.listView.adapter.Classify2Adapter;

public class Classify2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify2);

        new PhoneInfo().getData(this);
        ListView listView = (ListView) findViewById(R.id.listView);
        Classify2Adapter adapter = new Classify2Adapter(this, new PhoneInfo().getData(this));
        listView.setAdapter(adapter);
    }

}
