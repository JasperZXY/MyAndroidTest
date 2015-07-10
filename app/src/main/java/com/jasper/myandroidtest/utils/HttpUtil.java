package com.jasper.myandroidtest.utils;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.List;

/**
 * @author Jasper
 */
public class HttpUtil {
    private static final String TAG = "HttpUtil";

    public static String httpGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = httpResponse.getEntity();
                if (entity == null) {
                    Log.e(TAG, "httpGet errror, entity is null");
                } else {
                    return EntityUtils.toString(entity, HTTP.UTF_8);
                }
            } else {
                Log.e(TAG, "httpGet errror, statusCode=" + httpResponse.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            Log.e(TAG, "httpGet errror:" + e.getLocalizedMessage());
        }
        return null;
    }

    public static String httpPost(String url, List<NameValuePair> params) {
        HttpPost post = new HttpPost(url);
        HttpResponse response;
        try {
            post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            response = new DefaultHttpClient().execute(post);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return EntityUtils.toString(response.getEntity());
            }
            Log.e(TAG, "httpPost error, status code:" + response.getStatusLine().getStatusCode());
            return null;
        } catch (Exception e) {
            Log.e(TAG, "httpPost error:" + e.getMessage());
        }
        return null;
    }
}
