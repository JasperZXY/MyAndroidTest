package com.jasper.myandroidtest.other;

import android.app.Activity;
import android.os.Bundle;

import com.jasper.myandroidtest.R;

public class SoftInputActivity extends Activity {
    public static final String WINDOW_SOFT_INPUT_MODE = "windowSoftInputMode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_input);

        int mode = getIntent().getIntExtra(WINDOW_SOFT_INPUT_MODE, 0);
        getWindow().setSoftInputMode(mode);
    }

}
