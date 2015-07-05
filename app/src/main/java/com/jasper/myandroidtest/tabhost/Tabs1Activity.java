package com.jasper.myandroidtest.tabhost;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.fragmentPageV4.*;

/**
 * 用FragmentTabHost 实现底部导航栏
 * FragmentTabHost 是不保存状态的，填入的信息，在tab切换后会消失
 */
public class Tabs1Activity extends FragmentActivity {
    /**
     * 定义FragmentTabHost对象
     */
    private FragmentTabHost fragmentTabHost;
    /**
     * 定义一个线性布局
     */
    private LayoutInflater layoutInflater;
    /**
     * 定义数组来存放Fragment界面
     */
    private Class[] fragmentArray = {FragmentPage01.class,FragmentPage02.class,
            FragmentPage03.class,FragmentPage04.class,FragmentPage05.class};
    /**
     * 定义数组来存放按钮图片
     */
    private int[] mImageViewArray = {R.drawable.tab_home_btn,R.drawable.tab_message_btn,R.drawable.tab_selfinfo_btn,
            R.drawable.tab_square_btn,R.drawable.tab_more_btn};
    /**
     * Tab选项卡的文字
     */
    private String[] mTextviewArray = {"首页", "消息", "好友", "广场", "更多"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);
        init();
    }


    /**
     * 初始化组件
     */
    private void init() {
        //实例化布局
        layoutInflater = LayoutInflater.from(this);
        //实例化TabHost对象，得到TabHost
        fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
//		fragmentTabHost.setup();
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        //得到fragment的个数
        int count = fragmentArray.length;
        for (int i = 0; i < count; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            fragmentTabHost.addTab(tabSpec,fragmentArray[i], null);
            //设置Tab按钮的背景，由这个selector_tab_background文件来判断当前状态，若点击则显示背景图片
            fragmentTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }
    }


    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int i) {
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);
        //设置图像
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[i]);
        //设置文字
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[i]);
        return view;
    }
}
