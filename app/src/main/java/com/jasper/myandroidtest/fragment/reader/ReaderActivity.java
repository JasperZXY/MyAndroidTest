package com.jasper.myandroidtest.fragment.reader;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;

import com.jasper.myandroidtest.R;

/**
 * <pre>
 * 这个例子整体是参照官方文档的。
 * http://developer.android.com/guide/components/fragments.html
 *
 * Assets文件阅读器
 * 宽屏时左边显示标题，右边显示详细内容。
 * 竖屏时界面显示标题，点击具体item做跳转显示。
 *
 * 运用了横竖屏切换的数据保存。
 * 横竖屏加载不同的布局文件，横屏的放在layout-land
 * <pre/>
 */
public class ReaderActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注意这里的layout有两个文件，一个是竖屏的在layout文件夹里，一个是横屏的在layout-land文件夹里
        setContentView(R.layout.activity_reader);
        //根据重力感应进行横竖屏切换设置，而不是默认的，因为有的手机可能禁止了横竖屏切换
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        ReaderManager.getInstance().init(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //只在这里进行数据清理，防止多次init
            ReaderManager.getInstance().release();
        }
        return super.onKeyDown(keyCode, event);
    }
}
