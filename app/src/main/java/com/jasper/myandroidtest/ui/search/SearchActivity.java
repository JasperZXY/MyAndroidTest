package com.jasper.myandroidtest.ui.search;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.utils.ViewUtil;

public class SearchActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ViewUtil.findButtonAndSetOnClickListenr(this, this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                onSearchRequested();
        }
    }

}
