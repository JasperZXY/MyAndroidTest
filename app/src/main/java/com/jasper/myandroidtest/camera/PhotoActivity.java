package com.jasper.myandroidtest.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.jasper.myandroidtest.R;

public class PhotoActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "PhotoActivity";
    private Context context;

    private int cameraOrientation = 0;

    private static Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout preview;
    private Handler handler;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_photo);
        intent = getIntent();
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        findButtonAndSetOnClickListenr((ViewGroup) findViewById(R.id.layout_main));
    }

    private void findButtonAndSetOnClickListenr(ViewGroup viewGroup) {
        for (int i=0; i<viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i) instanceof Button) {
                viewGroup.getChildAt(i).setOnClickListener(this);
            } else if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                findButtonAndSetOnClickListenr((ViewGroup) viewGroup.getChildAt(i));
            }
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

    private boolean checkCameraHardware() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                if (checkCameraHardware()) {
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
            Log.i(TAG, "11  onPictureTaken:" + data.length + " camera:" + camera + " mCamera:" + mCamera);
            try {
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.outHeight = 30;
//                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
                intent.getExtras().putParcelable("data", BitmapFactory.decodeByteArray(data, 0, data.length));
                setResult(CameraActivity.INTENT_TACK_PHOTO_MY_UI, intent);
                finish();
//                imageView.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
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
