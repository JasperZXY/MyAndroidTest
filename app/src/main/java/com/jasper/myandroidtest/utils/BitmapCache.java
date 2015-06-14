package com.jasper.myandroidtest.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.Hashtable;

/**
 * 图片缓存，内存跟文件存储二级缓存
 */
public class BitmapCache {
    private static final String TAG = "BitmapCache";
    private static BitmapCache cache;
    private static Context context;
    /** 用于Chche内容的存储 */
    private Hashtable<String, MySoftRef> hashRefs;
    /** 垃圾Reference的队列（所引用的对象已经被回收，则将该引用存入队列中） */
    private ReferenceQueue<Bitmap> q;

    /**
     * 继承SoftReference，使得每一个实例都具有可识别的标识。
     */
    private class MySoftRef extends SoftReference<Bitmap> {
        private String key = "";

        public MySoftRef(Bitmap bmp, ReferenceQueue<Bitmap> q, String key) {
            super (bmp, q);
            this.key = key;
        }
    }

    private BitmapCache() {
        hashRefs = new Hashtable<>();
        q = new ReferenceQueue<>();
    }

    /**
     * 取得缓存器实例
     */
    public static BitmapCache getInstance(Context context) {
        if (cache == null ) {
            cache = new BitmapCache();
        }
        BitmapCache.context = context;
        return cache;
    }

    /**
     * 以软引用的方式对一个Bitmap对象的实例进行引用并保存该引用
     */
    public void addBitmapAndSaveFile(Bitmap bmp, String key) {
        addBitmap(bmp, key);
        FileOutputStream fos = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            fos = new FileOutputStream(getFilePath(key), false);
            //FIXME 这里选择的是WEBP图片格式，待确定
            bmp.compress(Bitmap.CompressFormat.WEBP, 100, baos);
            fos.write(baos.toByteArray());
        } catch (Exception e) {
            Log.e(TAG, "addCacheBitmap to cacheDir error:" + e.getLocalizedMessage());
        } finally {
            closeIO(fos);
            closeIO(baos);
        }
    }

    public void addBitmap(Bitmap bmp, String key) {
        cleanCache(); // 清除垃圾引用
        MySoftRef ref = new MySoftRef(bmp, q, key);
        hashRefs.put(key, ref);
    }

    private static final void closeIO(Closeable obj) {
        if (obj != null) {
            try {
                obj.close();
            } catch (IOException e) {
                Log.e(TAG, "closeIO error:" + e.getLocalizedMessage());
            }
        }
    }

    public Bitmap getBitmap( String key) {
        Bitmap bmp = null ;
        if (hashRefs.containsKey(key)) {
            MySoftRef ref = hashRefs.get(key);
            bmp = ref.get();
        }
        if (bmp == null) {
            bmp = BitmapFactory.decodeFile(getFilePath(key));
            if (bmp != null) {
                addBitmap(bmp, key);
            }
        }
        return bmp;
    }

    //优先考虑外置SDCARD，没有再用内置私有目录
    private final String getFilePath(String key) {
        File sdcache = context.getExternalCacheDir();
        if (sdcache != null) {
            return sdcache.getPath() + "/" + key;
        }
        return context.getCacheDir().getPath() + "/" + key;
    }

    private void cleanCache() {
        MySoftRef ref ;
        while ((ref = (MySoftRef) q.poll()) != null ) {
            hashRefs.remove(ref.key);
        }
    }

    /**
     * 清除Cache内的全部内容
     */
    public void clearCache() {
        cleanCache();
        hashRefs.clear();
        System.gc();
        System.runFinalization();
    }
}
