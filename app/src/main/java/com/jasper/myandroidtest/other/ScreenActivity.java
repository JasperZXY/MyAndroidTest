package com.jasper.myandroidtest.other;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ToggleButton;

import com.jasper.myandroidtest.R;

/**
 * 状态栏一体化，网上称这种为沉浸式状态栏，API 19起支持
 *
 * 注意最外层的padding无效了，还有设置FEATURE_NO_TITLE后魅族返回按钮没了
 */
public class ScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_screen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            ViewGroup viewGroup = (ViewGroup) ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
            viewGroup.setFitsSystemWindows(true);
//            viewGroup.setClipToPadding(true);

        }

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.layout);
        for (int i=0; i<5; i++) {
            Button button = new Button(this);
            button.setText("" + (i + 1));
            button.setTextColor(Color.DKGRAY);
            button.setBackgroundResource(R.drawable.my_btn);
            viewGroup.addView(button, 0);
        }

    }

}
