package com.jasper.myandroidtest.event.sensor;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jasper.myandroidtest.R;

public class SimpleSensorActivity extends Activity
        implements SensorEventListener, View.OnClickListener {
    private SensorManager sensorMgr;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_sensor);

        textView = (TextView) findViewById(R.id.tv);
        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);

        findViewById(R.id.btn_gradienter).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 为系统的方向传感器注册监听器
        sensorMgr.registerListener(this,
                sensorMgr.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // 取消注册
        sensorMgr.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        textView.setText(String.format("x:%s, y:%s, z:%s", event.values[0], event.values[1], event.values[2]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_gradienter:
                startActivity(new Intent(getApplicationContext(), GradienterActivity.class));
        }
    }
}
