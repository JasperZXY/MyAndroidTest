package com.jasper.myandroidtest.other;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.jasper.myandroidtest.R;

public class AnimatorActivity extends Activity implements View.OnClickListener {
    private ImageView imageView;
    private View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);
        imageView = (ImageView) findViewById(R.id.iv);
        layout = findViewById(R.id.layout);
        findViewById(R.id.btn_revert).setOnClickListener(this);
        findViewById(R.id.btn_freefall1).setOnClickListener(this);
        findViewById(R.id.btn_freefall2).setOnClickListener(this);
    }

    private void revert() {
        imageView.setTranslationX(0);
        imageView.setTranslationY(0);
    }

    /**
     * 用ValueAnimator来产生一系列值，通过addUpdateListener监听变化值来实现想要的效果
     */
    private void freefallWithValueAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, layout.getHeight() - imageView.getHeight());
        animator.setInterpolator(new BounceInterpolator());
        animator.setDuration(1000).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                imageView.setTranslationY(value);
            }
        });
    }

    /**
     * ObjectAnimator通过反射的方式来实现对设置属性的动画，
     * 当然这个也可以添加addUpdateListener来实现更复杂的动画，如同时添加透明度的变化
     */
    private void freefallWithObjectAnimator() {
        //动画要修改的属性translationY，从0到(layout.getHeight() - imageView.getHeight())
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                imageView, "translationY",
                0, layout.getHeight() - imageView.getHeight());
        animator.setInterpolator(new BounceInterpolator());
        animator.setDuration(1000).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_revert:
                revert();
                break;
            case R.id.btn_freefall1:
                freefallWithValueAnimator();
                break;
            case R.id.btn_freefall2:
                freefallWithObjectAnimator();
                break;
        }

    }
}
