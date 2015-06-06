package com.jasper.myandroidtest.preference;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.jasper.myandroidtest.R;

public class EditPreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.edit_preference);
    }
}
