package com.jasper.myandroidtest.utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by Jasper on 2016/1/9.
 */
public class FileUtil {
    public static File getCacheFileByTime(Context context) {
        return getCacheFile(context, System.currentTimeMillis() + "");
    }

    public static File getCacheFile(Context context, String filename) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return new File(context.getExternalCacheDir(), filename);
        }
        return new File(context.getCacheDir(), filename);
    }

    public static String Uri2String(Uri uri) {
        if (uri == null) {
            return null;
        }
        String tmp = uri.toString();
        return tmp.substring(tmp.indexOf("//") + 2);
    }

}