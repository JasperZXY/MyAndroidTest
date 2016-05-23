package com.jasper.myandroidtest.camera;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.utils.Constants;
import com.jasper.myandroidtest.utils.FileUtil;
import com.jasper.myandroidtest.video.VideoViewActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    public static final int INTENT_PHOTO_PICK = 102;
    public static final int INTENT_PHOTO_CROP = 103;
    public static final int INTENT_VIDEO = 201;
    public static final int INTENT_VIDEO_MY_UI = 202;
    private ImageView imageView;
    private Uri imgUri;
    private Uri videoUri;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss");

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
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case INTENT_TACK_PHOTO:
                case INTENT_TACK_PHOTO_MY_UI:
                    try {
                        //如果intent不指定MediaStore.EXTRA_OUTPUT，则用(Bitmap) data.getExtras().get("data")
                        imageView.setImageURI(imgUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case INTENT_PHOTO_PICK:
                    photoCrop(data.getData());
                    break;

                case INTENT_PHOTO_CROP:
                    try {
                        imageView.setImageURI(imgUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case INTENT_VIDEO:
                case INTENT_VIDEO_MY_UI:
                    Toast.makeText(this, "拍摄成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "取消操作", Toast.LENGTH_SHORT).show();
        }
    }

    // 图片裁切
    private void photoCrop(Uri oriUri) {
        imgUri = Uri.fromFile(FileUtil.getCacheFile(CameraActivity.this, DATE_FORMAT.format(new Date()) + ".jpg"));
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(oriUri, "image/*");
        intent.putExtra("crop", "true"); //发送裁剪信号
        intent.putExtra("aspectX", 1);  //X方向上的比例
        intent.putExtra("aspectY", 1);  //Y方向上的比例
        intent.putExtra("outputX", 1000); //裁剪区的宽
        intent.putExtra("outputY", 1000); //裁剪区的高
        intent.putExtra("scale", true);   //是否保留比例
        intent.putExtra("return-data", false);  // 特别要注意这样要为false
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, INTENT_PHOTO_CROP);
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
     *
     * @return
     */
    private String testCamera() {
        if (!checkCameraHardware()) {
            return "无摄像头设备";
        }
        Camera camera = null;
        String msg = "";
        try {
            camera = Camera.open();
        } catch (Exception e) {
            //一般情况下是摄像头被占用
            Log.e(TAG, "testCamera error:", e);
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
        return "暂时无法判断";
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_camera_test:
                Toast.makeText(this, testCamera(), Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_audio_test:
                Toast.makeText(this, testAudio(), Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_intent_photo:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imgUri = Uri.fromFile(FileUtil.getCacheFile(CameraActivity.this, DATE_FORMAT.format(new Date()) + ".jpg"));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                startActivityForResult(intent, INTENT_TACK_PHOTO);
                break;

            case R.id.btn_photo:
                intent = new Intent(this, PhotoActivity.class);
                imgUri = Uri.fromFile(FileUtil.getCacheFile(CameraActivity.this, DATE_FORMAT.format(new Date()) + ".jpg"));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                startActivityForResult(intent, INTENT_TACK_PHOTO_MY_UI);
                break;

            case R.id.btn_photo_crop:
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, INTENT_PHOTO_PICK);
                break;

            case R.id.btn_intent_video:
                intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                videoUri = Uri.fromFile(FileUtil.getCacheFile(this, DATE_FORMAT.format(new Date()) + ".mp4"));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0.8);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                startActivityForResult(intent, INTENT_VIDEO);
                break;

            case R.id.btn_video_my_UI:
                intent = new Intent(this, RecordVideoActivity.class);
                videoUri = Uri.fromFile(FileUtil.getCacheFile(this, DATE_FORMAT.format(new Date()) + ".mp4"));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
                startActivityForResult(intent, INTENT_VIDEO_MY_UI);
                break;

            case R.id.btn_video_play:
                if (videoUri == null) {
                    Toast.makeText(this, "还没拍摄视频！！！", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(this, VideoViewActivity.class);
                    intent.putExtra(Constants.VIDEO_PATH, videoUri.getEncodedPath());
                    startActivity(intent);
                }
                break;

        }

    }
}
