package com.jasper.myandroidtest.image;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jasper.myandroidtest.R;

import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * 第三方开源的显示gif图片控件
 * <a href="https://github.com/koral--/android-gif-drawable">项目地址</a>
 */
public class GifViewActivity extends Activity {
    private GifDrawable gifDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setBackgroundResource(R.drawable.bg);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        this.setContentView(layout);
        layout.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
        layout.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;

        FrameLayout frameLayout = new FrameLayout(this);
        layout.addView(frameLayout);
        frameLayout.getLayoutParams().width = 300;
        frameLayout.getLayoutParams().height = 300;

        GifImageView gifImageView = new GifImageView(this);
        frameLayout.addView(gifImageView);
        gifImageView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
        gifImageView.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;

        try {
            gifDrawable = new GifDrawable(getAssets(), "qh.gif");
            gifImageView.setImageDrawable(gifDrawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gifDrawable != null || ! gifDrawable.isRecycled()) {
            gifDrawable.recycle();
            gifDrawable = null;
        }
    }
}
