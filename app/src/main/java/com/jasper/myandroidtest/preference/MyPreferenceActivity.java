package com.jasper.myandroidtest.preference;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jasper.myandroidtest.R;

/**
 * <ol>
 *     <li>ListPreference提供单选功能</li>
 * </ol>
 */
public class MyPreferenceActivity extends Activity implements View.OnClickListener {
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private EditText et;
    private Resources resources;
    private String key = "preference_test_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_preference);

        resources = getResources();
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        et = (EditText) findViewById(R.id.et);
        findViewById(R.id.btn_set).setOnClickListener(this);
        findViewById(R.id.btn_show).setOnClickListener(this);
        setValue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_preference, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setValue();
    }

    private void setValue() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String option = preferences.getString(resources.getString(R.string.selected_option), resources.getString(R.string.selected_option_default_index));
        String[] options = resources.getStringArray(R.array.preference_options);
        tv1.setText("listPreference:" + options[Integer.parseInt(option)]);

        tv2.setText(String.format("checkbox:key1[%s], key2[%s], key3[%s]",
                preferences.getBoolean("key1", false),
                preferences.getBoolean("key2", false),
                preferences.getBoolean("key3", false)));

        tv3.setText("EditTextPreference:" + preferences.getString("key", "默认"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.item_list:
                intent = new Intent(this, ListPreferenceActivity.class);
            break;
            case R.id.item_checkbox:
                intent = new Intent(this, CheckboxPreferenceActivity.class);
                break;
            case R.id.item_edit:
                intent = new Intent(this, EditPreferenceActivity.class);
                break;
            case R.id.item_my_list:
                intent = new Intent(this, MyListPreferenceActivity.class);
                break;
        }
        if (intent != null) {
            this.startActivityForResult(intent, 0);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        switch (v.getId()) {
            case R.id.btn_set:
                //自己手动写首选项
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(key, et.getText().toString());
                editor.commit();
                break;
            case R.id.btn_show:
                tv4.setText("手动设置:" + preferences.getString(key, "默认"));
                break;
        }
    }
}
