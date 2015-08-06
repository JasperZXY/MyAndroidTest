package com.jasper.myandroidtest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Selection;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jasper.myandroidtest.utils.IOUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * 显示笔记内容用
 */
public class NoteActivity extends Activity {
    public static final String TITLE = "title";
    public static final String PATH = "path";

    @SuppressLint("NewApi")
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
        layout.setPadding(10, 10, 10, 10);

        EditText et = new EditText(this);
        et.setGravity(Gravity.LEFT);
        layout.addView(et);
        et.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
        et.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
        et.setBackground(null);

        setTitle(getIntent().getStringExtra(TITLE));
        String path = getIntent().getStringExtra(PATH);

        AssetManager am = getAssets();
        InputStream is = null;
        try {
            is = am.open(path);
            String note = IOUtil.inputStream2String(is);
            if (note == null || note == "") {
                et.setText("error");
            }
            et.setText(note);
        } catch (Exception e) {
            et.setText("error:" + e.getLocalizedMessage());
        } finally {
            IOUtil.close(is);
        }

        et.setTextColor(Color.DKGRAY);
        et.setSelection(0);
        et.setCursorVisible(false);
        et.setFocusable(false);
    }

}
