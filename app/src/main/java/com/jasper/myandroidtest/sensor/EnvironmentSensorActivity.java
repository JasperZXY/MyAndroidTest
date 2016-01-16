package com.jasper.myandroidtest.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.jasper.myandroidtest.R;

import java.util.HashMap;
import java.util.Map;

public class EnvironmentSensorActivity extends Activity implements SensorEventListener {
    private TableLayout tableLayout;
    private Map<Integer, Boolean> sensorAble;
    private Map<Integer, TextView> sensorValue;
    private static final int[] SENSOR_TYPE = new int[]{Sensor.TYPE_AMBIENT_TEMPERATURE, Sensor.TYPE_TEMPERATURE,
            Sensor.TYPE_LIGHT, Sensor.TYPE_PRESSURE, Sensor.TYPE_RELATIVE_HUMIDITY};
    private static final String[] UNITS = new String[]{"°C", "°C", "lx", "hPa", "%"};
    private static final String[] TYPES = new String[]{"周围空气温度", "设备温度【弃用】",
            "光照度", "大气压", "相对湿度"};
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_environment_sensor);

        if (SENSOR_TYPE.length != UNITS.length || SENSOR_TYPE.length != TYPES.length) {
            Toast.makeText(this, "数组长度有误，请检查代码！！！", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        sensorAble = new HashMap<>();
        sensorValue = new HashMap<>();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        tableLayout = (TableLayout) findViewById(R.id.layout_table);
        for (int i=0; i<SENSOR_TYPE.length; i++) {
            TableRow tableRow = new TableRow(this);

            TextView tvType = new TextView(this);
            tvType.setGravity(Gravity.CENTER);
            tvType.setText(TYPES[i]);
            tableRow.addView(tvType);

            TextView tvAble = new TextView(this);
            tvAble.setGravity(Gravity.CENTER);
            tableRow.addView(tvAble);

            TextView tvValue = new TextView(this);
            tvValue.setGravity(Gravity.CENTER);
            tableRow.addView(tvValue);
            sensorValue.put(SENSOR_TYPE[i], tvValue);

            if (sensorManager.getDefaultSensor(SENSOR_TYPE[i]) == null) {
                sensorAble.put(SENSOR_TYPE[i], Boolean.FALSE);
                tvAble.setText("N");
                tvValue.setText("-");
            } else {
                sensorAble.put(SENSOR_TYPE[i], Boolean.TRUE);
                tvAble.setText("Y");
                tvValue.setText("-" + UNITS[i]);
            }

            tableLayout.addView(tableRow);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (Map.Entry<Integer, Boolean> entry : sensorAble.entrySet()) {
            if (entry.getValue()) {
                sensorManager.registerListener(this,
                        sensorManager.getDefaultSensor(entry.getKey()),
                        SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (Map.Entry<Integer, Boolean> entry : sensorAble.entrySet()) {
            if (entry.getValue()) {
                sensorManager.unregisterListener(this);
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        for (int i=0; i<SENSOR_TYPE.length; i++) {
            if (SENSOR_TYPE[i] == event.sensor.getType()) {
                TextView textView = sensorValue.get(SENSOR_TYPE[i]);
                if (textView != null) {
                    textView.setText(event.values[0] + UNITS[i]);
                } else {
                    Log.e("EnvSensorAct", "onSensorChanged TextView is null, SENSOR_TYPE index:" + i);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
