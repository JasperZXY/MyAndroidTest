package com.jasper.myandroidtest;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 搜索展示的类，需要在AndroidManifest.xml对这个Activity进行相应的配置
 */
public class SearchResultActivity extends Activity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        textView = (TextView) findViewById(R.id.tv);
        doSearchQuery(getIntent());
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        this.finish();
        return super.getParentActivityIntent();
    }

//    @Override
//    protected void onNewIntent(Intent intent) {  //activity重新置顶
//        super.onNewIntent(intent);
//        doSearchQuery(intent);
//    }

    // 对searchable activity的调用仍是标准的intent，我们可以从intent中获取信息，即要搜索的内容
    private void doSearchQuery(Intent intent){
        if(intent == null)
            return;

        if( Intent.ACTION_SEARCH.equals( intent.getAction())){  //如果是通过ACTION_SEARCH来调用，即如果通过搜索调用
            String queryString = intent.getStringExtra(SearchManager.QUERY); //获取搜索内容
            textView.setText("要搜索的内容：" + queryString);
        }

    }
}
