package com.jasper.myandroidtest.camera;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.jasper.myandroidtest.R;

/**
 * 摄像头操作
 * 1. 通过Intent调用系统功能进行拍照，不需要权限
 * 2. 自定义拍摄UI，需要添加权限;
 * <p/>
 * 注：经常报Bitmap too large to be uploaded into a texture的意思是图片超过了硬件加速所规定的高度
 * 在AndroidManifest.xml中对应的Activity添加android:hardwareAccelerated="false"
 */
public class CameraActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "CameraActivity";
    public static final int INTENT_TACK_PHOTO = 100;
    public static final int INTENT_TACK_PHOTO_MY_UI = 101;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imageView = (ImageView) findViewById(R.id.img);
        findButtonAndSetOnClickListenr((ViewGroup) findViewById(R.id.layout_main));
    }

    private void findButtonAndSetOnClickListenr(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i) instanceof Button) {
                viewGroup.getChildAt(i).setOnClickListener(this);
            } else if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                findButtonAndSetOnClickListenr((ViewGroup) viewGroup.getChildAt(i));
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INTENT_TACK_PHOTO || requestCode == INTENT_TACK_PHOTO_MY_UI) {
            try {
                if (data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(photo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检查硬件是否支持摄像头
     *
     * @return
     */
    private boolean checkCameraHardware() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * 摄像头的状态
     * @return
     */
    private String testCamera() {
        if (! checkCameraHardware()) {
            return "无摄像头设备";
        }
        Camera camera = null;
        String msg = "";
        try {
            camera = Camera.open();
        } catch (Exception e) {
            //一般情况下是摄像头被占用
            msg = e.getLocalizedMessage();
        } finally {
            if (camera == null) {
                msg = "没权限";
            } else {
                camera.release();
                msg = "有权限";
            }
        }
        return msg;
    }

    private String testAudio() {
        return "";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_camera_test:
                Toast.makeText(this, testCamera(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_audio_test:
                Toast.makeText(this, testAudio(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_intent_photo:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, INTENT_TACK_PHOTO);
                break;
            case R.id.btn_photo:
                startActivityForResult(new Intent(this, PhotoActivity.class), INTENT_TACK_PHOTO_MY_UI);
                break;
        }
    }
}
