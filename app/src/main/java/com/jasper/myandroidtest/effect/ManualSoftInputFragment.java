package com.jasper.myandroidtest.effect;

import android.app.Fragment;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.jasper.myandroidtest.R;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 原先这部分代码是写在ManualSoftInputActivity中的，这里是为了验证是否放到Fragment中也能正常控制。
 *
 * @author Jasper
 */
public class ManualSoftInputFragment extends Fragment {

    public static final String TAG = "ManualSoftInputFragment";
    private EditText et;
    //输入框所在父控件及输入框下面的控件
    private RelativeLayout layoutInput;
    private int etHeight = 0;

    //整个View
    private ViewGroup mainLayout;
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainLayout = (ViewGroup) inflater.inflate(R.layout.activity_manual_soft_input_fragment, null);

        et = (EditText) mainLayout.findViewById(R.id.et);
        /**
         * 注意这里的布局，layout必须要为RelativeLayout才能成功，
         * layout里面先画其他的，然后再画EditText，让EditText在其他控件上面即可
         */
        layoutInput = (RelativeLayout) mainLayout.findViewById(R.id.layout_input);
        controlKeyboardLayout();
        return mainLayout;
    }

    private void controlKeyboardLayout() {
        final AtomicBoolean isShowKeyboard = new AtomicBoolean(false);

        layoutInput.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (etHeight <= 0) {
                    //计算输入框的高度
                    etHeight = et.getHeight();
                    return;
                }
                Rect rect = new Rect();
                //获取layout在窗体的可视区域
                layoutInput.getWindowVisibleDisplayFrame(rect);
                //获取layout在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int viewInvisibleHeight = layoutInput.getRootView().getHeight() - rect.bottom;
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) et.getLayoutParams();
                //若不可视区域高度大于100，则键盘显示
                if (viewInvisibleHeight > 100) {
                    //这里一定要加判断，不然输入框在输入回车后，输入框无法回车跟进
                    if (!isShowKeyboard.get()) {
                        layoutInput.scrollTo(0, viewInvisibleHeight);
                        layoutParams.height = etHeight - viewInvisibleHeight;
                        et.setLayoutParams(layoutParams);
                    }
                    isShowKeyboard.set(true);
                } else {
                    //键盘隐藏
                    //这里一定要加判断，不然输入框的内容无法显示完
                    if (isShowKeyboard.get()) {
                        layoutInput.scrollTo(0, 0);
                        layoutParams.height = etHeight;
                        et.setLayoutParams(layoutParams);
                    }
                    isShowKeyboard.set(false);
                }
            }
        });
    }
}
