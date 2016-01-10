package com.jasper.myandroidtest.other;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.resource.StyleAttributesActivity;
import com.jasper.myandroidtest.utils.FileUtil;

import java.io.File;

public class SettingsActivity extends Activity implements View.OnClickListener {
    public static final String THEME = "theme";
    public static final String BBWW = "bbww";   //黑底白字
    public static final String WBBW = "wbbw";   //白底黑字
    private TextView tvCacheCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findTextViewAndSetOnClickListenr((ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        tvCacheCount = (TextView) findViewById(R.id.tv_clean_cache);
        tvCacheCount.append("   缓存:" + Formatter.formatFileSize(this,
                FileUtil.getDirSize(getCacheDir()) + FileUtil.getDirSize(getExternalCacheDir())));
    }

    private void findTextViewAndSetOnClickListenr(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i) instanceof TextView) {
                viewGroup.getChildAt(i).setOnClickListener(this);
            } else if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                findTextViewAndSetOnClickListenr((ViewGroup) viewGroup.getChildAt(i));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_check:
                startActivity(new Intent(SettingsActivity.this, StyleAttributesActivity.class));
                finish();
                break;
            case R.id.tv_wbbw:
                putTheme(WBBW);
                break;
            case R.id.tv_bbww:
                putTheme(BBWW);
                break;
            case R.id.tv_about:
                Toast.makeText(SettingsActivity.this, "欢迎使用，永久免费", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_clean_cache:
                FileUtil.deleteFile(getCacheDir());
                FileUtil.deleteFile(getExternalCacheDir());
                tvCacheCount.setText("清除缓存");
                break;
        }
    }

    private void putTheme(String theme) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(THEME, theme);
        editor.commit();
    }

    //与ActionBar的返回键结合使用
    @Override
    public Intent getParentActivityIntent() {
        this.finish();
        return super.getParentActivityIntent();
    }
}
