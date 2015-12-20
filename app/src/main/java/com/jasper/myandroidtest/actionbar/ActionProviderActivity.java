package com.jasper.myandroidtest.actionbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

import com.jasper.myandroidtest.R;

public class ActionProviderActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_provider);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_provider, menu);

        // 使用ShareActionProvider来调用系统分享
        MenuItem shareItem = menu.findItem(R.id.share_system);
        ShareActionProvider mShareActionProvider = (ShareActionProvider) shareItem.getActionProvider();
        mShareActionProvider.setShareIntent(getDefaultIntent());

        // 自定义分享
        ((MyActionProvider)menu.findItem(R.id.share_custom).getActionProvider()).setText("Hello");

        return super.onCreateOptionsMenu(menu);
    }



    //纯文字分享
    private Intent getDefaultIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
        intent.putExtra(Intent.EXTRA_TEXT, "Hello World");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

}
