package com.jasper.myandroidtest.effect;

import android.app.Activity;
import android.os.Bundle;

import com.jasper.myandroidtest.R;

public class DragViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_view);
        new DragViewHelper(findViewById(R.id.view1)).makeItCanDrag();
        new DragViewHelper(findViewById(R.id.view2)).makeItCanDrag();
    }
}
