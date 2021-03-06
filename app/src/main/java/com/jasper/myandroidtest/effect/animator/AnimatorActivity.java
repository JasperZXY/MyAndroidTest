package com.jasper.myandroidtest.effect.animator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.dialog.ActionSheetDialog;
import com.jasper.myandroidtest.effect.DragViewHelper;

import java.util.Random;

/**
 * Interpolators（插值器）
 * 系统默认的有Interpolators有
 * AccelerateInterpolator 加速
 * DecelerateInterpolator 减速
 * AccelerateDecelerateInterpolator 开始，和结尾都很慢，但是，中间加速
 * AnticipateInterpolator 开始向后一点，然后，往前抛
 * OvershootInterpolator 往前抛超过一点，然后返回来
 * AnticipateOvershootInterpolator 开始向后一点，往前抛过点，然后返回来
 * BounceInterpolator 结束的时候弹一下
 * LinearInterpolator 默认匀速
 */
public class AnimatorActivity extends Activity {
    private ImageView ivTarget;
    private ViewGroup layoutMain;
    private View viewFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animator);
        ivTarget = (ImageView) findViewById(R.id.iv_target);
        layoutMain = (ViewGroup) ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);

        viewFunction = findViewById(R.id.view_function);
        new DragViewHelper(viewFunction).makeItCanDrag();
        viewFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bubble();
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
                .addSheetItem("自由落体并慢慢变大-AnimatorSet实现", new ActionSheetDialog.OnItemClickListener() {
                    @Override
                    public void onClick() {
                        freefallWithAnimatorSet();
                    }
                })
                .addSheetItem("自由落体-xml实现", new ActionSheetDialog.OnItemClickListener() {
                    @Override
                    public void onClick() {
                        freefallWithXml();
                    }
                })
                .addSheetItem("类抛物线", new ActionSheetDialog.OnItemClickListener() {
                    @Override
                    public void onClick() {
                        parabola();
                    }
                })
                .addSheetItem("LayoutTransition-过渡动画", new ActionSheetDialog.OnItemClickListener() {
                    @Override
                    public void onClick() {
                        startActivity(new Intent(getApplicationContext(), LayoutTransitionActivity.class));
                        overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
                    }
                })
                .addSheetItem("LayoutAnimation-布局加载过程中的动画", new ActionSheetDialog.OnItemClickListener() {
                    @Override
                    public void onClick() {
                        startActivity(new Intent(getApplicationContext(), LayoutAnimationActivity.class));
                        overridePendingTransition(R.anim.abc_slide_in_top, R.anim.abc_slide_out_bottom);
                    }
                })
                .show();
    }

    private void revert() {
        ivTarget.setTranslationX(0);
        ivTarget.setTranslationY(0);
        ivTarget.setScaleX(1.0f);
        ivTarget.setScaleY(1.0f);
    }

    /**
     * 用ValueAnimator来产生一系列值，通过addUpdateListener监听变化值来实现想要的效果
     */
    private void freefallWithValueAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, layoutMain.getHeight() - ivTarget.getHeight());
        animator.setInterpolator(new BounceInterpolator());
        animator.setDuration(1000).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                ivTarget.setTranslationY(value);
            }
        });
    }

    /**
     * 测试结果，xml文件中的Interpolator要设置在顶级，不然无效，并且高度比较难把控，
     * 如果设置了fillAfter为true，那么最终的状态会保存，但复原无效。
     */
    private void freefallWithXml() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.freefall);
        ivTarget.startAnimation(animation);
    }

    /**
     * AnimatorSet的简单应用
     */
    private void freefallWithAnimatorSet() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(
                ivTarget, "translationY",
                0, layoutMain.getHeight() - ivTarget.getHeight());

        ObjectAnimator animator2 = ObjectAnimator.ofFloat(ivTarget, "scaleX", 1.0f, 1.5f);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(ivTarget, "scaleY", 1.0f, 1.5f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator1, animator2, animator3);
        animatorSet.setInterpolator(new BounceInterpolator());

        animatorSet.setDuration(1000).start();
    }

    /**
     * ObjectAnimator通过反射的方式来实现对设置属性的动画，
     * 当然这个也可以添加addUpdateListener来实现更复杂的动画，如同时添加透明度的变化。
     *
     * 可操纵的属性参数：x/y；scaleX/scaleY；rotationX/ rotationY；transitionX/ transitionY等等。
     * X是View最终的位置、translationX为最终位置与布局时初始位置的差。
     * 所以若就用translationX即为在原来基础上移动多少，X为最终多少。
     * getX()的值为getLeft()与getTranslationX()的和。
     *
     */
    private void freefallWithObjectAnimator() {
        //动画要修改的属性translationY，从0到(layoutMain.getHeight() - ivTarget.getHeight())
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                ivTarget, "translationY",
                0, layoutMain.getHeight() - ivTarget.getHeight());
        animator.setInterpolator(new BounceInterpolator());
        animator.setDuration(1000).start();
    }

    private void parabola() {
        ValueAnimator animator = ValueAnimator.ofObject(new TypeEvaluator<PointF>() {
            @Override
            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                PointF p = new PointF();
                p.x = fraction * (layoutMain.getWidth() - ivTarget.getWidth());
                p.y = fraction * fraction * 0.5f * (layoutMain.getHeight() - ivTarget.getHeight()) * 4f * 0.5f;
                return p;
            }
        }, new PointF(0, 0));
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(1000).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                ivTarget.setTranslationX(pointF.x);
                ivTarget.setTranslationY(pointF.y);
            }
        });
    }

    /**
     * 水泡效果
     * 动画属性包装类（HeartWrapper）的使用
     * onAnimationEnd监听器回调的使用
     */
    private void bubble() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
        float density = displayMetrics.density;
        int imgWidth = (int) (30 * density);
        final int imgHeight = imgWidth * bitmap.getHeight() / bitmap.getWidth();

        final Random random = new Random();
        final int startY = (int) (layoutMain.getHeight() - imgHeight - 20 * density);
        final int endY = startY / 2 - random.nextInt(100);
        final int left = layoutMain.getWidth() - 300;
        final int right = layoutMain.getWidth() - imgWidth - 20;
        final float scaleHeight = imgHeight * 0.2f;
        final float scaleRaiseHeight = imgHeight * 0.3f;

        final ImageView imageViewNew = new ImageView(this);
        imageViewNew.setImageBitmap(bitmap);
        //特别要注意的，setX是针对父控件而言的
        imageViewNew.setX(right - 15 * density);
        imageViewNew.setY(startY - 10 * density);
        layoutMain.addView(imageViewNew);
        imageViewNew.getLayoutParams().width = imgWidth;
        imageViewNew.getLayoutParams().height = imgHeight;

        HeartWrapper heartWrapper = new HeartWrapper();
        heartWrapper.alpha = 1;
        heartWrapper.x = imageViewNew.getX();
        heartWrapper.y = imageViewNew.getY();
        heartWrapper.scale = 0;

        ValueAnimator animator = ValueAnimator.ofObject(new TypeEvaluator<HeartWrapper>() {
            @Override
            public HeartWrapper evaluate(float fraction, HeartWrapper startValue, HeartWrapper endValue) {
                if (endValue.scale < 1) {
                    endValue.scale += 0.1f;
                    if (endValue.scale > 1.0f) {
                        endValue.scale = 1.0f;
                    }
                    endValue.alpha = 1;
                    //scaleHeight * endValue.scale 让变大的时候看起来不是从中间变大，而是从下面冲上去慢慢变大
                    //imgHeight * 0.1f * (1 - endValue.scale) 让变大的过程有个上升的空间
                    endValue.y = startY - scaleHeight * endValue.scale + scaleRaiseHeight * (1 - endValue.scale);
                    endValue.isAdd = random.nextBoolean();
                } else {
                    //防止震动太厉害采取的措施
                    if (random.nextDouble() < 0.015) {
                        endValue.isAdd = ! endValue.isAdd;
                    }
                    endValue.x += (random.nextFloat() * 2) * (endValue.isAdd ? 1 : -1);
                    //碰到边界的转方向
                    if (endValue.x > right) {
                        endValue.x = right;
                        endValue.isAdd = false;
                    }
                    if (endValue.x < left) {
                        endValue.x = left;
                        endValue.isAdd = true;
                    }
                    endValue.y = startY - (startY - endY) * fraction - scaleHeight;
                    if (fraction > 0.7) {
                        endValue.alpha = (float) (1 - (fraction - 0.7) / 0.3);
                    }
                }
                return endValue;
            }
        }, heartWrapper);
        animator.setDuration(3500).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                HeartWrapper heartWrapper = (HeartWrapper) animation.getAnimatedValue();
                imageViewNew.setScaleX(heartWrapper.scale);
                imageViewNew.setScaleY(heartWrapper.scale);
                imageViewNew.setX(heartWrapper.x);
                imageViewNew.setY(heartWrapper.y);
                imageViewNew.setAlpha(heartWrapper.alpha);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                layoutMain.removeView(imageViewNew);
                if (bitmap != null) {
                    bitmap.recycle();
                }
            }
        });
    }

    private class HeartWrapper {
        public float scale;
        public float x;
        public float y;
        public float alpha;
        public boolean isAdd;

        @Override
        public String toString() {
            return "HeartWrapper{" +
                    "scale=" + scale +
                    ", x=" + x +
                    ", y=" + y +
                    ", alpha=" + alpha +
                    ", isAdd=" + isAdd +
                    '}';
        }
    }

}
