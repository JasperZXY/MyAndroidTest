package com.jasper.myandroidtest.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Jasper on 2016/1/9.
 */
public class FileUtil {
    private static final String TAG = "FileUtil";

    public static File getCacheFileByTime(Context context) {
        return getCacheFile(context, System.currentTimeMillis() + "");
    }

    public static File getCacheFile(Context context, String filename) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return new File(context.getExternalCacheDir(), filename);
        }
        return new File(context.getCacheDir(), filename);
    }

    /**
     * 删除文件或文件夹及其里面的所有文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file == null) {
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFile(files[i]);
            }
        }
        file.delete();
    }

    /**
     * 获取文件夹及其所有文件的大小
     * @param file
     * @return
     */
    public static long getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                long size = 0;
                for (File f : files)
                    size += getDirSize(f);
                return size;
            } else {
                return file.length();
            }
        } else {
            return 0;
        }
    }

    public static String getContent(String path) {
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(path);
            br = new BufferedReader(fr, 1024);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            Log.e(TAG, "getContent error, path:" + path, e);
        } finally {
            IOUtil.close(br);
            IOUtil.close(fr);
        }
        return null;
    }

}