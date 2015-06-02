package com.jasper.myandroidtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 摄像头操作
 * 1. 通过Intent调用系统功能进行拍照，不需要权限
 * 2. 自定义拍摄UI，需要添加权限
 * <p/>
 * 注：经常报Bitmap too large to be uploaded into a texture的意思是图片超过了硬件加速所规定的高度
 * 在AndroidManifest.xml中对应的Activity添加android:hardwareAccelerated="false"
 */
public class CameraActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "CameraActivity";

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private Button btn;
    private Button btnStart;
    private Button btnSure;
    private Button btnOri;
    private ImageView imageView;
    private FrameLayout preview;
    private Context context;

    private int cameraOrientation = 0;

    private static Camera mCamera;
    private CameraPreview mPreview;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        context = this;

        preview = (FrameLayout) findViewById(R.id.camera_preview);
        imageView = (ImageView) findViewById(R.id.imageview);
        btn = (Button) findViewById(R.id.btn);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnSure = (Button) findViewById(R.id.btn_sure);
        btnOri = (Button) findViewById(R.id.btn_ori);
        btn.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnSure.setOnClickListener(this);
        btnOri.setOnClickListener(this);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }

    /**
     * 检查硬件是否支持摄像头
     *
     * @param context
     * @return
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    public static Camera getCameraInstance() {
//        Camera c = null;
//        try {
//            c = Camera.open(); // attempt to get a Camera instance
//        }
//        catch (Exception e){
//            Log.e(TAG, "Camera is not available (in use or does not exist)");
//        }
//        return c; // returns null if camera is unavailable
        if (mCamera == null) {
            try {
                mCamera = Camera.open(); // attempt to get a Camera instance
            } catch (Exception e) {
                Log.e(TAG, "Camera is not available (in use or does not exist)");
            }
        }
        return mCamera; // returns null if camera is unavailable
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                break;
            case R.id.btn_start:
                if (checkCameraHardware(context)) {
                    if (mCamera == null) {
                        try {
                            mCamera = Camera.open(); // attempt to get a Camera instance
                        } catch (Exception e) {
                            Log.e(TAG, "Camera is not available (in use or does not exist)");
                        }
                    }
                    mPreview = new CameraPreview(this, mCamera);
                    preview.addView(mPreview);
                } else {
                    Toast.makeText(context, "摄像头不可用", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_sure:
                mCamera.takePicture(null, null, mPicture);
                preview.removeAllViews();
                break;
            case R.id.btn_ori:
                cameraOrientation = (cameraOrientation + 90) % 360;
                mCamera.setDisplayOrientation(cameraOrientation);
                break;
        }
    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            Log.i(TAG, "onPictureTaken:" + data.length + " camera:" + camera + " mCamera:" + mCamera);
            try {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.outHeight = 30;
//                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
//                File file = new File(Environment.getExternalStorageDirectory(), "/test/1.jpg");
//                if (! file.getParentFile().exists()) {
//                    file.getParentFile().mkdirs();
//                }
//                FileOutputStream fos = new FileOutputStream(file);
//                fos.write(data);
//                fos.close();

//                imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            } catch (Exception e) {
                Log.e(TAG, "setImageBitmap error:" + e.getLocalizedMessage());
                e.printStackTrace();
            }
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                }
//            });

//            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
//            if (pictureFile == null){
//                Log.d(TAG, "Error creating media file, check storage permissions: " +
//                        e.getMessage());
//                return;
//            }
//
//            try {
//                FileOutputStream fos = new FileOutputStream(pictureFile);
//                fos.write(data);
//                fos.close();
//            } catch (FileNotFoundException e) {
//                Log.d(TAG, "File not found: " + e.getMessage());
//            } catch (IOException e) {
//                Log.d(TAG, "Error accessing file: " + e.getMessage());
//            }
        }
    };


    class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder mHolder;
        private Camera mCamera;

        public CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera;

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            Log.d(TAG, "surfaceCreated");
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (Exception e) {
                Log.d(TAG, "Error setting camera preview: " + e.getMessage());
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // empty. Take care of releasing the Camera preview in your activity.
            Log.d(TAG, "surfaceDestroyed");
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.
            Log.d(TAG, "surfaceChanged");

            if (mHolder.getSurface() == null) {
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e) {
                // ignore: tried to stop a non-existent preview
            }

            // set preview size and make any resize, rotate or
            // reformatting changes here

            // start preview with new settings
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e) {
                Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            }
        }
    }
}
