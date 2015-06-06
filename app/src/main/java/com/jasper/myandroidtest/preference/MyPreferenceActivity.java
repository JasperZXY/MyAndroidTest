package com.jasper.myandroidtest.preference;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.jasper.myandroidtest.R;

public class MyPreferenceActivity extends Activity {
    private TextView tv;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_preference);

        resources = getResources();
        tv = (TextView) findViewById(R.id.tv);
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
        String[] options = resources.getStringArray(R.array.flight_options);
        tv.setText(options[Integer.parseInt(option)]);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.item_pre) {
            Intent intent = new Intent(this, PreferenceOptionActivity.class);
            this.startActivityForResult(intent, 0);
        }
        return true;
    }
}
