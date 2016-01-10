package com.jasper.myandroidtest.camera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.utils.UIUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 存在的问题
 * 1、视频不够清晰
 * 2、不能自定义比例
 * 3、视频旋转
 * 4、视频变形
 */
@SuppressWarnings("deprecation")
public class RecordVideoActivity extends Activity implements
        Callback, MediaPlayer.OnPreparedListener, View.OnClickListener,
        OnBufferingUpdateListener, OnCompletionListener {
    private static final String TAG = "RecordVideoActivity";
    private String videoPath;

    private MediaRecorder mediarecorder;// 录制视频的类
    private MediaPlayer mediaPlayer;//播放视频的类
    private SurfaceView surfaceview;// 显示视频的控件
    private Camera camera;
    //实现这个接口的Callback接口
    private SurfaceHolder surfaceHolder;
    /**
     * 是否正在录制true录制中 false未录制
     */
    private boolean isRecord = false;
    private boolean isCameraBack = true;
    private ImageView ivVideoRecord;
    private ImageView ivVideoPlay;
    private ImageView ivCameraFlashLight;
    private ImageView ivVideoSure;

    private int mVideoWidth;
    private int mVideoHeight;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);
        Uri videoUri = getIntent().getParcelableExtra(MediaStore.EXTRA_OUTPUT);
        videoPath = videoUri.getEncodedPath();

        // 选择支持半透明模式,在有surfaceview的activity中使用。
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        initView();
    }

    private void initView() {
        surfaceview = (SurfaceView) this.findViewById(R.id.surfaceview);
        ivVideoPlay = (ImageView) findViewById(R.id.iv_video_play);
        ivVideoPlay.setOnClickListener(this);
        ivVideoRecord = (ImageView) findViewById(R.id.iv_video_record);
        ivVideoRecord.setOnClickListener(this);
        findViewById(R.id.video_new_img_back).setOnClickListener(this);
        findViewById(R.id.iv_swich_camera).setOnClickListener(this);
        ivCameraFlashLight = (ImageView) findViewById(R.id.video_new_img_flashlight);
        ivCameraFlashLight.setOnClickListener(this);
        ivVideoSure = (ImageView) findViewById(R.id.iv_video_sure);
        ivVideoSure.setOnClickListener(this);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) surfaceview.getLayoutParams();
        int screenWidth = UIUtil.getScreenWidth(this);
        layoutParams.width = screenWidth;
        layoutParams.height = screenWidth;
        surfaceview.setLayoutParams(layoutParams);

        SurfaceHolder holder = surfaceview.getHolder();// 取得holder
        holder.addCallback(this); // holder加入回调接口
        // setType必须设置，要不出错.
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isCameraBack) {
            try {
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);//打开摄像头
            } catch (Exception e) {
                Log.e(TAG, "open camera error:", e);
            } finally {
                if (camera == null) {
                    Toast.makeText(getApplicationContext(), "摄像头被占用", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            }
            dealCamera();
        } else {
            try {
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);//打开摄像头
            } catch (Exception e) {
                Log.e(TAG, "open camera error:", e);
            } finally {
                if (camera == null) {
                    Toast.makeText(getApplicationContext(), "摄像头被占用", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
            }
            Camera.Parameters parameters = camera.getParameters();
            camera.setDisplayOrientation(90);
            camera.setParameters(parameters);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseCamera();
    }

    /**
     * 播放视频
     */
    public void playVideo() {
        ivVideoRecord.setVisibility(View.GONE);
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(videoPath);
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            Log.e(TAG, "playVideo error:", e);
        }

        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    /**
     * 开始录制/停止录制
     */
    public void recordVideo() {
        if (isRecord) {
            isRecord = false;
            ivVideoRecord.setImageResource(R.drawable.btn_video_record);
            ivVideoPlay.setVisibility(View.VISIBLE);
            if (mediarecorder != null) {
                mediarecorder.stop();
                mediarecorder.release();
                mediarecorder = null;
                camera.lock();
            }
            if (camera != null) {
                camera.release();
                camera = null;
            }
            ivVideoSure.setVisibility(View.VISIBLE);
        } else {
            isRecord = true;
            ivVideoRecord.setImageResource(R.drawable.btn_video_recording);
            ivVideoPlay.setVisibility(View.GONE);
            mediarecorder = new MediaRecorder();

            camera.unlock();

            mediarecorder.setCamera(camera);

            // Set sources
            mediarecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

            CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
            mediarecorder.setProfile(profile);

            // 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
//            mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            // 设置录制的视频编码h263 h264
//            mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
//            mediarecorder.setVideoSize(560, 560);  //这里不能随便设置，会start()失败
            // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
            mediarecorder.setVideoFrameRate(20);
            mediarecorder.setVideoEncodingBitRate(1 * 1024 * 1024);
            mediarecorder.setPreviewDisplay(surfaceHolder.getSurface());
            // 设置视频文件输出的路径
            mediarecorder.setOutputFile(videoPath);
            try {
                // 准备录制
                mediarecorder.prepare();
                mediarecorder.start();
            } catch (Exception e) {
                Log.e(TAG, "recordVideo error:", e);
            }

        }
    }

    /**
     * 更换前置后置摄像头
     */
    private void switchCamera() {
        CameraInfo cameraInfo = new CameraInfo();
        int cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数

        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if (isCameraBack) {
                //现在是后置，变更为前置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    camera.stopPreview();//停掉原来摄像头的预览
                    camera.release();//释放资源
                    camera = null;//取消原来摄像头
                    camera = Camera.open(i);//打开当前选中的摄像头
                    try {
                        dealCamera();
                        camera.setPreviewDisplay(surfaceHolder);//通过surfaceview显示取景画面
                    } catch (IOException e) {
                        Log.e(TAG, "error:", e);
                    }
                    camera.startPreview();//开始预览
                    isCameraBack = false;
                    ivCameraFlashLight.setVisibility(View.INVISIBLE);
                    break;
                }
            } else {
                //现在是前置， 变更为后置
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    camera.stopPreview();//停掉原来摄像头的预览
                    camera.release();//释放资源
                    camera = null;//取消原来摄像头
                    camera = Camera.open(i);//打开当前选中的摄像头
                    try {
                        dealCamera();
                        camera.setPreviewDisplay(surfaceHolder);//通过surfaceview显示取景画面
                    } catch (IOException e) {
                        Log.e(TAG, "error:", e);
                    }
                    camera.startPreview(); //开始预览
                    isCameraBack = true;
                    ivCameraFlashLight.setVisibility(View.VISIBLE);
                    break;
                }
            }

        }
    }

    /**
     * 闪光灯
     */
    private void flashLight() {
        if (isCameraBack && camera != null) {// 前置摄像头的时候不能切换闪光灯
            Camera.Parameters parameters = camera.getParameters();
            if (parameters.getFlashMode() != null && parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                ivCameraFlashLight.setImageResource(R.drawable.img_video_new_flashlight_close);
            } else if (parameters.getFlashMode() != null && parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                ivCameraFlashLight.setImageResource(R.drawable.img_video_new_flashlight_open);
            }
            camera.setParameters(parameters);
        }
    }

    public void dealCamera() {
        //设置camera预览的角度，因为默认图片是倾斜90度的
        camera.setDisplayOrientation(90);

        Size pictureSize;
        Size previewSize;
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewFrameRate(20);
        //设置旋转代码
//        parameters.setRotation(90);

        List<Size> supportedPictureSizes
                = SupportedSizesReflect.getSupportedPictureSizes(parameters);
        List<Size> supportedPreviewSizes
                = SupportedSizesReflect.getSupportedPreviewSizes(parameters);

        if (supportedPictureSizes != null &&
                supportedPreviewSizes != null &&
                supportedPictureSizes.size() > 0 &&
                supportedPreviewSizes.size() > 0) {

            //2.x
            pictureSize = supportedPictureSizes.get(0);

            int maxSize = UIUtil.getScreenWidth(this);
            if (maxSize > 0) {
                for (Size size : supportedPictureSizes) {
                    if (maxSize >= Math.max(size.width, size.height)) {
                        pictureSize = size;
                        break;
                    }
                }
            }

            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getMetrics(displayMetrics);

            previewSize = getOptimalPreviewSize(
                    supportedPreviewSizes,
                    display.getWidth(),
                    display.getHeight());

            parameters.setPictureSize(pictureSize.width, pictureSize.height);
            parameters.setPreviewSize(previewSize.width, previewSize.height);

        }
        camera.setParameters(parameters);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int arg1, int arg2, int arg3) {
        Log.i(TAG, "surfaceChanged");
        // 将holder，这个holder为开始在oncreat里面取得的holder，将它赋给surfaceHolder
        surfaceHolder = holder;
    }

    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated");
        surfaceHolder = holder;
        try {
            dealCamera();
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {
            Log.e(TAG, "surfaceCreated error:", e);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        Log.i(TAG, "surfaceDestroyed");
        if (camera != null) {
            camera.release();
            camera = null;
        }
        surfaceview = null;
        surfaceHolder = null;
        if (surfaceHolder != null) {
            surfaceHolder = null;
        }
        if (mediarecorder != null) {
            mediarecorder = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }


    @Override
    public void onCompletion(MediaPlayer arg0) {
        Log.i(TAG, "onCompletion");
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        ivVideoRecord.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPrepared(MediaPlayer arg0) {
        mVideoWidth = mediaPlayer.getVideoWidth();
        mVideoHeight = mediaPlayer.getVideoHeight();
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            surfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
            mediaPlayer.start();
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer player, int arg1) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_video_record:
                recordVideo();
                break;

            case R.id.iv_video_play:
                playVideo();
                break;

            case R.id.video_new_img_back:
                exitCheck();
                break;

            case R.id.iv_swich_camera:
                switchCamera();
                break;

            case R.id.video_new_img_flashlight:
                flashLight();
                break;

            case R.id.iv_video_sure:
                setResult(RESULT_OK);
                finish();
                break;

        }
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();// 停掉原来摄像头的预览
            camera.release();
            camera = null;
        }
    }

    private void exitCheck() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RecordVideoActivity.this);
        builder.setMessage("确定放弃这段视频吗？");
        builder.setTitle("温馨提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                File file = new File(videoPath);
                if (file.exists()) {
                    file.delete();
                }
                setResult(RESULT_CANCELED);
                finish();
            }

        });
        builder.create().show();
    }
}