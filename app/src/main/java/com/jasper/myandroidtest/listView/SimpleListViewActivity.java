package com.jasper.myandroidtest.listView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.jasper.myandroidtest.R;

public class SimpleListViewActivity extends Activity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list_view);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new SimpleListViewAdapter(this));
    }
}
