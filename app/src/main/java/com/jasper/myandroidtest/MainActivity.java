package com.jasper.myandroidtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jasper.myandroidtest.dialog.DialogActivity;
import com.jasper.myandroidtest.listView.MyListViewActivity;
import com.jasper.myandroidtest.listView.SimpleListViewActivity;
import com.jasper.myandroidtest.preference.MyPreferenceActivity;

public class MainActivity extends Activity implements View.OnClickListener {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        findButtonAndSetOnClickListenr((ViewGroup) findViewById(R.id.layout_main));
    }

    private void findButtonAndSetOnClickListenr(ViewGroup viewGroup) {
        for (int i=0; i<viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i) instanceof Button) {
                viewGroup.getChildAt(i).setOnClickListener(this);
            } else if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                findButtonAndSetOnClickListenr((ViewGroup) viewGroup.getChildAt(i));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            case R.id.btn4simpleListView:
                context.startActivity(new Intent(context, SimpleListViewActivity.class));
                break;
            case R.id.btn4refresh:
                context.startActivity(new Intent(context, MyListViewActivity.class));
                break;
            case R.id.btn4imageview:
                context.startActivity(new Intent(context, ImageViewActivity.class));
                break;
            case R.id.btn4menu:
                context.startActivity(new Intent(context, MenuActivity.class));
                break;
            case R.id.btn4permission:
                context.startActivity(new Intent(context, PermissionActivity.class));
                break;
            case R.id.btn4dialog:
                context.startActivity(new Intent(context, DialogActivity.class));
                break;
        }
    }
}
