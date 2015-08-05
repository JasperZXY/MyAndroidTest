package com.jasper.myandroidtest.other;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jasper.myandroidtest.R;

import java.util.Arrays;


public class PermissionActivity extends Activity {
    private TextView tv;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        btn = (Button) findViewById(R.id.btn);
        tv = (TextView) findViewById(R.id.tv);
        PackageManager pm = getPackageManager();
        try {
            PackageInfo pack = pm.getPackageInfo("com.jasper.myandroidtest", PackageManager.GET_PERMISSIONS);
            String[] permissionStrings = pack.requestedPermissions;
            tv.setText("权限清单--->" + Arrays.toString(permissionStrings));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getPackageManager();
                //这样也只判断了是否在AndroidManifest.xml中定义了权限
                tv.append("\n摄像头权限" +
                        packageManager.checkPermission("android.permission.CAMERA", "com.jasper.myandroidtest"));
            }
        });
    }

}
