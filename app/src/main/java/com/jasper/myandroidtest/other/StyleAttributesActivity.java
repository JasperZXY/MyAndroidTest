package com.jasper.myandroidtest.other;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jasper.myandroidtest.R;

public class StyleAttributesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = preferences.getString(SettingsActivity.THEME, SettingsActivity.WBBW);
        if (SettingsActivity.BBWW.equals(theme)) {
            setTheme(R.style.BlackBackgroundWhiteWordTheme);
        } else {
            setTheme(R.style.WhiteBackgroundBlackWordTheme);
        }
        setContentView(R.layout.activity_style_attributes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 1, "设置");
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(StyleAttributesActivity.this, SettingsActivity.class));
        finish();
        return super.onOptionsItemSelected(item);
    }
}
