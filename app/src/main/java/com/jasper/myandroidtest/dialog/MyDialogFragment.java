package com.jasper.myandroidtest.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.jasper.myandroidtest.R;

/**
 * 至少需要实现onCreateView或者onCreateDIalog方法
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

        view.findViewById(R.id.btn_cancle).setOnClickListener(this);
        view.findViewById(R.id.btn_login).setOnClickListener(this);
        view.findViewById(R.id.btn_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancle:
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
