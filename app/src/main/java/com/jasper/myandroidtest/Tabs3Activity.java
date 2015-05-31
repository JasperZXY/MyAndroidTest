package com.jasper.myandroidtest;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;


public class Tabs3Activity extends FragmentActivity {
    private Context context;

    private static final String TAG = "Tabs3Activity";

    private ViewPager mTabPager;
    private RadioGroup rgs;
    private int lastIndex = 0;// 上一次tab index
    private int screenWidth;

    private ArrayList<View> views = new ArrayList<>();
    private List<Fragment> fragments = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs3);

        Display currDisplay = getWindowManager().getDefaultDisplay();
        screenWidth = currDisplay.getWidth();
        context = this;
        initRadioGroup();
        initViewPager();
    }

    private void initViewPager() {
        mTabPager = (ViewPager)findViewById(R.id.main_tab_pager);
        fragments = new ArrayList<>();
        fragments.add(new FragmentPage01());
        fragments.add(new FragmentPage02());
        fragments.add(new FragmentPage03());
        fragments.add(new FragmentPage04());
        fragments.add(new FragmentPage05());

        //给ViewPager设置适配器，用FragmentPagerAdapter就只需实现getCount跟getItem两个方法
        mTabPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int index) {
                return fragments.get(index);
            }
        });
        mTabPager.setCurrentItem(0);//设置当前显示标签页为第一页
        mTabPager.setOnPageChangeListener(new TabPagerChangerListener());//页面变化时的监听器
    }

    private void initRadioGroup() {
        rgs = (RadioGroup) findViewById(R.id.tabs_rg);
        rgs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i=0; i<group.getChildCount(); i++) {
                    if (group.getChildAt(i).getId() == checkedId) {
                        mTabPager.setCurrentItem(i);
                    }
                }
            }
        });
    }


    public class TabPagerChangerListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (position >= fragments.size() || position < 0) {
                return;
            }

            Log.d(TAG, String.format("onPageSelected lastIndex:%s, curIndex:%s", lastIndex, position));
//            int one = screenWidth / 4;
//            Animation animation = new TranslateAnimation(one * lastIndex, one * position, 0, 0);
//            animation.setFillAfter(true);
//            animation.setDuration(3000);
//            rgs.setAnimation(animation);
            lastIndex = position;
            ((RadioButton)rgs.getChildAt(position)).setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
