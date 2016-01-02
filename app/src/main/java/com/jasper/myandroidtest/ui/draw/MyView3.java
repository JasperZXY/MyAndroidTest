package com.jasper.myandroidtest.ui.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.SumPathEffect;
import android.util.AttributeSet;
import android.view.View;

/**
 * 画各种不同的线
 */
public class MyView3 extends View {

    private float phase;
    private PathEffect[] effects = new PathEffect[7];
    private int[] colors;
    private Paint paint;
    private Path path;

    public MyView3(Context context) {
        this(context, null);
    }

    public MyView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);

        //创建初始化Path
        path = new Path();
        path.moveTo(0, 0);    //设置绘制的起点在左上角
        for (int i = 1; i <= 15; i++) {
            path.lineTo(i * 20, (float) Math.random() * 60);
        }
        colors = new int[]{Color.BLACK, Color.BLUE, Color.CYAN,
                Color.GREEN, Color.MAGENTA, Color.RED, Color.DKGRAY};
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //将背景填充成白色
        canvas.drawColor(Color.WHITE);
        //-------下面开始初始化7中路径的效果
        //使用路径效果
        effects[0] = null;
        //实现平滑效果
        effects[1] = new CornerPathEffect(10);
        //打散Path的线段
        effects[2] = new DiscretePathEffect(3.0f, 3.0f);
        //虚线
        effects[3] = new DashPathEffect(new float[]{20, 10, 5, 10}, phase);

        Path p = new Path();
        p.addRect(0, 0, 8, 8, Path.Direction.CCW);
        effects[4] = new PathDashPathEffect(p, 12, phase, PathDashPathEffect.Style.ROTATE);
         /*
         组合效果，这个类需要两个PathEffect参数来构造一个实例，
         ComposePathEffect (PathEffect outerpe,PathEffect innerpe)，
         表现时，会首先将innerpe表现出来，然后再在innerpe的基础上去增加outerpe的效果。
         */
        effects[5] = new ComposePathEffect(effects[2], effects[4]);
        /*
        叠加效果，这个类也需要两个PathEffect作为参数
        SumPathEffect(PathEffect first,PathEffect second)，但与ComposePathEffect不同的是，
        在表现时，会分别对两个参数的效果各自独立进行表现，然后将两个效果简单的重叠在一起显示出来。
         */
        effects[6] = new SumPathEffect(effects[4], effects[3]);

        //将画布移到8,8处开始绘制  
        canvas.translate(10, 10);

        //依次使用7中不同路径效果,7种不同的颜色来绘制路径
        for (int i = 0; i < effects.length; i++) {
            paint.setPathEffect(effects[i]);
            paint.setColor(colors[i]);
            canvas.drawPath(path, paint);
            canvas.translate(0, 60);
        }

        //改变phase值,形成动画效果
        phase += 1;
        invalidate();
    }

}