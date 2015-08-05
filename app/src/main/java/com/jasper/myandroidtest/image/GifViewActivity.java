package com.jasper.myandroidtest.image;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        this.setContentView(layout);

        GifImageView gifImageView = new GifImageView(this);
        layout.addView(gifImageView);
        try {
            GifDrawable gifDrawable = new GifDrawable(getAssets(), "qh.gif");
            gifImageView.setImageDrawable(gifDrawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
