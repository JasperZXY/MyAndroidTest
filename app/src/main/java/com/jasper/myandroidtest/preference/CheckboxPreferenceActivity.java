package com.jasper.myandroidtest.preference;

import android.preference.PreferenceActivity;
import android.os.Bundle;

import com.jasper.myandroidtest.R;

public class CheckboxPreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.checkbox_preference);
    }

}
