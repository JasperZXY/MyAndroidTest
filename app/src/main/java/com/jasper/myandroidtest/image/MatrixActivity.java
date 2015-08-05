package com.jasper.myandroidtest.image;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jasper.myandroidtest.R;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 注意xml文件中SeekBar配置的progress跟max属性
 * Bitmap.createBitmap有时会报OutOfMemoryError，已经做了两层保护措施，
 * 1. 拖动时超过一定的秒数才进行图像处理
 * 2. 定时器处理图片，每隔一段时间进行图片强制回收
 */
public class MatrixActivity extends Activity implements SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "MatrixActivity";
    private SeekBar seekBarRotateX;
    private SeekBar seekBarRotateY;
    private SeekBar seekBarRotateZ;
    private SeekBar seekBarSkewX;
    private SeekBar seekBarSkewY;
    private SeekBar seekBarTranslateZ;
    private TextView tvRotateX;
    private TextView tvRotateY;
    private TextView tvRotateZ;
    private TextView tvSkewX;
    private TextView tvSkewY;
    private TextView tvTranslateZ;
    private ImageView imageView;

    private int rotateX, rotateY, rotateZ;
    private float skewX, skewY;
    private int translateZ;

    //注意这个Camera的包android.graphics下的类
    private Camera camera;
    //要处理图片的原图
    private Bitmap bitmapSource;
    private long lastTime = 0;
    private CopyOnWriteArrayList<Bitmap> bitmaps = new CopyOnWriteArrayList<>();
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix);

        imageView = (ImageView) findViewById(R.id.img);

        tvRotateX = (TextView) findViewById(R.id.tv_rotate_x);
        tvRotateY = (TextView) findViewById(R.id.tv_rotate_y);
        tvRotateZ = (TextView) findViewById(R.id.tv_rotate_z);
        tvSkewX = (TextView) findViewById(R.id.tv_skew_x);
        tvSkewY = (TextView) findViewById(R.id.tv_skew_y);
        tvTranslateZ = (TextView) findViewById(R.id.tv_translate_z);

        seekBarRotateX = (SeekBar) findViewById(R.id.sb_rotate_x);
        seekBarRotateY = (SeekBar) findViewById(R.id.sb_rotate_y);
        seekBarRotateZ = (SeekBar) findViewById(R.id.sb_rotate_z);
        seekBarSkewX = (SeekBar) findViewById(R.id.sb_skew_x);
        seekBarSkewY = (SeekBar) findViewById(R.id.sb_skew_y);
        seekBarTranslateZ = (SeekBar) findViewById(R.id.sb_translate_z);

        seekBarRotateX.setOnSeekBarChangeListener(this);
        seekBarRotateY.setOnSeekBarChangeListener(this);
        seekBarRotateZ.setOnSeekBarChangeListener(this);
        seekBarSkewX.setOnSeekBarChangeListener(this);
        seekBarSkewY.setOnSeekBarChangeListener(this);
        seekBarTranslateZ.setOnSeekBarChangeListener(this);

        camera = new Camera();
        bitmapSource = ((BitmapDrawable) getResources().getDrawable(R.drawable.icon)).getBitmap();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (int i=0; i<bitmaps.size() - 1; i++) {
                    Bitmap bitmap = bitmaps.get(i);
                    bitmap.recycle();
                    bitmaps.remove(i);
                    Log.i(TAG,"bitmap recycle");
                }
                System.gc();
            }
        }, 100, 200);
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    private void refreshImage() {
        // 开始处理图像
        // 1.获取处理矩阵
        // 记录一下初始状态。save()和restore()可以将图像过渡得柔和一些。
        // Each save should be balanced with a call to restore().
        camera.save();
        Matrix matrix = new Matrix();
        // rotate
        camera.rotateX(rotateX);
        camera.rotateY(rotateY);
        camera.rotateZ(rotateZ);
        // translate
        camera.translate(0, 0, translateZ);
        camera.getMatrix(matrix);
        // 恢复到之前的初始状态。
        camera.restore();
        // 设置图像处理的中心点
        matrix.preTranslate(bitmapSource.getWidth() >> 1, bitmapSource.getHeight() >> 1);
        matrix.preSkew(skewX, skewY);
        // matrix.postSkew(skewX, skewY);
        // 直接setSkew()，则前面处理的rotate()、translate()等等都将无效。
        // matrix.setSkew(skewX, skewY);
        // 2.通过矩阵生成新图像(或直接作用于Canvas)
        try {
            // 经过矩阵转换后的图像宽高有可能不大于0，此时会抛出IllegalArgumentException
            Bitmap bitmapResult = Bitmap.createBitmap(bitmapSource, 0, 0, bitmapSource.getWidth(), bitmapSource.getHeight(), matrix, true);
            imageView.setImageBitmap(bitmapResult);
            bitmaps.add(bitmapResult);
        } catch (IllegalArgumentException e) {
            Log.w(TAG, "Bitmap.createBitmap error:" + e.getLocalizedMessage());
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.sb_rotate_x:
                rotateX = progress;
                tvRotateX.setText(progress + "°");
                break;
            case R.id.sb_rotate_y:
                rotateY = progress;
                tvRotateY.setText(progress + "°");
                break;
            case R.id.sb_rotate_z:
                rotateZ = progress;
                tvRotateZ.setText(progress + "°");
                break;
            case R.id.sb_skew_x:
                skewX = (progress - 100) * 1.0f / 100;
                tvSkewX.setText(Float.toString(skewX));
                break;
            case R.id.sb_skew_y:
                skewY = (progress - 100) * 1.0f / 100;
                tvSkewY.setText(Float.toString(skewY));
                break;
            case R.id.sb_translate_z:
                /*
                * 这里的处理比较特殊，camera.translate方法的第三个参数传递的是Z轴，
                * 负数时图片反而变动，正数是反而变小，故这么处理
                 */
                translateZ = 100 - progress;
                tvTranslateZ.setText(Integer.toString(- translateZ));
                break;
        }
        //防止调用太多次，有时会报OutOfMemoryError
        long curTime = System.currentTimeMillis();
        if (curTime - lastTime > 300) {
                lastTime = curTime;
                refreshImage();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        refreshImage();
    }
}
