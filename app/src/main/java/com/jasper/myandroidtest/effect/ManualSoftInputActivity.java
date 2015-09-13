package com.jasper.myandroidtest.effect;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.jasper.myandroidtest.R;

/**
 * 手动控制软键盘的显示，软键盘显示时，让输入框能够能够缩小，输入框以下部分能够推上去。
 * 如果单纯用SOFT_INPUT_ADJUST_PAN，那么是整个页面都会被推上去。
 */
public class ManualSoftInputActivity extends Activity {
    public static final String TAG = "ManualSoftInputActivity";
    private EditText et;
    private RelativeLayout layout;
    private int etHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_soft_input);

        //除了SOFT_INPUT_ADJUST_NOTHING都可以
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        et = (EditText) findViewById(R.id.et);
        /**
         * 注意这里的布局，layout必须要为RelativeLayout才能成功，
         * layout里面先画其他的，然后再画EditText，让EditText在其他控件上面即可
         */
        layout = (RelativeLayout) findViewById(R.id.layout);
        controlKeyboardLayout();
    }

    private void controlKeyboardLayout() {
        //由于需要final，这里用数组
        final boolean[] isShowKeyboard = {false};

        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (etHeight <= 0) {
                    //计算输入框的高度
                    etHeight = et.getHeight();
                    return;
                }
                Rect rect = new Rect();
                //获取layout在窗体的可视区域
                layout.getWindowVisibleDisplayFrame(rect);
                //获取layout在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int viewInvisibleHeight = layout.getRootView().getHeight() - rect.bottom;
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) et.getLayoutParams();
                //若不可视区域高度大于100，则键盘显示
                if (viewInvisibleHeight > 100) {
                    //这里一定要加判断，不然输入框在输入回车后，输入框无法回车跟进
                    if (! isShowKeyboard[0]) {
                        layout.scrollTo(0, viewInvisibleHeight);
                        layoutParams.height = etHeight - viewInvisibleHeight;
                        et.setLayoutParams(layoutParams);
                    }
                    isShowKeyboard[0] = true;
                } else {
                    //键盘隐藏
                    //这里一定要加判断，不然输入框的内容无法显示完
                    if (isShowKeyboard[0]) {
                        layout.scrollTo(0, 0);
                        layoutParams.height = etHeight;
                        et.setLayoutParams(layoutParams);
                    }
                    isShowKeyboard[0] = false;
                }
            }
        });
    }
}
