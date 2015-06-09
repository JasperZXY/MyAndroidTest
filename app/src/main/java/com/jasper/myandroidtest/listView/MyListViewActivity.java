package com.jasper.myandroidtest.listView;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.listView.view.MyListView;

import java.util.ArrayList;
import java.util.List;

public class MyListViewActivity extends Activity implements MyListView.IMyListViewListener {
    private MyListView myListView;
    private List<String> items = new ArrayList<>();
    private ArrayAdapter adapter;
    private int start = 30;
    private int end = 50;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list_view);
        context = this;

        initItems();
        myListView = (MyListView) findViewById(R.id.mylistview);
        myListView.setPullUpLoadEnable(true);
        myListView.setPullDownRefreshEnable(true);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        myListView.setAdapter(adapter);
        myListView.setMyListViewListener(this);
    }

    private void initItems() {
        for (int i = end; i > start; i--) {
            items.add(Integer.toString(i));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_list_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_refresh) {
            myListView.refresh();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        new MyTask().execute(false);
    }

    @Override
    public void onLoadMore() {
        new MyTask().execute(true);
    }

    private class MyTask extends AsyncTask<Boolean, Void, Boolean> {
        private boolean isUp = false;

        @Override
        protected Boolean doInBackground(Boolean... params) {
            isUp = params[0];
            if (isUp) {
                if (start > 0) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                    }
                    for (int i = start; i > start - 10; i--) {
                        items.add(Integer.toString(i));
                    }
                    start -= 10;
                    return true;
                }
            } else {
                if (end < 100) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                    }
                    for (int i = end + 1; i <= end + 10; i++) {
                        items.add(0, Integer.toString(i));
                    }
                    end += 10;
                    return true;
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean flag) {
            if (flag) {
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(context, "没有数据了", Toast.LENGTH_SHORT).show();
            }
            myListView.stopRefresh();
            myListView.stopLoadMore();
        }
    }
}
