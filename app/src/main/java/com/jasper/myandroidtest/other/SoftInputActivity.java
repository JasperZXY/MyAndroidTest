package com.jasper.myandroidtest.other;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.jasper.myandroidtest.R;

/**
 * 软键盘相关配置
 * 1.EditText不自动获取焦点，在EditText的父级控件中找一个，设置成
 *      android:focusable="true"
 *      android:focusableInTouchMode="true"
 *
 * TODO 需要继续完善
 */
public class SoftInputActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "SoftInputActivity";
    private EditText etTop;
    private EditText etBottom;
    private ToggleButton tgInputStatus;
    private InputMethodManager imm;
    private Button btnSure;
    private int mode = 0;
    private CheckBox cbAU;
    private CheckBox cbAR;
    private CheckBox cbAP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_input);
        Log.i(TAG, "version 1");

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        etTop = (EditText) findViewById(R.id.et_top);
        etBottom = (EditText) findViewById(R.id.et_bottom);

        tgInputStatus = (ToggleButton) findViewById(R.id.tb_show_hide);
        tgInputStatus.setChecked(false);
        tgInputStatus.setOnClickListener(this);
        btnSure = (Button) findViewById(R.id.btn_sure);
        btnSure.setOnClickListener(this);
        cbAU = (CheckBox) findViewById(R.id.cb_au);
        cbAR = (CheckBox) findViewById(R.id.cb_ar);
        cbAP = (CheckBox) findViewById(R.id.cb_ap);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tb_show_hide:
                if (tgInputStatus.isChecked()) {
//                  imm.showSoftInput(etTop, 0);   //这样显示不出键盘，用下面的可以
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    imm.hideSoftInputFromWindow(etTop.getWindowToken(), 0);
                }
                break;

            case R.id.btn_sure:
                mode = 0;
                if (cbAU.isChecked()) {
                    mode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED;
                }
                if (cbAR.isChecked()) {
                    mode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
                }
                if (cbAP.isChecked()) {
                    mode |= WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
                }
                getWindow().setSoftInputMode(mode);
                break;
        }
    }
}
