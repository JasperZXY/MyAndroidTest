package com.jasper.myandroidtest.effect;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;

import com.jasper.myandroidtest.R;

/**
 * 手动控制软键盘的显示，软键盘显示时，让输入框能够推上去。
 * 如果单纯用SOFT_INPUT_ADJUST_PAN，那么是整个页面都会被推上去。
 * TODO 以前还可以用的，最近在魅族上测试不通过
 */
public class ManualSoftInputActivity extends Activity {
    public static final String TAG = "ManualSoftInputActivity";
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_soft_input);

        //除了SOFT_INPUT_ADJUST_NOTHING都可以
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        et = (EditText) findViewById(R.id.et);
        controlKeyboardLayout((ViewGroup)et.getParent());
    }

    /**
     * 当软键盘出现时，Layout能跟着软键盘推上去
     */
    private void controlKeyboardLayout(final ViewGroup layout) {
        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取layout在窗体的可视区域
                layout.getWindowVisibleDisplayFrame(rect);
                //获取layout在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int viewInvisibleHeight = layout.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于100，则键盘显示
                if (viewInvisibleHeight > 100) {
                    layout.scrollTo(0, viewInvisibleHeight);
                } else {
                    //键盘隐藏
                    layout.scrollTo(0, 0);
                }
            }
        });
    }
}
