package com.jasper.myandroidtest.listView;

import android.app.Activity;
import android.os.Bundle;
import android.text.Selection;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.listView.adapter.SimpleListViewAdapter;

public class SimpleListViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list_view);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new SimpleListViewAdapter(this));
    }
}
