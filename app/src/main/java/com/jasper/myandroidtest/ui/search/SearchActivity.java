package com.jasper.myandroidtest.ui.search;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.utils.UIUtil;

public class SearchActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        UIUtil.findButtonAndSetOnClickListenr(this, this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                onSearchRequested();
        }
    }

}
