package com.jasper.myandroidtest.library;

import android.app.Activity;
import android.widget.TextView;
import android.widget.Toast;

import com.jasper.myandroidtest.R;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

/**
 * <strong>androidannotations使用</strong><br/>
 * 项目地址：https://github.com/excilys/androidannotations<br/>
 * 项目介绍：https://github.com/excilys/androidannotations/wiki/Home<br/>
 * 相应gradle的配置：https://github.com/excilys/androidannotations/wiki/Building-Project-Gradle<br/>
 *
 * 注：使用时用的是AndroidAnnotationsActivity_，有“_”
 */
@EActivity(R.layout.activity_android_annotations)
public class AndroidAnnotationsActivity extends Activity {
    @ViewById(R.id.tv_num)   //若变量名与资源名一致，则这里不需要写上资源名
    TextView tvNum;

    @Click(R.id.btn_add)
    void add() {
        tvNum.setText(Integer.parseInt(tvNum.getText().toString()) + 1 + "");
    }

    @Click(R.id.btn_subtract)
    void subtract() {
        tvNum.setText(Integer.parseInt(tvNum.getText().toString()) - 1 + "");
    }

    @Click(R.id.btn_background)
    void background() {
        sleepAndShowToast();
    }

    @Background
    void sleepAndShowToast() {
        try {
            Thread.sleep(3000);   //这里只是为了掩饰后台运行而已，点击后并不会阻塞UI，还可以点击其他按钮
        } catch (InterruptedException e) {
        }
        showToash();
    }

    @UiThread
    void showToash() {
        Toast.makeText(this, "普通Toast", Toast.LENGTH_SHORT).show();
    }

}
