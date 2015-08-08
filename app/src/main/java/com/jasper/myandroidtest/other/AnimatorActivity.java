package com.jasper.myandroidtest.other;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.dialog.ActionSheetDialog;

public class AnimatorActivity extends Activity {
    private ImageView imageView;
    private View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);
        imageView = (ImageView) findViewById(R.id.iv);
        layout = findViewById(R.id.layout);

        findViewById(R.id.btn_show_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showDialog() {
        new ActionSheetDialog(AnimatorActivity.this).builder()
                .setTitle("选择操作")
                .setCancelable(true)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("复原", new ActionSheetDialog.OnItemClickListener() {
                    @Override
                    public void onClick() {
                        revert();
                    }
                })
                .addSheetItem("自由落体-ValueAnimator实现", new ActionSheetDialog.OnItemClickListener() {
                    @Override
                    public void onClick() {
                        freefallWithValueAnimator();
                    }
                })
                .addSheetItem("自由落体-ObjectAnimator实现", new ActionSheetDialog.OnItemClickListener() {
                    @Override
                    public void onClick() {
                        freefallWithObjectAnimator();
                    }
                })
                .show();
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


}
