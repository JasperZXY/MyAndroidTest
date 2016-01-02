package com.jasper.myandroidtest.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jasper.myandroidtest.MainActivity;
import com.jasper.myandroidtest.R;

/**
 * 来自网络：http://blog.csdn.net/zd_1471278687/article/details/20376377
 */
public class DragDropActivity extends Activity {
    /**
     * 为ImageView创建一个字符标签
     */
    private static final String IMAGEVIEW_TAG = "icon bitmap";
    /**
     * 创建用于拖动的ImageView
     */
    private ImageView imageView;
    /**
     * 拖动事件监听
     */
    private myDragEventListener mDragListen;
    /**
     * 拖动ImageView的长按事件监听
     */
    private View.OnLongClickListener mLongClick = new View.OnLongClickListener() {

        @SuppressLint("NewApi")
        @Override
        public boolean onLongClick(View v) {
            // 创建一个ClipData对象
            // 这里分为两步，第一步中方法ClipData.newPlainText()可以创建一个纯文本ClipData

            // 根据ImageView的标签创建一个ClipData.Item对象
            ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());

            // 使用标签，纯文本和已经创建的item来创建一个ClipData对象
            // 这里将在ClipData中创建一个新的ClipDescription对象并设置它的MIME类型为"text/plain"
            ClipData dragData = new ClipData((CharSequence) v.getTag(),
                    new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);

            // 实例化拖拽影子.
            View.DragShadowBuilder myShadow = new MyDragShadowBuilder(imageView);

            // 开始拖拽

            return v.startDrag(dragData, // 被拖拽的数据
                    myShadow, // 拖拽的影子
                    null, // 不需要使用本地数据
                    0 // 标记（目前用不到，设置为0）
            );
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_drop);

        init();
    }

    /**
     * 功能描述: 初始化<br>
     */
    public void init() {
        imageView = new ImageView(this);
        imageView = (ImageView) findViewById(R.id.drag_image);
        // 设置标签
        imageView.setTag(IMAGEVIEW_TAG);
        //添加长按事件
        imageView.setOnLongClickListener(mLongClick);
        // 创建一个拖拽的事件监听器
        mDragListen = new myDragEventListener();
        // 拖拽反应区域
        View view = findViewById(R.id.drag_room);
        // 为该区域添加拖动事件监听器
        view.setOnDragListener(mDragListen);
    }

    /**
     * 〈拖动阴影〉<br>
     */
    @SuppressLint("NewApi")
    private static class MyDragShadowBuilder extends View.DragShadowBuilder {
        // 拖动阴影的图像， 作为一个drawable来定义
        private static Drawable shadow;

        // 构造函数
        public MyDragShadowBuilder(ImageView v) {
            // 通过myDragShadowBuilder存储View参数
            super(v);
            // 创建一个可拖拽的图像，此图像可以通过系统的Canvas来填充
            //            shadow = new ColorDrawable(Color.LTGRAY);
            //这里的处理有待优化，本意是要创建一个跟本体一样的Shadow
            shadow = v.getResources().getDrawable(R.drawable.android);
        }

        // 定义一个回调方法，将阴影的维度和触摸点返回给系统
        @Override
        public void onProvideShadowMetrics(Point size, Point touch) {
            // 定义当地的变量
            int width;
            int height;
            // 设置阴影的宽度为视图的1.5倍
            width = (int) (getView().getWidth() * 1.5f);
            // 设置阴影的高度为视图一半
            height = (int) (getView().getHeight() * 1.5f);
            // 拖拽阴影是一个ColorDrawable. 这个集合的维度和系统所提供的Canvas是一样的
            // 因此，拖拽阴影将会被Canvas覆盖
            shadow.setBounds(0, 0, width, height);
            // 设置参数宽度和高度的大小.通过大小参数返回给系统
            size.set(width, height);
            // 设置触摸点的位置为拖拽阴影的中心
            touch.set(width / 2, height / 2);
        }

        // 在画布Canvas中定义一个回调函数来绘制拖拽的阴影，该画布是通过方法onProvideShadowMetrics()提供的维度
        // 由系统构造
        @Override
        public void onDrawShadow(Canvas canvas) {
            // 在由系统传递的Canvas上绘制ColorDrawable
            shadow.draw(canvas);
        }
    }

    @SuppressLint("NewApi")
    protected class myDragEventListener implements View.OnDragListener {

        // 该方法由系统调用，当有拖拽事件发生时
        @SuppressLint("ShowToast")
        public boolean onDrag(View v, DragEvent event) {
            // 定义一个变量来存储通过事件传递的action类型
            final int action = event.getAction();
            // 每个事件的处理
            switch (action) {

                case DragEvent.ACTION_DRAG_STARTED:
                    // 确定是否这个视图（View）可以接收拖拽的数据类型
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        // 这里是一个例子，通过更改TextView的背景颜色和文字
                        // 来证明它可以接收数据
                        v.setBackgroundColor(Color.BLUE);
                        ((TextView) v).setText("drag");
                        // 强制重绘视图以显示新的特性
                        v.invalidate();
                        // 通过返回true来表明View可以接收拖拽数据
                        return (true);

                    } else {
                        // 返回false. 在当前是拖拽和落下操作时，视图（View）将不再接收
                        // 事件直到发送ACTION_DRAG_ENDED
                        return (false);
                    }

                case DragEvent.ACTION_DRAG_ENTERED:
                    // 拖拽的阴影已经到达指定的区域，TextView背景变为绿色，返回true，该返回值没有意义
                    v.setBackgroundColor(Color.GREEN);
                    ((TextView) v).setText("in");
                    // 强制重绘视图以显示新的特性
                    v.invalidate();
                    return (true);

                case DragEvent.ACTION_DRAG_LOCATION:
                    // 忽略该事件
                    return (true);

                case DragEvent.ACTION_DRAG_EXITED:
                    System.out.println("ACTION_DRAG_EXITED----------------");
                    // 拖拽阴影超出制定区域，重置TextView背景色为蓝色
                    v.setBackgroundColor(Color.BLUE);
                    ((TextView) v).setText("drag");
                    // 强制重绘视图以显示新的特性
                    v.invalidate();
                    return (true);

                case DragEvent.ACTION_DROP:
                    // 获得item包括拖拽数据
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    // 从item获得文本数据
                    CharSequence dragData = item.getText();
                    // 显示拖拽数据中包含的信息.
                    Toast.makeText(DragDropActivity.this, "Dragged data is: " + dragData, Toast.LENGTH_SHORT).show();
                    // 重新设置颜色和文字
                    v.setBackgroundColor(Color.WHITE);
                    ((TextView) v).setText("get");
                    // 强制重绘视图以显示新的特性
                    v.invalidate();

                    // 返回true. DragEvent.getResult()将会返回true.
                    return (true);

                case DragEvent.ACTION_DRAG_ENDED:
                    // 重新设置颜色和文字
                    v.setBackgroundColor(Color.WHITE);
                    ((TextView) v).setText("room");
                    // 强制重绘视图以显示新的特性
                    v.invalidate();
                    //通过getResult()方法的返回值判断发生了什么
                    if (event.getResult()) {
                        Toast.makeText(DragDropActivity.this, "The drop was handled.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(DragDropActivity.this, "The drop didn't work.", Toast.LENGTH_LONG).show();

                    }
                    return (true);

                // 其他未知的action.
                default:
                    Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");

                    break;
            }
            return true;
        }

    }
}
