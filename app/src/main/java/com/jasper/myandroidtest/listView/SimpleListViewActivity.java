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
    private ListView listView;
    private EditText etLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list_view);
        etLog = (EditText) findViewById(R.id.et_log);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new SimpleListViewAdapter(this));

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                etLog.setText("");
                return false;
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_TOUCH_SCROLL:
                        appendLog("onScrollStateChanged SCROLL_STATE_TOUCH_SCROLL");
                        break;
                    case SCROLL_STATE_FLING:
                        appendLog("onScrollStateChanged SCROLL_STATE_FLING");
                        break;
                    case SCROLL_STATE_IDLE:
                        appendLog("onScrollStateChanged SCROLL_STATE_IDLE");
                        break;
                    default:
                        appendLog("onScrollStateChanged");
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                appendLog("onScroll");
            }
        });
    }

    private void appendLog(String msg) {
        if ("".equals(etLog.getText().toString())) {
            etLog.append(msg);
        } else {
            etLog.append("\n" + msg);
        }
        Selection.setSelection(etLog.getText(), etLog.getText().length());
    }
}
