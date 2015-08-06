package com.jasper.myandroidtest.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * IO操作工具类
 */
public class IOUtil {
    private static final String TAG = "IOUtil";

    public final static void close(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                Log.e(TAG, "error:" + e.getLocalizedMessage(), e);
            }
        }
    }

    public static String inputStream2String(InputStream is) {
        StringBuffer sb = new StringBuffer();
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str).append("\n");
            }
        } catch (Exception e) {
            Log.e(TAG, "error:" + e.getLocalizedMessage(), e);
        } finally {
            close(br);
            close(isr);
        }

        return sb.toString();
    }
}
