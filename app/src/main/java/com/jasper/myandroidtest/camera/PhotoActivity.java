package com.jasper.myandroidtest.camera;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.utils.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

/**
 * <strong>注意事项</strong>
 * <ul>
 * <li>需要添加权限：android.permission.CAMERA</li>
 * <li>注册OrientationEventListener，获取屏幕旋转事件</li>
 * <li>让摄像头的预览正常显示，camera.setDisplayOrientation(90);</li>
 * </ul>
 */
public class PhotoActivity extends Activity implements View.OnClickListener, Camera.PictureCallback {
    private static final String TAG = "PhotoActivity";
    private Context context;
    private Camera camera;
    private boolean isBackCamera = true;
    private int cameraId = 0;
    private FrameLayout preview;
    private SurfaceHolder surfaceHolder;
    private int curOrientation = 0;
    private Bitmap bitmap;
    private Intent intent;
    private CameraPreview cameraPreview;
    private OrientationEventListener orientationEventListener;
    private Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        imgUri = getIntent().getParcelableExtra(MediaStore.EXTRA_OUTPUT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_photo);
        intent = getIntent();
        preview = (FrameLayout) findViewById(R.id.preview);
        cameraPreview = new CameraPreview(this);
        preview.addView(cameraPreview);
        findButtonAndSetOnClickListenr((ViewGroup) findViewById(R.id.layout_main));

        surfaceHolder = cameraPreview.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //屏幕旋转事件获取
        orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                curOrientation = orientation;
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (orientationEventListener != null) {
            orientationEventListener.enable();
        }
    }

    @Override
    protected void onPause() {
        if (orientationEventListener != null) {
            orientationEventListener.disable();
        }
        super.onPause();
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
                if (imgUri == null) {
                    intent.putExtra("data", bitmap);
                } else {
                    saveBitmap(bitmap, imgUri);
                }
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.btn_around:
                isBackCamera = !isBackCamera;
                preview.removeAllViews();
                preview.addView(cameraPreview);
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void saveBitmap(Bitmap bitmap, Uri uri) {
        try (FileOutputStream fos = new FileOutputStream(uri.getEncodedPath())
        ) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            Log.e(TAG, "saveBitmap error", e);
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

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        if (isBackCamera) {
            Matrix m = new Matrix();
            m.setRotate(getDegree());
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        } else {
            Matrix m = new Matrix();
            android.graphics.Camera cameraMatrix = new android.graphics.Camera();
            cameraMatrix.rotateY(180);
            /*
             * 用下面这句，或者进行二次处理
             * m.setRotate(getDegree());
             * bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true)
             */
            cameraMatrix.rotateZ(getDegree());
            cameraMatrix.getMatrix(m);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
        }
        Log.i(TAG, "2 onPictureTaken:" + bitmap);
    }

    private int getDegree() {
        int degree = 0;
        if (curOrientation > 325 || curOrientation <= 45) {
            degree = 90;
        } else if (curOrientation > 45 && curOrientation <= 135) {
            degree = 180;
        } else if (curOrientation > 135 && curOrientation < 225) {
            degree = 270;
        }
        if (isBackCamera) {
            return degree;
        }
        return (360 - degree) % 360;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(TAG, "onConfigurationChanged isAdd:" + newConfig.orientation);
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
                } else {
                    cameraId = findFrontCamera();
                }
                camera = Camera.open(cameraId);
                  /* 创建Camera.Parameters对象 */
                Camera.Parameters parameters = camera.getParameters();
                 /* 设置相片格式为JPEG */
                parameters.setPictureFormat(PixelFormat.JPEG);
                 /* 指定preview的屏幕大小 */
                int previewWidth = 0;
                int previewHeight = 0;
                // 选择合适的预览尺寸
                List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
                // 如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择
                if (sizeList.size() > 1) {
                    Iterator<Camera.Size> itor = sizeList.iterator();
                    while (itor.hasNext()) {
                        Camera.Size cur = itor.next();
                        if (cur.width >= previewWidth
                                && cur.height >= previewHeight) {
                            previewWidth = cur.width;
                            previewHeight = cur.height;
//                            break;
                        }
                    }
                }
                parameters.setPreviewSize(previewWidth, previewHeight);
                /* 设置图片分辨率大小，不能设置太大，不然Intent传输的时候会报FAILED BINDER TRANSACTION，由于保存到文件，这里可以大一点 */
                parameters.setPictureSize(1920, 1080);
                camera.setParameters(parameters);
                camera.setDisplayOrientation(90);
                try {
                    camera.setPreviewDisplay(surfaceHolder);
                    camera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
