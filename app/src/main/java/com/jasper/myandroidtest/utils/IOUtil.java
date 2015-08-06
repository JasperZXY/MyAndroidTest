package com.jasper.myandroidtest.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * IO操作工具类
 */
public class IOUtil {

    public final static void close(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
