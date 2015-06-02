package com.jasper.myandroidtest;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;


public class MainActivity extends Activity implements View.OnClickListener {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        findViewById(R.id.btn4fragmentTabHost).setOnClickListener(this);
        findViewById(R.id.btn4fragment_radiogroup).setOnClickListener(this);
        findViewById(R.id.btn4fragment_viewpager).setOnClickListener(this);
        findViewById(R.id.btn4AsyncTask).setOnClickListener(this);
        findViewById(R.id.btn4file_read_write).setOnClickListener(this);
        findViewById(R.id.btn4camera).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     *设置Action Bar隐藏菜单、子菜单能够显示图标
     * @param featureId
     * @param menu
     * @return
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true); // 设置Action Bar隐藏菜单、子菜单能够显示图标
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_search) {
            //AndroidManifest.xml中的android:theme配置需要去掉获取修改theme
            ActionBar actionBar = getActionBar();
            actionBar.setCustomView(R.layout.actionbar_view);
            final EditText search = (EditText) actionBar.getCustomView().findViewById(R.id.et_search);
            search.clearFocus();
            search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    Toast.makeText(MainActivity.this, "Search triggered:" + search.getText(),
                            Toast.LENGTH_LONG).show();
                    return false;
                }
            });
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        } else {
            Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
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
        }
    }
}
