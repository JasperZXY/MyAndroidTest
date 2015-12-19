package com.jasper.myandroidtest.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jasper.myandroidtest.R;

//TODO 还需深入理解getMeasuredWidth与getWidth
public class ViewActivity extends Activity {
    private EditText etLog;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        etLog = (EditText) findViewById(R.id.et_log);
        view = findViewById(R.id.view);

        view.measure(View.MEASURED_SIZE_MASK, View.MEASURED_SIZE_MASK);

        getData();
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

    }

    private void getData() {
        etLog.setText(String.format("MeasuredWidth:%s\nWidth:%s\nLeft:%s\nRight:%s",
                view.getMeasuredWidth(), view.getWidth(), view.getLeft(), view.getRight()));
    }
}
