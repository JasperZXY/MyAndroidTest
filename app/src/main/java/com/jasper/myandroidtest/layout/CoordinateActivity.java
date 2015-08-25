package com.jasper.myandroidtest.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.jasper.myandroidtest.R;

public class CoordinateActivity extends Activity {
    private ImageView iv1;
    private ImageView iv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinate);

        iv1 = (ImageView) findViewById(R.id.iv_1);
        iv2 = (ImageView) findViewById(R.id.iv_2);

        //这样设置了，还是在父控件的左上角，说明是针对父控件来说的
        iv1.setX(0);
        iv1.setY(0);

        //获取控件宽高的方法
        iv1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                iv1.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                iv2.setX(iv1.getWidth() / 2);
                iv2.setY(iv1.getHeight() / 2);
            }
        });

    }

}
