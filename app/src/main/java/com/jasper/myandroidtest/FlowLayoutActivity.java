package com.jasper.myandroidtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import com.jasper.myandroidtest.view.FlowLayout;

/**
 * FlowLayout 使用
 */
public class FlowLayoutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScrollView scrollView = new ScrollView(this);
        FlowLayout layout = new FlowLayout(this);
        for (int i=1; i<=81; i++) {
            Button button = new Button(this);
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                    ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            params.setMargins(20, 20, 0, 0);
            button.setLayoutParams(params);
            button.setPadding(30, 50, 30, 50);
            button.setTextSize((float) (12 + Math.random() * 12));
            button.setText("button" + i);
            layout.addView(button);
        }
        scrollView.addView(layout);
        setContentView(scrollView);
    }
}
