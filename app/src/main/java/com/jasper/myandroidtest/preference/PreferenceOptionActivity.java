package com.jasper.myandroidtest.preference;

import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.jasper.myandroidtest.R;

/**
 * 注意这个类继承的是PreferenceActivity，而且不需要调用setContentView，不然不会报错
 */
public class PreferenceOptionActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_flight_preference);
        addPreferencesFromResource(R.xml.flightoptions);
    }
}
