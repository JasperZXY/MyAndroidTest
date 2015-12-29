package com.jasper.myandroidtest.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.jasper.myandroidtest.R;

public class ToastActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast);

        findButtonAndSetOnClickListenr((ViewGroup) ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0));
    }

    private void findButtonAndSetOnClickListenr(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i) instanceof Button) {
                viewGroup.getChildAt(i).setOnClickListener(this);
            } else if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                findButtonAndSetOnClickListenr((ViewGroup) viewGroup.getChildAt(i));
            }
        }
    }

    @Override
    public void onClick(View v) {
        Context context = getApplicationContext();
        switch (v.getId()) {
            case R.id.btn_simple:
                Toast toastSimple = Toast.makeText(context, "Hello toast!", Toast.LENGTH_SHORT);
                toastSimple.show();
                break;

            case R.id.btn_custom:
                Toast toastCustom = new Toast(context);
                toastCustom.setDuration(Toast.LENGTH_SHORT);
                toastCustom.setView(View.inflate(context, R.layout.notification_custom, null));
                toastCustom.show();
                break;
        }
    }
}
