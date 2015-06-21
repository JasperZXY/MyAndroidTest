package com.jasper.myandroidtest.listView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.listView.adapter.PhoneInfoAdapter;
import com.jasper.myandroidtest.listView.entity.ContentItem;
import com.jasper.myandroidtest.listView.entity.GroupItem;
import com.jasper.myandroidtest.listView.entity.IItem;

import java.util.ArrayList;
import java.util.List;

public class PhoneInfoActivity extends Activity {
    private List<IItem> items;
    private ListView listView;
    private PhoneInfoAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_info);

        items = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);
        initData();
        adapter = new PhoneInfoAdapter(this, items);
        listView.setAdapter(adapter);
    }

    private void initData() {
        int j = 1;
        for (int i=0; i< 40; i++) {
            if (i == 0 || (i < 38 && Math.random() * 10 > 9)) {
                items.add(new GroupItem("组" + j));
                j ++;
            } else {
                items.add(new ContentItem("title" + i, "xxxxxx\nxxxxxxxxx"));
            }
        }
    }

}
