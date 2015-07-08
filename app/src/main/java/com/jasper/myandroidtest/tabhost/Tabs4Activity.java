package com.jasper.myandroidtest.tabhost;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.jasper.myandroidtest.R;

public class Tabs4Activity extends TabActivity implements RadioGroup.OnCheckedChangeListener {
    private final static int TAB_COUNT = 3;

    private final static String[] TAGS = new String[]{"tab1", "tab2", "tab3"};
    private Intent[] intents = null;

    private TabHost mTabHost = null;
    private RadioGroup mainTabRadioGroup = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs4);
        mTabHost = this.getTabHost();
        mainTabRadioGroup = (RadioGroup)findViewById(R.id.gr_main_tab);
        mainTabRadioGroup.setOnCheckedChangeListener(this);

        intents = new Intent[TAB_COUNT];
        intents[0] = new Intent(this, TabTest1Activity.class);
        intents[1] = new Intent(this, TabTest2Activity.class);
        intents[2] = new Intent(this, TabTest3Activity.class);

        for (int i=0; i<TAB_COUNT; i++) {
            this.mTabHost.addTab(buildTabSqec(TAGS[i], intents[i]));
        }

        mainTabRadioGroup.getChildAt(0).performClick();
    }

    //添加Tab按钮
    private TabHost.TabSpec buildTabSqec(String tag, Intent intent) {
        return mTabHost.newTabSpec(tag).setIndicator(null, null).setContent(intent);
    }
    //为TabHost添加Tab
    private void addTabSqec() {
    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId) {
            case R.id.tab1:
                mTabHost.setCurrentTabByTag(TAGS[0]);
                break;
            case R.id.tab2:
                mTabHost.setCurrentTabByTag(TAGS[1]);
                break;
            case R.id.tab3:
                mTabHost.setCurrentTabByTag(TAGS[2]);
                break;
        }
    }

}
