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
import android.text.TextUtils;
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
 * <p/>
 * 三星可以正常调用系统自带播放器播放本地视频
 */
public class VideoActivity extends Activity implements View.OnClickListener {
    private static final String TAG = VideoActivity.class.getSimpleName();
    private TextView tvFile;
    private Dialog dialog;
    private String curSelectedFile = "http://nj01ct01.baidupcs.com/file/3ea28a480fe5b6e7083654964b503edd?bkt=p2-nj-34&fid=2635120378-250528-23888087758345&time=1441188809&sign=FDTAXGERLBH-DCb740ccc5511e5e8fedcff06b081203-hF9QthG09Mz2ZKXgy2BAK9kAulo%3D&to=njhb&fm=Nan,B,T,t&sta_dx=2&sta_cs=0&sta_ft=mp4&sta_ct=0&fm2=Nanjing,B,T,t&newver=1&newfm=1&secfm=1&flow_ver=3&pkey=14003ea28a480fe5b6e7083654964b503edd16d1fc0d0000001f51e6&sl=76742735&expires=8h&rt=sh&r=232267437&mlogid=2425161345&vuk=2635120378&vbdid=1409623386&fin=%E5%A4%A7%E5%9C%A3%E5%BD%92%E6%9D%A5-%E9%A2%84%E5%91%8A%E7%89%87.mp4&fn=%E5%A4%A7%E5%9C%A3%E5%BD%92%E6%9D%A5-%E9%A2%84%E5%91%8A%E7%89%87.mp4&slt=pm&uta=0&rtype=1&iv=0&isw=0";

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
        tvFile.setText(curSelectedFile);

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
                    String strend = "";
                    if (curSelectedFile.toLowerCase().endsWith(".mp4")) {
                        strend = "mp4";
                    } else if (curSelectedFile.toLowerCase().endsWith(".3gp")) {
                        strend = "3gp";
                    } else if (curSelectedFile.toLowerCase().endsWith(".mov")) {
                        strend = "mov";
                    } else if (curSelectedFile.toLowerCase().endsWith(".wmv")) {
                        strend = "wmv";
                    }

                    if (TextUtils.isEmpty(strend)) {
                        Toast.makeText(this, "不支持的视频格式", Toast.LENGTH_SHORT).show();
                    } else {
                        intent.setDataAndType(uri, "video/" + strend);
                        if (intent.resolveActivity(getPackageManager()) == null) {
                            Toast.makeText(this, "没有匹配到可以播放的播放器", Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(intent);
                        }
                    }
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
