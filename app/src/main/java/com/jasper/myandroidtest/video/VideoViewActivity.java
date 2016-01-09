package com.jasper.myandroidtest.video;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.utils.Constants;

/**
 * 中文路径的视频播放不了
 */
public class VideoViewActivity extends Activity {
    public static final String VIDEO_PATH = "video_path";

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        VideoView videoView = (VideoView) findViewById(R.id.video_view);
        String videoPath = getIntent().getStringExtra(Constants.VIDEO_PATH);
        if (videoPath == null || videoPath.equals("")) {
            Toast.makeText(this, "没传视频路径", Toast.LENGTH_LONG).show();
            return;
        }
        videoView.setMediaController(new MediaController(this));
//        videoView.setVideoPath(videoPath);
        videoView.setVideoURI(Uri.parse(videoPath));
        videoView.start();
        videoView.requestFocus();

    }

    @Override
    protected void onStop() {
        if (videoView != null) {
            videoView.pause();
        }
        super.onStop();
    }
}
