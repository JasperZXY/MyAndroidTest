package com.jasper.myandroidtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jasper.myandroidtest.camera.CameraActivity;
import com.jasper.myandroidtest.dialog.DialogActivity;
import com.jasper.myandroidtest.fragmentManager.FragmentManagerActivity;
import com.jasper.myandroidtest.listView.MyListViewActivity;
import com.jasper.myandroidtest.listView.PhoneInfoActivity;
import com.jasper.myandroidtest.listView.SimpleListViewActivity;
import com.jasper.myandroidtest.preference.MyPreferenceActivity;
import com.jasper.myandroidtest.sensor.GradienterActivity;
import com.jasper.myandroidtest.sensor.SimpleSensorActivity;
import com.jasper.myandroidtest.utils.BitmapCache;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn4fragmentTabHost:
                startActivity(new Intent(this, Tabs1Activity.class));
                break;
            case R.id.btn4fragment_radiogroup:
                startActivity(new Intent(this, Tabs2Activity.class));
                break;
            case R.id.btn4fragment_viewpager:
                startActivity(new Intent(this, Tabs3Activity.class));
                break;
            case R.id.btn4AsyncTask:
                startActivity(new Intent(this, AsyncTaskActivity.class));
                break;
            case R.id.btn4file_read_write:
                startActivity(new Intent(this, FileActivity.class));
                break;
            case R.id.btn4camera:
                startActivity(new Intent(this, CameraActivity.class));
                break;
            case R.id.btn4actionbar:
                startActivity(new Intent(this, MyActionBarActivity.class));
                break;
            case R.id.btn4preference:
                startActivity(new Intent(this, MyPreferenceActivity.class));
                break;
            case R.id.btn4simpleListView:
                startActivity(new Intent(this, SimpleListViewActivity.class));
                break;
            case R.id.btn4refresh:
                startActivity(new Intent(this, MyListViewActivity.class));
                break;
            case R.id.btn4imageview:
                startActivity(new Intent(this, ImageViewActivity.class));
                break;
            case R.id.btn4menu:
                startActivity(new Intent(this, MenuActivity.class));
                break;
            case R.id.btn4permission:
                startActivity(new Intent(this, PermissionActivity.class));
                break;
            case R.id.btn4dialog:
                startActivity(new Intent(this, DialogActivity.class));
                break;
            case R.id.btn4FragmentManager:
                startActivity(new Intent(this, FragmentManagerActivity.class));
                break;
            case R.id.btn4WebView:
                startActivity(new Intent(this, WebViewActivity.class));
                break;
            case R.id.btn4BitmapCache:
                startActivity(new Intent(this, BitmapCacheActivity.class));
                break;
            case R.id.btn4simple_sensor:
                startActivity(new Intent(this, SimpleSensorActivity.class));
                break;
            case R.id.btn4gradienter:
                startActivity(new Intent(this, GradienterActivity.class));
                break;
            case R.id.btn4phone_info:
                startActivity(new Intent(this, PhoneInfoActivity.class));
                break;
            case R.id.btn4Matrix:
                startActivity(new Intent(this, MatrixActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        BitmapCache.getInstance().clearCache();
        super.onDestroy();
    }
}
