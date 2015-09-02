package com.jasper.myandroidtest.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import com.jasper.myandroidtest.R;

/**
 * FlowLayout 使用
 */
public class FlowLayoutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlowLayout layout = new FlowLayout(this);
        for (int i=1; i<=81; i++) {
            Button button = new Button(this);
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            params.setMargins(20, 20, 0, 0);
            button.setLayoutParams(params);
            button.setTextSize((float) (12 + Math.random() * 12));
            button.setText("button" + i);
            layout.addView(button);
        }

        ScrollView scrollView = new ScrollView(this);
        scrollView.setBackgroundResource(R.drawable.bg);
        scrollView.addView(layout);
        setContentView(scrollView);
    }
}
