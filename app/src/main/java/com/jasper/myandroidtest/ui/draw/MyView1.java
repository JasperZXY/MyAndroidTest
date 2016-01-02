package com.jasper.myandroidtest.ui.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 用于属性基本概念
 */
public class MyView1 extends View {
    private Paint paint = new Paint();

    public MyView1(Context context) {
        this(context, null);
    }

    public MyView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置画笔颜色
        paint.setColor(Color.RED);
        //实心
        paint.setStyle(Paint.Style.FILL);
        canvas.drawColor(Color.WHITE);
        //绘制直线
        canvas.drawLine(50, 50, 450, 50, paint);
        //绘制矩形
        canvas.drawRect(100, 100, 200, 200, paint);

        //空心
        paint.setStyle(Paint.Style.STROKE);
        //绘制矩形
        canvas.drawRect(300, 100, 400, 200, paint);
    }

}