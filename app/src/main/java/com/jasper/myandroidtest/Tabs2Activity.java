package com.jasper.myandroidtest;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用RadioGroup跟Fragment实现底部导航栏
 */
public class Tabs2Activity extends FragmentActivity {
    private static final String TAG = "Tabs2Activity";
    private RadioGroup rgs;
    public List<Fragment> fragments = new ArrayList<Fragment>();

    private FragmentTabAdapter tabAdapter;
    private GestureDetector gestureDetector;
    private int screenWidth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs2);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();

        fragments.add(new FragmentPage01());
        fragments.add(new FragmentPage02());
        fragments.add(new FragmentPage03());
        fragments.add(new FragmentPage04());
        fragments.add(new FragmentPage05());

        rgs = (RadioGroup) findViewById(R.id.tabs_rg);

        tabAdapter = new FragmentTabAdapter(this, fragments, R.id.tab_content, rgs);
        tabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int index) {
                Log.d(TAG, "Extra---- " + index + " checked!!! ");
            }
        });
        gestureDetector = new GestureDetector(this, onGestureListener);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() < -screenWidth/4) {
                if (tabAdapter.getCurrentTab() > 0) {
                    ((RadioButton)rgs.getChildAt(tabAdapter.getCurrentTab() - 1)).setChecked(true);
                }
            } else if (e1.getX() - e2.getX() > screenWidth/4) {
                if (tabAdapter.getCurrentTab() + 1 < fragments.size()) {
                    ((RadioButton)rgs.getChildAt(tabAdapter.getCurrentTab() + 1)).setChecked(true);
                }
            }
            return true;
        }
    };

}
