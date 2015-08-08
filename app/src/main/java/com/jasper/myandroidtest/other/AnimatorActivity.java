package com.jasper.myandroidtest.other;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
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
                .addSheetItem("类抛物线", new ActionSheetDialog.OnItemClickListener() {
                    @Override
                    public void onClick() {
                        parabola();
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

    private void parabola() {
        ValueAnimator animator = ValueAnimator.ofObject(new TypeEvaluator<PointF>() {

            @Override
            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                PointF p = new PointF();
                p.x = fraction * (layout.getWidth() - imageView.getWidth());
                p.y = fraction * fraction * 0.5f * (layout.getHeight() - imageView.getHeight()) * 4f * 0.5f;
                return p;
            }
        }, new PointF(0, 0));
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(1000).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                imageView.setTranslationX(pointF.x);
                imageView.setTranslationY(pointF.y);
            }
        });
    }


}
