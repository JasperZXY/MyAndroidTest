package com.jasper.myandroidtest.video;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jasper.myandroidtest.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 小米手机的测试结果
 * 如果是本地视频，无法找到系统自带播放器进行播放，如果是网络视频，可以播放
 * 如果是VideoView，那么可以播放视频，但如果是网络视频，有时候退出会卡死
 *
 * 三星可以正常调用系统自带播放器播放本地视频
 *
 */
public class VideoActivity extends Activity implements View.OnClickListener {
    private static final String TAG = VideoActivity.class.getSimpleName();
    private TextView tvFile;
    private Dialog dialog;
    private String curSelectedFile = "http://shortv.bs2cdn.yy.com/test/video/4918b9e19febd6e265863c2a46637a5618";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        tvFile = (TextView) findViewById(R.id.tv_file);
        findViewById(R.id.btn_chose).setOnClickListener(this);
        findViewById(R.id.btn_system_player).setOnClickListener(this);
        findViewById(R.id.btn_video_view).setOnClickListener(this);
        findViewById(R.id.btn_MediaPlayer).setOnClickListener(this);

        final List<String> filePaths = getVideoPaths();
        filePaths.add(0, curSelectedFile);

        //默认是第一个视频文件
//        if (!filePaths.isEmpty()) {
//            curSelectedFile = filePaths.get(0);
//            tvFile.setText("当前文件：" + curSelectedFile);
//        }
        dialog = new AlertDialog.Builder(this)
                .setTitle("请选择")
                .setSingleChoiceItems(filePaths.toArray(new String[filePaths.size()]), 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        curSelectedFile = filePaths.get(i);
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tvFile.setText("当前文件：" + curSelectedFile);
                    }
                })
                .setNegativeButton("取消", null)
                .create();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_chose:
                dialog.show();
                break;
            case R.id.btn_system_player:
                if (curSelectedFile == null) {
                    Toast.makeText(this, "没有视频", Toast.LENGTH_SHORT).show();
                } else {
                    Uri uri = Uri.parse(curSelectedFile);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "video/mp4");
                    startActivity(intent);
                }
                break;
            case R.id.btn_video_view:
                if (curSelectedFile == null) {
                    Toast.makeText(this, "没有视频", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, VideoViewActivity.class);
                    intent.putExtra(Constants.VIDEO_PATH, curSelectedFile);
                    startActivity(intent);
                }
                break;
            case R.id.btn_MediaPlayer:
                if (curSelectedFile == null) {
                    Toast.makeText(this, "没有视频", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, MediaPlayerActivity.class);
                    intent.putExtra(Constants.VIDEO_PATH, curSelectedFile);
                    startActivity(intent);
                }
                break;
        }
    }

    private List<String> getVideoPaths() {
        List<String> retList = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        String[] projection = new String[]{MediaStore.Video.Media.DATA};
        Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
                null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        cursor.moveToFirst();
        int fileNum = cursor.getCount();

        for (int counter = 0; counter < fileNum; counter++) {
            retList.add(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));
            cursor.moveToNext();
        }
        cursor.close();
        return retList;
    }

}
