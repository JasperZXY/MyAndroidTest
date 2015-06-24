package com.jasper.myandroidtest.video;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jasper.myandroidtest.R;

public class MediaPlayerActivity extends Activity implements SurfaceHolder.Callback,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnCompletionListener {
    private static final String TAG = "MediaPlayerActivity";
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private String videoPath = null;
    private int screenWidth;
    private int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_media_player);
        //支持屏幕旋转
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        videoPath = getIntent().getStringExtra(Constants.VIDEO_PATH);
        Log.i(TAG, "version 11");
        if (videoPath == null || videoPath.equals("")) {
            Toast.makeText(this, "没传视频路径", Toast.LENGTH_LONG).show();
            return;
        }
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        DisplayMetrics mDisplayMetrics = new DisplayMetrics();//屏幕分辨率容器
        Display display = getWindowManager().getDefaultDisplay();
        display.getMetrics(mDisplayMetrics);
        screenWidth = mDisplayMetrics.widthPixels;
        screenHeight = mDisplayMetrics.heightPixels;

    }

    private void playVideo() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(videoPath);
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            //e.printStackTrace();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("视频无法播放");
            builder.setMessage(e.getLocalizedMessage());
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.create().show();
        }

        mediaPlayer.setOnBufferingUpdateListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        //从视频中随便获取一帧来获取视频的宽跟高
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(videoPath);
//        Bitmap bitmap = retriever.getFrameAtTime();
//        width = bitmap.getWidth();
//        height = bitmap.getHeight();
//        bitmap.recycle();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        playVideo();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.i(TAG, "Surface Changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.i(TAG, "Surface Destroyed");
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.v(TAG, "onPrepared called");
        if (mediaPlayer.getVideoWidth() > 0 && mediaPlayer.getVideoHeight() > 0) {
            int sw = screenWidth;
            int sh = screenHeight;
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                sw = screenHeight;
                sh = screenWidth;
            }
            float rw = 1.0f * sw / mediaPlayer.getVideoWidth();
            float rh = 1.0f * sh / mediaPlayer.getVideoHeight();
//            /* 设置视频的宽度和高度 */
            if (rw > rh) {
                Log.i(TAG, String.format("newWidth:%s, newHeight:%s", (int) (rh * mediaPlayer.getVideoWidth()), (int) (rh * mediaPlayer.getVideoHeight())));
                Log.i(TAG, String.format("newWidth:%s, newHeight:%s", (int) (rw * mediaPlayer.getVideoWidth()), (int) (rw * mediaPlayer.getVideoHeight())));
                surfaceHolder.setFixedSize((int) (rh * mediaPlayer.getVideoWidth()), (int) (rh * mediaPlayer.getVideoHeight()));
            } else {
                surfaceHolder.setFixedSize((int) (rw * mediaPlayer.getVideoWidth()), (int) (rw * mediaPlayer.getVideoHeight()));
            }
            /* 开始播放 */
            mediaPlayer.start();
        }
    }
}
