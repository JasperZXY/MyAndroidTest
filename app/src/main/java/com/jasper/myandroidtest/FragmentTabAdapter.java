package com.jasper.myandroidtest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;

import java.util.List;

/**
 */
public class FragmentTabAdapter implements RadioGroup.OnCheckedChangeListener {
    private List<Fragment> fragments; // 一个tab页面对应一个Fragment
    private RadioGroup rgs; // 用于切换tab
    private FragmentActivity fragmentActivity; // Fragment所属的Activity
    private int fragmentContentId; // Activity中所要被替换的区域的id

    private int currentTab = -1; // 当前Tab页面索引

    private OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener; // 用于让调用者在切换tab时候增加新的功能

    public FragmentTabAdapter(FragmentActivity fragmentActivity, List<Fragment> fragments, int fragmentContentId, RadioGroup rgs) {
        this.fragments = fragments;
        this.rgs = rgs;
        this.fragmentActivity = fragmentActivity;
        this.fragmentContentId = fragmentContentId;

        rgs.setOnCheckedChangeListener(this);
        // 默认显示第一页
//        rgs.check(R.id.tab_rb_a);
        currentTab = 0;
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        ft.add(fragmentContentId, fragments.get(currentTab));
        ft.commit();
        //因为默认就是第一个被选中，所以下面的代码没法让其调用onCheckedChanged
//        ((RadioButton)rgs.getChildAt(0)).setChecked(true);
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        for (int index = 0; index < rgs.getChildCount(); index++) {
            if (rgs.getChildAt(index).getId() == checkedId) {
                if (currentTab != index) {
                    Fragment fragment = fragments.get(index);
                    FragmentTransaction ft = obtainFragmentTransaction(index);
                    if (!fragment.isAdded()) {
                        ft.add(fragmentContentId, fragment);
                    }
                    if (currentTab >= 0 && currentTab < fragments.size()) {
                        ft.hide(fragments.get(currentTab));
                    }
                    ft.show(fragments.get(index));
                    currentTab = index;
                    ft.commit();

                    // 如果设置了切换tab额外功能功能接口
                    if (null != onRgsExtraCheckedChangedListener) {
                        onRgsExtraCheckedChangedListener.OnRgsExtraCheckedChanged(rgs, index);
                    }
                }
            }
        }
    }

    /**
     * 获取一个带动画的FragmentTransaction
     *
     * @param index
     * @return
     */
    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        // 设置切换动画
        if (index > currentTab) {
            ft.setCustomAnimations(R.anim.right_in, R.anim.left_out);
        } else {
            ft.setCustomAnimations(R.anim.left_in, R.anim.right_out);
        }
        return ft;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public Fragment getCurrentFragment() {
        return fragments.get(currentTab);
    }

    public OnRgsExtraCheckedChangedListener getOnRgsExtraCheckedChangedListener() {
        return onRgsExtraCheckedChangedListener;
    }

    public void setOnRgsExtraCheckedChangedListener(OnRgsExtraCheckedChangedListener onRgsExtraCheckedChangedListener) {
        this.onRgsExtraCheckedChangedListener = onRgsExtraCheckedChangedListener;
    }

    /**
     * 切换tab额外功能功能接口
     */
    static class OnRgsExtraCheckedChangedListener {
        public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int index) {

        }
    }

}