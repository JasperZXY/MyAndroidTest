package com.jasper.myandroidtest.view;

import android.view.MotionEvent;
import android.view.View;

/**
 * 让View可以被拖动
 */
public class DragViewHelper {
    private View view;
    private int startx;
    private int starty;

    public DragViewHelper(View view) {
        this.view = view;
    }

    public void makeItCanDrag() {
        //这一步是必须的，不然就只有Touch的Action_DOWN事件
        view.setClickable(true);
        view.setOnTouchListener(new TouchListener());
    }

    private class TouchListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // 如果手指放在imageView上拖动
            // event.getRawX(); //获取手指第一次接触屏幕在x方向的坐标
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:// 获取手指第一次接触屏幕
                    startx = (int) event.getRawX();
                    starty = (int) event.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:// 手指在屏幕上移动对应的事件
                    int x = (int) event.getRawX();
                    int y = (int) event.getRawY();

                    // 获取手指移动的距离
                    int dx = x - startx;
                    int dy = y - starty;
                    // 得到imageView最开始的各顶点的坐标
                    int l = view.getLeft();
                    int r = view.getRight();
                    int t = view.getTop();
                    int b = view.getBottom();

                    // 更改imageView在窗体的位置
                    view.layout(l + dx, t + dy, r + dx, b + dy);

                    // 获取移动后的位置
                    startx = (int) event.getRawX();
                    starty = (int) event.getRawY();
                    break;
            }

            return false;
        }
    }


}
