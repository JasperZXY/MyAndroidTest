package com.jasper.myandroidtest.image;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jasper.myandroidtest.R;

public class ImageViewDifferentDPIActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view_different_dpi);

        TextView tv = (TextView) findViewById(R.id.tv_img_size);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img);
        tv.setText(String.format("%s (%spx * %spx)", tv.getText(), bitmap.getWidth(), bitmap.getHeight()));
        bitmap.recycle();
        bitmap = null;

        measure();
    }

    /**
     * 计算ImageView的大小，注意界面不要轻易修改，否则这个方法要跟着修改
     */
    private void measure() {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        LinearLayout layoutContent = (LinearLayout) findViewById(R.id.layout_content);
        for (int i = 0; i < layoutContent.getChildCount(); i++) {
            View view = layoutContent.getChildAt(i);
            if (view instanceof LinearLayout) {
                LinearLayout layout = (LinearLayout) view;
                if (layout.getChildCount() == 2) {
                    View imageView = layout.getChildAt(0);
                    View textView = layout.getChildAt(1);
                    if (imageView instanceof ImageView && textView instanceof TextView) {
                        imageView.measure(w, h);
                        int height = imageView.getMeasuredHeight();
                        int width = imageView.getMeasuredWidth();

                        TextView tv = (TextView) textView;
                        tv.setText(String.format("%s (%spx * %spx)", tv.getText(), width, height));
                    }
                }
            }
        }
    }

}
