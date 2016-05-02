package com.jasper.myandroidtest._library.eventbus;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import com.jasper.myandroidtest.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <strong>EventBus</strong><br/>
 * 项目地址：https://github.com/greenrobot/EventBus<br/>
 * 文档：http://greenrobot.org/eventbus/documentation/<br/>
 */
public class EventBusActivity extends Activity {
    @Bind(R.id.et_log)
    EditText etLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);  //关键
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);  //关键
    }

    @OnClick(R.id.btn1)
    void click1() {
        EventBus.getDefault().post(new FirstEvent());
    }

    @OnClick(R.id.btn2)
    void click2() {
        EventBus.getDefault().post(new SecondEvent());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FirstEvent firstEvent) {
        final String threaName = Thread.currentThread().getName();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                etLog.append("\nFirstEvent thread:" + threaName);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(SecondEvent secondEvent) {
        final String threaName = Thread.currentThread().getName();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                etLog.append("\nsecondEvent thread:" + threaName);
            }
        });
    }
}
