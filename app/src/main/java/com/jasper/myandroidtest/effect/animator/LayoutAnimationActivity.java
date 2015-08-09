package com.jasper.myandroidtest.effect.animator;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import com.jasper.myandroidtest.R;

public class LayoutAnimationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_animation);

        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_main);
        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1);
        sa.setDuration(1000);
        // 注意第二个参数delay，是浮点数，真正的delay时间是Duration*delay
        LayoutAnimationController lac = new LayoutAnimationController(sa, 0.5F);
        // 设置显示的顺序 这个必须要在delay不为0的时候才有效
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
        layout.setLayoutAnimation(lac);

    }

}
