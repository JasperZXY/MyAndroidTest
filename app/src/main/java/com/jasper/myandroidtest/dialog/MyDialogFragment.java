package com.jasper.myandroidtest.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.jasper.myandroidtest.R;

/**
 * 至少需要实现onCreateView或者onCreateDIalog方法
 *
 * 解决DialogFragment宽度无法自定义的问题，需要在onResume中进行设置才可以，但还是有边框距离
 */
public class MyDialogFragment extends DialogFragment implements View.OnClickListener {
    private static final String TAG = MyDialogFragment.class.getSimpleName();
    private OnLoginListener listener;
    private EditText etName;
    private EditText etPass;

    @Override
    public void onAttach(Activity activity) {
        try {
            listener = (OnLoginListener)activity;
        } catch (Exception e) {
            Log.e(TAG, activity + " no implements OnLoginListener");
        }
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_login, container, false);
        init(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window dialogWindow = getDialog().getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.horizontalMargin = 0;
        lp.alpha = 0.7f;
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_login, null);
//        init(view);
//        builder.setView(view)
//                .setPositiveButton("确定", null)
//                .setNegativeButton("取消", null);
//        return builder.create();
//    }

    private void init(View view) {
        etName = (EditText) view.findViewById(R.id.et_username);
        etPass = (EditText) view.findViewById(R.id.et_password);

        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        view.findViewById(R.id.btn_login).setOnClickListener(this);
        view.findViewById(R.id.btn_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                break;
            case R.id.btn_login:
                Toast.makeText(getActivity(), String.format("name:%s password:%s",
                        etName.getText().toString(), etPass.getText().toString()), Toast.LENGTH_SHORT).show();
                if (etName.getText().toString().equals("1")) {
                    listener.result(true);
                } else {
                    listener.result(false);
                }
                break;
        }
        dismiss();
    }
}
