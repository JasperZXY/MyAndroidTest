package com.jasper.myandroidtest.resource;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jasper.myandroidtest.R;

/**
 * http://developer.android.com/guide/topics/resources/string-resource.html
 */
public class StringActivity extends Activity {

    @SuppressLint("StringFormatMatches")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_string);

        LinearLayout layout = new LinearLayout(this);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        layout.setBackgroundResource(R.drawable.bg);
        layout.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) getResources().getDimension(R.dimen.activity_padding);
        layout.setPadding(padding, padding, padding, padding);
        setContentView(layout);

        //复数的情况
        TextView tvPlurals1 = new TextView(this);
        //第一个1表示表示一个，第二个1表示传进去的参数
        tvPlurals1.setText(getResources().getQuantityString(R.plurals.test_plurals, 1, 1));
        layout.addView(tvPlurals1);
        TextView tvPlurals5 = new TextView(this);
        tvPlurals5.setText(getResources().getQuantityString(R.plurals.test_plurals, 5, 5));
        layout.addView(tvPlurals5);

        //格式化
        TextView tvFormat = new TextView(this);
        tvFormat.setText(String.format(getString(R.string.test_format, "book")));
        layout.addView(tvFormat);

        //占位符
        TextView tvHtml = new TextView(this);
        tvHtml.setText(Html.fromHtml(String.format(getString(R.string.test_html, "Jasper", 3))));
        layout.addView(tvHtml);

        //相当于一个富文本，显示各种样式
        TextView tvSpannable = new TextView(this);
        CharSequence strItalic= apply(new String[]{"Hello"}, new StyleSpan(Typeface.ITALIC));
        CharSequence strRedBold = apply(new String[]{" world"}, new StyleSpan(Typeface.BOLD), new ForegroundColorSpan(Color.RED));
        tvSpannable.setText(strItalic);
        tvSpannable.append(strRedBold);
        layout.addView(tvSpannable);
    }

    private static CharSequence apply(CharSequence[] content, Object... tags) {
        SpannableStringBuilder text = new SpannableStringBuilder();
        openTags(text, tags);
        for (CharSequence item : content) {
            text.append(item);
        }
        closeTags(text, tags);
        return text;
    }

    private static void openTags(Spannable text, Object[] tags) {
        for (Object tag : tags) {
            text.setSpan(tag, 0, 0, Spannable.SPAN_MARK_MARK);
        }
    }

    private static void closeTags(Spannable text, Object[] tags) {
        int len = text.length();
        for (Object tag : tags) {
            if (len > 0) {
                text.setSpan(tag, 0, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                text.removeSpan(tag);
            }
        }
    }

}
