package com.jasper.myandroidtest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class WebViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        final String mimeType = "text/html";
        myWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
        myWebView.loadData("<input type=\"button\" value=\"Say hello\" onClick=\"Android.showToast('hello')\" />", mimeType, null);

        WebView myWebView2 = (WebView) findViewById(R.id.webview2);
        myWebView2.setBackgroundColor(0);
        /**
         * 这个需要在res的同级目录放一个文件夹assets，
         * 这个文件夹下的文件就不会被编译成二进制字节码，
         * 然后就可以通过file:///android_asset/进行引用了
         */
        myWebView2.loadUrl("file:///android_asset/qh.gif");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
    }
}
