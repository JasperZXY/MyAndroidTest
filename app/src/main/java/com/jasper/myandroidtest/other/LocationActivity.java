package com.jasper.myandroidtest.other;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.utils.UIUtil;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * 定位服务
 * 需添加权限  <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
 */
public class LocationActivity extends Activity implements View.OnClickListener {
    private EditText etLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        etLog = (EditText) findViewById(R.id.et_log);
        UIUtil.findButtonAndSetOnClickListenr(this, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_location:
                location();
                break;

            case R.id.btn_location_last_known:
                getLastKnownLocation();
                break;

            case R.id.btn_location_name:
                new LocationAsyncTask(etLog).execute();

        }
    }

    private void location() {
        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                location.getTime();
                etLog.append(String.format("\n经度[%s] 纬度[%s]", location.getLatitude(), location.getLongitude()));
                locationManager.removeUpdates(this);   //停止监听，不然会一直重复获取地理位置
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                etLog.append(String.format("\nonStatusChanged provider[%s] status[%s]", provider, status));
            }

            public void onProviderEnabled(String provider) {
                etLog.append(String.format("\nonProviderEnabled provider[%s]", provider));
            }

            public void onProviderDisabled(String provider) {
                etLog.append(String.format("\nonProviderDisabled provider[%s]", provider));
            }
        };

        //可以同时调用两次
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    private void getLastKnownLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null) {
            etLog.append("getLastKnownLocation is null");
        } else {
            etLog.append(String.format("\nLast Known Location 经度[%s] 纬度[%s]", location.getLatitude(), location.getLongitude()));
        }
    }

    private static class LocationAsyncTask extends AsyncTask<Void, Void, String> {
        private EditText etLog;

        public LocationAsyncTask(EditText etLog) {
            this.etLog = etLog;
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpGet httpGet = new HttpGet("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json");
            String ret = null;
            try {
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                    ret = String.format("\n位置:%s,%s", jsonObject.get("province"), jsonObject.get("city"));
                }
            } catch (Exception e) {
                Log.e("LocationActivity", "getLocationName error:", e);
                ret = "\ngetLocationName error:" + e.getLocalizedMessage();
            }
            return ret;
        }

        @Override
        protected void onPostExecute(String result) {
            etLog.append(result);
        }
    }

}


