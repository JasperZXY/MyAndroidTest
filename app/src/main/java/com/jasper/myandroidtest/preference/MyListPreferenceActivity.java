package com.jasper.myandroidtest.preference;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

import com.jasper.myandroidtest.R;

/**
 * ListPreferenceActivity的改版，可以通过代码配置要显示的数据
 */
public class MyListPreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //居然还是要写一个xml文件。书上的例子可以通过PreferenceManager.createPreferenceScreen获取PreferenceScreen，这里却实现不了
        addPreferencesFromResource(R.xml.empty_preference);

        ListPreference listPreference = (ListPreference) findPreference(getResources().getString(R.string.selected_option));
        listPreference.setEntries(new String[]{"选项一", "选项二", "选项三"});
        listPreference.setEntryValues(new String[]{"0", "1", "2"});
        listPreference.setTitle("title");
        listPreference.setSummary("summary");
        //首次运行居然默认选择选项二
        listPreference.setDefaultValue("1");
    }
}
