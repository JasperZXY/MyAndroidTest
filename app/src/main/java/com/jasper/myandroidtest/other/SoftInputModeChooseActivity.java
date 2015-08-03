package com.jasper.myandroidtest.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.jasper.myandroidtest.R;

import java.util.LinkedList;
import java.util.List;

/**
 * 这里Activity做windowSoftInputMode的选择，SoftInputActivity做效果展示
 *
 * 软键盘相关配置
 * 1.EditText不自动获取焦点，在EditText的父级控件中找一个，设置成
 *      android:focusable="true"
 *      android:focusableInTouchMode="true"
 *   这种效果其实可以通过配置windowSoftInputMode来实现的
 * 2.hide跟show有没有always，测试后效果一样
 * 3.各个属性的效果说明见xml或运行后看界面文字说明
 * 4.windowSoftInputMode属性可以java代码配置或AndroidManifest.xml文件中配置，值见
 * {@link android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED}
 */
public class SoftInputModeChooseActivity extends Activity implements View.OnClickListener {
    private Button btnSure;
    private ToggleButton tgInputStatus;
    private EditText et;
    private InputMethodManager imm;
    private List<CheckBox> checkBoxList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_input_mode_choose);

        btnSure = (Button) findViewById(R.id.btn_sure);
        btnSure.setOnClickListener(this);
        et = (EditText) findViewById(R.id.et);
        tgInputStatus = (ToggleButton) findViewById(R.id.tb_show_hide);
        tgInputStatus.setChecked(false);
        tgInputStatus.setOnClickListener(this);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.layout_chose);
        for (int i=0; i<viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof CheckBox) {
                checkBoxList.add((CheckBox) view);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb_show_hide:
                if (tgInputStatus.isChecked()) {
//                  imm.showSoftInput(et, 0);   //这样显示不出键盘，用下面的可以
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                }
                break;
            case R.id.btn_sure:
                int mode = 0;
                for (CheckBox checkBox : checkBoxList) {
                    if (checkBox.isChecked()) {
                        mode |= Integer.parseInt((String) checkBox.getTag(), 16);
                    }
                }
                Intent intent = new Intent(getApplicationContext(), SoftInputActivity.class);
                intent.putExtra(SoftInputActivity.WINDOW_SOFT_INPUT_MODE, mode);
                startActivity(intent);
                break;
        }
    }

}
