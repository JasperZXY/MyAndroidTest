package com.jasper.myandroidtest._thirdparty.jpush;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.utils.Constants;

/**
 * 如果要支持富媒体，AndroidManifest.xml要配置cn.jpush.android.ui.PopWinActivity，
 * 还需要导入res文件夹下“jpush_”开头的文件名，
 * 这些资源文件基本都让我给改了名字，原先很多都不是“jpush_”开头的。
 */
public class JPushDetailActivity extends Activity {
    private TextView tvResult;
    private TextView tvRegId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jpush_detail);

        tvResult = (TextView) findViewById(R.id.tv_result);
        tvRegId = (TextView) findViewById(R.id.tv_reg_Id);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        tvRegId.setText(preferences.getString(Constants.JPUSH_REG_ID, "无登录信息"));

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            tvResult.setText("数据为空");
        } else {
            JPushDetail jPushDetail = bundle.getParcelable(Constants.JPUSH_DETAIL_KEY);
            if (jPushDetail != null) {
                tvResult.setText(jPushDetail.toString());
            } else {
                tvResult.setText("无");
            }
        }

    }
}
