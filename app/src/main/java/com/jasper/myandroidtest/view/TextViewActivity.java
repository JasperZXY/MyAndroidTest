package com.jasper.myandroidtest.view;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jasper.myandroidtest.R;

public class TextViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        layout.setBackgroundResource(R.drawable.bg);
        layout.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) getResources().getDimension(R.dimen.activity_padding);
        layout.setPadding(padding, padding, padding, padding);
        setContentView(layout);

        TextView tv1 = new TextView(this);
        tv1.setText("文字链接是实现方式");
        layout.addView(tv1);

        TextView tv2 = new TextView(this);
        //这样这里必须要有“href”属性
        tv2.setText(Html.fromHtml("<html>文字内加跳转链接！<a href=\"\">返回</a></html>"));
        tv2.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text2 = tv2.getText();
        if (text2 instanceof Spannable) {
            int end = text2.length();
            Spannable sp = (Spannable) text2;
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(text2);
            style.clearSpans();
            for (URLSpan url : urls) {
                style.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        finish();
                    }
                }, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            tv2.setText(style);
        }
        layout.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText(Html.fromHtml("<html>文字内加跳转链接！<a href=\"https://www.baidu.com\">百度</a></html>"));
        tv3.setMovementMethod(LinkMovementMethod.getInstance());
        layout.addView(tv3);

        TextView tv4 = new TextView(this);
        String str4 = "call: 10086.";
        SpannableString sb4 = new SpannableString(str4);
        sb4.setSpan(new StyleSpan(Typeface.BOLD), 0, str4.indexOf("1"), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb4.setSpan(new URLSpan("tel:10086"), str4.indexOf("1"), str4.indexOf("."), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv4.setText(sb4);
        tv4.setMovementMethod(LinkMovementMethod.getInstance());
        layout.addView(tv4);
    }

}
