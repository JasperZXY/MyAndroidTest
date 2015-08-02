package com.jasper.myandroidtest.layout;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jasper.myandroidtest.R;

/**
 * 这个例子只是证明了getLayoutParams()获取到的是当前控件的信息，并可以对其进行设置
 */
public class LayoutParamActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_param);

        ImageView iv1 = (ImageView) findViewById(R.id.iv_1);
        ImageView iv2 = (ImageView) findViewById(R.id.iv_2);
        ImageView iv3 = (ImageView) findViewById(R.id.iv_3);
        iv1.getLayoutParams().width = 120;
        iv1.getLayoutParams().height = 120;
        iv2.getLayoutParams().width = 120;
        iv2.getLayoutParams().height = 120;

        //动态设置padding跟margin
        iv3.setPadding(20, 20, 20, 20);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(120, 120);
        lp.setMargins(30, 30, 20, 20);
        //设置LayoutParams的类型要跟父控件一致，由于iv3是放在LinearLayout里的，所以用LinearLayout.LayoutParams
        iv3.setLayoutParams(lp);

        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
        ImageView imageView1 = new ImageView(this);
        imageView1.setImageResource(R.drawable.icon);
        //要先调用addView，才会有getLayoutParams()，不然会报空指针异常，因为LayoutParams要根据父控件来
        layout.addView(imageView1);
        imageView1.getLayoutParams().width = 150;

        ImageView imageView2 = new ImageView(this);
        imageView2.setImageResource(R.drawable.icon);
        layout.addView(imageView2);

        imageView2.getLayoutParams().width = 240;
    }


}
