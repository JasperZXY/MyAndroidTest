package com.jasper.myandroidtest.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jasper.myandroidtest.listView.entity.ContentItem;

/**
 * Created by Jasper on 2016/1/1.
 */
public class UIUtil {

    /**
     * 获取Activity中的主容器
     *
     * @param activity
     * @return
     */
    public static ViewGroup getMainContainer(Activity activity) {
        return (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
    }

    /**
     * 找出Activity中的所有Button并设置点击事件
     *
     * @param activity
     * @param listener
     */
    public static void findButtonAndSetOnClickListenr(Activity activity, View.OnClickListener listener) {
        findButtonAndSetOnClickListenr(getMainContainer(activity), listener);
    }

    private static void findButtonAndSetOnClickListenr(ViewGroup viewGroup, View.OnClickListener listener) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i) instanceof Button) {
                viewGroup.getChildAt(i).setOnClickListener(listener);
            } else if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                findButtonAndSetOnClickListenr((ViewGroup) viewGroup.getChildAt(i), listener);
            }
        }
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
}
