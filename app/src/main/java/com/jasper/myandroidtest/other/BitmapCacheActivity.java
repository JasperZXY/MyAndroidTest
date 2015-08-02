package com.jasper.myandroidtest.other;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.utils.BitmapCache;
import com.jasper.myandroidtest.utils.HttpUtil;


public class BitmapCacheActivity extends Activity {
    private static final String TAG = "BitmapCacheActivity";
    private static final String IMG_URL = "http://b.hiphotos.baidu.com/baike/g%3D0%3Bw%3D268/sign=60c889ffb051f819e1250641ad8978db/ae51f3deb48f8c54cd34cafb3a292df5e1fe7f7a.jpg";
    private static final String IMG_KEY = "bitmapcache_test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap_cache);
        new ImgAsyncTask((ImageView) findViewById(R.id.img)).execute(IMG_KEY, IMG_URL);
    }

    class ImgAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;
        public ImgAsyncTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = BitmapCache.getInstance().getBitmap(getBaseContext(), params[0]);
            if (bitmap == null) {
                bitmap = HttpUtil.downloadBitmap(params[1]);
                BitmapCache.getInstance().addBitmapAndSaveFile(getBaseContext(), bitmap, params[0]);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

}
