package com.jasper.myandroidtest.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jasper.myandroidtest.R;

/**
 * 说明：
 * 当一个新的View出现的时候，其他View要立即执行CHANGE_APPEARING动画腾出位置，
 * 而新出现的View在一定延迟之后再执行APPEARING出现；
 * 相反地，一个View消失的时候，它需要先DISAPPEARING动画消失，
 * 而其他的View需要先等它消失后再执行CHANGE_DISAPPEARING。
 */
public class LayoutTransitionActivity extends Activity {
    private static final String TAG = "LayoutTransition";
    private LinearLayout layoutMain;
    private LayoutTransition mTransition = new LayoutTransition();
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_transition);
        layoutMain = (LinearLayout) findViewById(R.id.layout_main);
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                addButton();
            }
        });

        setupCustomAnimations();
        layoutMain.setLayoutTransition(mTransition);
    }

    public void addButton() {
        final Button button = new Button(this);
        button.setText("按钮 " + count);
        button.setTextColor(Color.WHITE);
        button.setTag("" + count);
        layoutMain.addView(button);
        button.setBackgroundResource(R.drawable.my_btn);
        button.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                layoutMain.removeView(button);
            }
        });
    }

    // 生成自定义动画
    private void setupCustomAnimations() {
        // 动画：CHANGE_APPEARING
        // Changing while Adding
        //这个动画暂时无效
        PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1);
        PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 0, 1);
        PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 0, 1);
        PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom", 0, 1);
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f, 1f);
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f, 1f);

        final ObjectAnimator changeIn = ObjectAnimator.ofPropertyValuesHolder(
                this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhScaleX, pvhScaleY)
                .setDuration(mTransition.getDuration(LayoutTransition.CHANGE_APPEARING));
        mTransition.setAnimator(LayoutTransition.CHANGE_APPEARING, changeIn);
        changeIn.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                Log.d(TAG, "CHANGE_APPEARING " + view.getTag());
                // View也支持此种动画执行方式了
                view.setScaleX(1f);
                view.setScaleY(1f);
            }
        });

        // 动画：CHANGE_DISAPPEARING
        // Changing while Removing
        Keyframe kf0 = Keyframe.ofFloat(0f, 0f);
        Keyframe kf1 = Keyframe.ofFloat(.9999f, 360f);
        Keyframe kf2 = Keyframe.ofFloat(1f, 0f);
        PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe(
                "rotation", kf0, kf1, kf2);
        final ObjectAnimator changeOut = ObjectAnimator
                .ofPropertyValuesHolder(this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhRotation)
                .setDuration(mTransition.getDuration(LayoutTransition.CHANGE_DISAPPEARING));
        mTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, changeOut);
        changeOut.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                Log.d(TAG, "CHANGE_DISAPPEARING " + view.getTag());
                view.setRotation(0f);
            }
        });

        // 动画：APPEARING
        // Adding
        ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "rotationY", 90f, 0f)
                .setDuration(mTransition.getDuration(LayoutTransition.APPEARING));
        mTransition.setAnimator(LayoutTransition.APPEARING, animIn);
        animIn.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                Log.d(TAG, "APPEARING " + view.getTag());
                view.setRotationY(0f);
            }
        });

        // 动画：DISAPPEARING
        // Removing
        ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "rotationX", 0f, 90f)
                .setDuration(mTransition.getDuration(LayoutTransition.DISAPPEARING));
        mTransition.setAnimator(LayoutTransition.DISAPPEARING, animOut);
        animOut.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator anim) {
                View view = (View) ((ObjectAnimator) anim).getTarget();
                Log.d(TAG, "DISAPPEARING " + view.getTag());
                view.setRotationX(0f);
            }
        });

    }

}
