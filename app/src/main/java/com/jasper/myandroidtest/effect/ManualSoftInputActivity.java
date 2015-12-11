package com.jasper.myandroidtest.effect;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.jasper.myandroidtest.R;

/**
 * 手动控制软键盘的显示，软键盘显示时，让输入框能够能够缩小，输入框以下部分能够推上去。
 * 如果单纯用SOFT_INPUT_ADJUST_PAN，那么是整个页面都会被推上去。
 */
public class ManualSoftInputActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_soft_input);

        //除了SOFT_INPUT_ADJUST_NOTHING都可以
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        getFragmentManager().beginTransaction().replace(R.id.layout_container, new ManualSoftInputFragment()).commit();

    }

}
