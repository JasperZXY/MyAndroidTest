package com.jasper.myandroidtest.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jasper.myandroidtest.R;

/**
 * 有点小问题，旋转后无法知道屏幕进行了旋转
 */
public class PhotoActivity extends Activity implements View.OnClickListener, Camera.PictureCallback {
    private static final String TAG = "PhotoActivity";
    private Context context;
    private Camera camera;
    private boolean isBackCamera = true;
    private int cameraId = 0;
    private FrameLayout preview;
    private SurfaceHolder surfaceHolder;
    private Bitmap bitmap;
    private Intent intent;
    private CameraPreview cameraPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_photo);
        intent = getIntent();
        preview = (FrameLayout) findViewById(R.id.preview);
        cameraPreview = new CameraPreview(this);
        preview.addView(cameraPreview);
        findButtonAndSetOnClickListenr((ViewGroup) findViewById(R.id.layout_main));

        surfaceHolder = cameraPreview.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
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

    private boolean checkCameraHardware() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_restart:
                camera.startPreview();
                break;
            case R.id.btn_photo:
                camera.takePicture(null, null, this);
                break;
            case R.id.btn_sure:
                intent.putExtra("data", bitmap);
                setResult(CameraActivity.INTENT_TACK_PHOTO_MY_UI, intent);
                finish();
                break;
            case R.id.btn_around:
                getDispalyRotation();
                isBackCamera = !isBackCamera;
                preview.removeAllViews();
                preview.addView(cameraPreview);
                break;
        }
    }

    private int findFrontCamera() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return camIdx;
            }
        }
        return -1;
    }

    private int findBackCamera() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return camIdx;
            }
        }
        return -1;
    }

    private int getDisplayOritation(int degrees, int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    //获取手机旋转的角度
    //FIXME 这里方向的原点待确定
    private int getDispalyRotation() {
        int i = getWindowManager().getDefaultDisplay().getRotation();
        Log.i(TAG, "1 getDispalyRotation:" + i);
        switch (i) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        int degrees = getDispalyRotation();
        if (isBackCamera) {
            Matrix m = new Matrix();
            m.setRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        } else {
            Matrix m = new Matrix();
            m.setRotate(270);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            android.graphics.Camera camera2 = new android.graphics.Camera();
            camera2.rotateY(180);
            camera2.getMatrix(m);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        }
        Log.i(TAG, "onPictureTaken:" + bitmap);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(TAG, "onConfigurationChanged orientation:" + newConfig.orientation);
    }

    private class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

        public CameraPreview(Context context) {
            super(context);
            getHolder().addCallback(this);
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.d(TAG, "surfaceCreated");
            if (checkCameraHardware()) {
                if (isBackCamera) {
                    cameraId = findBackCamera();
//                    camera = Camera.open(findBackCamera());
                } else {
                    cameraId = findFrontCamera();
//                    camera = Camera.open(findFrontCamera());
                }
                camera = Camera.open(cameraId);
                  /* 创建Camera.Parameters对象 */
                Camera.Parameters parameters = camera.getParameters();
                 /* 设置相片格式为JPEG */
                parameters.setPictureFormat(PixelFormat.JPEG);
                 /* 指定preview的屏幕大小 */
                parameters.setPreviewSize(640, 480);
                    /* 设置图片分辨率大小，不能设置太大，不然Intent传输的时候会报FAILED BINDER TRANSACTION */
                parameters.setPictureSize(320, 240);
                camera.setParameters(parameters);
                camera.setDisplayOrientation(90);
                try {
                    camera.setPreviewDisplay(surfaceHolder);
                    camera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                camera.startPreview();
            } else {
                Toast.makeText(context, "摄像头不可用", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.d(TAG, String.format("surfaceChanged format:%s, width:%s, height:%s", format, width, height));
            camera.stopPreview();
            camera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.d(TAG, "surfaceDestroyed");
            if (camera != null) {
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        }
    }

}
