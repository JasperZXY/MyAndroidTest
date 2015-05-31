package com.jasper.myandroidtest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        findViewById(R.id.btn4fragmentTabHost).setOnClickListener(this);
        findViewById(R.id.btn4fragment_radiogroup).setOnClickListener(this);
        findViewById(R.id.btn4AsyncTask).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn4fragmentTabHost:
                context.startActivity(new Intent(context, TabsActivity.class));
                break;
            case R.id.btn4fragment_radiogroup:
                context.startActivity(new Intent(context, Tabs2Activity.class));
                break;
            case R.id.btn4AsyncTask:
                context.startActivity(new Intent(context, AsyncTaskActivity.class));
                break;
        }
    }
}
