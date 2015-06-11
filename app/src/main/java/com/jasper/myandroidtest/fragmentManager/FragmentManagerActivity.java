package com.jasper.myandroidtest.fragmentManager;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.jasper.myandroidtest.R;

/**
 * 测试结果是：主页面显示F1，F1跳到F2，F1做了回退栈操作，F2再跳到F3，F2不做回退栈操作，那么在按返回键的时候，F3会跟F1重叠
 */
public class FragmentManagerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_manager);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.replace(R.id.container, new Fragment1(), "ONE");
        tx.commit();
    }
}
