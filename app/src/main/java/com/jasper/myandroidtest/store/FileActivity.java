package com.jasper.myandroidtest.store;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jasper.myandroidtest.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * 文件读写操作，如果是sdcard，需要添加权限
 */
public class FileActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "FileActivity";
    private static final String FILE_NAME = "test.txt";
    private static final String SDCARD_PATH = "/tmp/test.txt";

    private TextView textView;
    private TextView textViewSdcard;
    private EditText editText;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        context = this;

        findViewById(R.id.btn_clean).setOnClickListener(this);
        findViewById(R.id.btn_read).setOnClickListener(this);
        findViewById(R.id.btn_write).setOnClickListener(this);
        findViewById(R.id.btn_sdcard_read).setOnClickListener(this);
        findViewById(R.id.btn_sdcard_write).setOnClickListener(this);

        textView = (TextView) findViewById(R.id.tv);
        textViewSdcard = (TextView) findViewById(R.id.tv_sdcard);
        editText = (EditText) findViewById(R.id.et);

        if (isSdcard()) {
            textViewSdcard.setText("sdcard文件路径：" + Environment.getExternalStorageDirectory() + SDCARD_PATH);
        }
    }

    private boolean isSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String read() {
        try (FileInputStream fis = openFileInput(FILE_NAME);
             BufferedInputStream bis = new BufferedInputStream(fis);
        ) {
            byte[] buf = new byte[1024];
            StringBuffer sb = new StringBuffer();
            int length = 0;
            while ((length = bis.read(buf)) != -1) {
                sb.append(new String(buf, 0, length));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void write(String str) {
        try (FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
        ) {
            fos.write(str.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String sdcardRead() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(),
                    SDCARD_PATH);
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuffer sb = new StringBuffer();
            String readline = null;
            while ((readline = br.readLine()) != null) {
                sb.append(readline);
            }
            br.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sdcardWrite(String text) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), SDCARD_PATH);
            if (! file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            //第二个参数意义是说是否以append方式添加内容
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, false));
            bw.write(text);
            bw.flush();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clean:
                textView.setText("");
                editText.setText("");
                break;
            case R.id.btn_read:
                textView.setText(read());
                break;
            case R.id.btn_write:
                write(editText.getText().toString());
                break;
            case R.id.btn_sdcard_read:
                if (isSdcard()) {
                    textView.setText(sdcardRead());
                } else {
                    Toast.makeText(context, "不支持sdcard", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_sdcard_write:
                if (isSdcard()) {
                    sdcardWrite(editText.getText().toString());
                } else {
                    Toast.makeText(context, "不支持sdcard", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
