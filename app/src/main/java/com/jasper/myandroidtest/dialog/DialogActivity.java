package com.jasper.myandroidtest.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jasper.myandroidtest.R;

public class DialogActivity extends Activity implements OnLoginListener, View.OnClickListener {
    private Context context;
    private EditText et;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        context = this;
        tv = (TextView) findViewById(R.id.tv_result);

        findButtonAndSetOnClickListenr((ViewGroup) findViewById(R.id.layout_main));
    }

    private void findButtonAndSetOnClickListenr(ViewGroup viewGroup) {
        for (int i=0; i<viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i) instanceof Button) {
                viewGroup.getChildAt(i).setOnClickListener(this);
            } else if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                findButtonAndSetOnClickListenr((ViewGroup) viewGroup.getChildAt(i));
            }
        }
    }

    //View.OnClickListene
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_two:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this)
                        .setTitle("普通弹窗")
                        .setPositiveButton("OK", new DialogButtonClickListener("OK"))
                        .setNegativeButton("cancel", new DialogButtonClickListener("cancel"))
                        .setMessage("message");
                builder1.create().show();
                break;
            case R.id.btn_three:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this)
                        .setTitle("评价")
                        .setPositiveButton("很好", new DialogButtonClickListener("good"))
                        .setNegativeButton("不好", new DialogButtonClickListener("bad"))
                        .setNeutralButton("及格", new DialogButtonClickListener("no bad"))
                        .setMessage("对这个Demo的评价");
                builder2.create().show();
                break;
            case R.id.btn_edit:
                et = new EditText(this);
                AlertDialog.Builder builder3 = new AlertDialog.Builder(this)
                        .setTitle("评价")
                        .setPositiveButton("确定", new DialogButtonClickListener("sure"))
                        .setView(et)
                        .setMessage("对这个Demo的评价");
                builder3.create().show();
                break;
            case R.id.btn_radio:
                AlertDialog.Builder builder4 = new AlertDialog.Builder(this)
                        .setTitle("评价")
                        //这里可以看which查看是点击的哪一项
                        .setSingleChoiceItems(new String[]{"很好", "及格", "不好"}, 0, new DialogButtonClickListener("radio"))
                        .setPositiveButton("确定", new DialogButtonClickListener("sure"));
                builder4.create().show();
                break;
            case R.id.btn_checkbox:
                AlertDialog.Builder builder5 = new AlertDialog.Builder(this)
                        .setTitle("喜欢的语言")
                        .setMultiChoiceItems(new String[]{"Java", "Android", "IOS"}, new boolean[]{true, false, false}, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                Toast.makeText(context, String.format(" which:%s, isChecked:%s", which, isChecked), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("确定", new DialogButtonClickListener("sure"));
                builder5.create().show();
                break;
            case R.id.btn_xml:
                final View view = LayoutInflater.from(this).inflate(R.layout.dialog_login, null);
                final AlertDialog.Builder builder6 = new AlertDialog.Builder(this)
                        .setTitle("登录")
                        .setView(view);
                final AlertDialog dialog6 = builder6.create();
                dialog6.show();
                view.findViewById(R.id.btn_cancle).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog6.dismiss();
                    }
                });
                view.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText etName = (EditText) view.findViewById(R.id.et_username);
                        EditText etPass = (EditText) view.findViewById(R.id.et_password);
                        Toast.makeText(context, String.format("name:%s, password:%s",
                                etName.getText().toString(), etPass.getText().toString()), Toast.LENGTH_SHORT ).show();
                        dialog6.dismiss();
                    }
                });
                break;
            case R.id.btn_df:
                MyDialogFragment myDialogFragment = new MyDialogFragment();
                myDialogFragment.show(getFragmentManager().beginTransaction(),"MyDialogFragment");
        }
    }

    @Override
    public void result(boolean success) {
        tv.setText("登录结果:" + success);
    }

    private class DialogButtonClickListener implements DialogInterface.OnClickListener {
        private String tag;
        public DialogButtonClickListener(String tag) {
            this.tag = tag;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (et == null) {
                Toast.makeText(context, String.format("tag:%s, which:%s", tag, which), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, String.format("tag:%s, which:%s,et:%s", tag, which, et.getText().toString()), Toast.LENGTH_SHORT).show();
            }
        }

    }
}
