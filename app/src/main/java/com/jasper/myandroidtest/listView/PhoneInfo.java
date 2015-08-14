package com.jasper.myandroidtest.listView;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.jasper.myandroidtest.listView.entity.ContentItem;
import com.jasper.myandroidtest.listView.entity.GroupItem;
import com.jasper.myandroidtest.listView.entity.IItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jasper
 */
public class PhoneInfo {

    public List<IItem> getData(Activity activity) {
        List<IItem> items = new ArrayList<>();
        getPhoneInfo(activity, items);
        getScreenInfo(activity, items);
        getMemoryInfo(activity, items);
        getStorageInfo(activity, items);
        getNetworkInfo(activity, items);
        getTelephonyInfo(activity, items);
        return items;
    }

    private void getPhoneInfo(Activity activity, List<IItem> items) {
        items.add(new GroupItem("手机"));
        items.add(new ContentItem("手机型号", android.os.Build.MODEL));
        items.add(new ContentItem("SDK版本", android.os.Build.VERSION.SDK));
        items.add(new ContentItem("系统版本", android.os.Build.VERSION.RELEASE));
        try {
            PackageManager packageManager = activity.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo("com.jasper.myandroidtest", 0);
            items.add(new ContentItem("本软件版本", packageInfo.versionName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getScreenInfo(Activity activity, List<IItem> items) {
        items.add(new GroupItem("屏幕信息"));
        DisplayMetrics displayMetrics = new DisplayMetrics();//屏幕分辨率容器
        //(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        items.add(new ContentItem("大小", String.format("%spx * %spx", displayMetrics.widthPixels, displayMetrics.heightPixels)));
        //同上面的一致，不过getWidth跟getHeight已经是Deprecated的了
//        items.addButton(new ContentItem("大小", String.format("%spx * %spx", display.getWidth(), display.getHeight())));
        items.add(new ContentItem("density", Float.toString(displayMetrics.density)));
        items.add(new ContentItem("densityDpi", Integer.toString(displayMetrics.densityDpi)));
    }

    private void getMemoryInfo(Activity activity, List<IItem> items) {
        items.add(new GroupItem("内存"));
        ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        items.add(new ContentItem("总共", Formatter.formatFileSize(activity, mi.totalMem)));
        items.add(new ContentItem("可用", Formatter.formatFileSize(activity, mi.availMem)));
    }

    private void getStorageInfo(Activity activity, List<IItem> items) {
        items.add(new GroupItem("系统存储"));
        File root = Environment.getRootDirectory();
        StatFs sfRoot = new StatFs(root.getPath());
        items.add(new ContentItem("block大小", Formatter.formatFileSize(activity, sfRoot.getBlockSizeLong())));
        items.add(new ContentItem("block数目", Long.toString(sfRoot.getBlockCountLong())));
        items.add(new ContentItem("总大小", Formatter.formatFileSize(activity, sfRoot.getBlockSizeLong() * sfRoot.getBlockCountLong())));
        items.add(new ContentItem("可用的block数目", Long.toString(sfRoot.getAvailableBlocksLong())));
        items.add(new ContentItem("剩余", Formatter.formatFileSize(activity, sfRoot.getBlockSizeLong() * sfRoot.getAvailableBlocksLong())));

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            items.add(new GroupItem("外部存储"));
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            items.add(new ContentItem("block大小", Formatter.formatFileSize(activity, sf.getBlockSizeLong())));
            items.add(new ContentItem("block数目", Long.toString(sf.getBlockCountLong())));
            items.add(new ContentItem("总大小", Formatter.formatFileSize(activity, sf.getBlockSizeLong() * sf.getBlockCountLong())));
            items.add(new ContentItem("可用的block数目", Long.toString(sf.getAvailableBlocksLong())));
            items.add(new ContentItem("剩余", Formatter.formatFileSize(activity, sf.getBlockSizeLong() * sf.getAvailableBlocksLong())));
        }
    }

    /**
     * 获取网络状态，需要权限
     */
    private void getNetworkInfo(Activity activity, List<IItem> items) {
        items.add(new GroupItem("网络信息"));
        ConnectivityManager connectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo activeNetInfo = connectivity.getActiveNetworkInfo();
            if (activeNetInfo != null) {
                items.add(new ContentItem("DetailedState", activeNetInfo.getDetailedState().name()));
                items.add(new ContentItem("State", activeNetInfo.getState().name()));
                /** 这里常用于判断当前网络是否WiFi,{@link ConnectivityManager#TYPE_WIFI} */
                items.add(new ContentItem("Type", activeNetInfo.getType() + ""));
                items.add(new ContentItem("TypeName", activeNetInfo.getTypeName()));
                items.add(new ContentItem("ExtraInfo", activeNetInfo.getExtraInfo()));
                items.add(new ContentItem("Reason", activeNetInfo.getReason()));
                items.add(new ContentItem("Subtype", activeNetInfo.getSubtype() + ""));
                items.add(new ContentItem("SubtypeName", activeNetInfo.getSubtypeName()));
            }
            items.add(new ContentItem("网络+WIFI", connectivity.getAllNetworkInfo().length + "个"));

            NetworkInfo mobNetInfo = connectivity.getNetworkInfo( ConnectivityManager.TYPE_MOBILE );
            if (mobNetInfo != null) {
//                items.addButton(new ContentItem("NetState_" + mobNetInfo.getTypeName(), mobNetInfo.getExtraInfo()));
                items.add(new ContentItem("手机网络", mobNetInfo.getExtraInfo()));
            }
        }
    }

    /**
     * 获取电话信息，需要添加权限android.permission.READ_PHONE_STATE跟其他权限
     */
    private void getTelephonyInfo(Activity activity, List<IItem> items) {
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(activity.TELEPHONY_SERVICE);
        items.add(new GroupItem("电话信息"));
        switch (tm.getCallState()) {
            case TelephonyManager.CALL_STATE_IDLE:
                items.add(new ContentItem("状态", "无活动"));
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                items.add(new ContentItem("状态", "响铃"));
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                items.add(new ContentItem("状态", "摘机"));
                break;
        }
        //需要添加权限android.permission.ACCESS_COARSE_LOCATION
        try {
            items.add(new ContentItem("方位", tm.getCellLocation().toString()));
        } catch (Exception e) {
            items.add(new ContentItem("方位", "获取失败"));
        }
        items.add(new ContentItem("设备ID", tm.getDeviceId()));
        items.add(new ContentItem("软件版本号", tm.getDeviceSoftwareVersion()));
        items.add(new ContentItem("手机号", tm.getLine1Number()));
        List<NeighboringCellInfo> neighboringCellInfos = tm.getNeighboringCellInfo();
        for (int i=0; i<neighboringCellInfos.size(); i++) {
            NeighboringCellInfo info = neighboringCellInfos.get(i);
            items.add(new ContentItem("附近的电话的信息" + (i+1), String.format("NetworkType:%s\ncid:%s\nlac:%s\npsc:%s\nrssi:%s",
                    info.getNetworkType(), info.getCid(), info.getLac(), info.getPsc(), info.getRssi())));
        }
        items.add(new ContentItem("ISO标准的国家码", tm.getNetworkCountryIso()));
        items.add(new ContentItem("mobile country code + mobile network code", tm.getNetworkOperator()));
        items.add(new ContentItem("网络", tm.getNetworkOperatorName()));
        items.add(new ContentItem("网络类型", tm.getNetworkType() + ""));
        switch (tm.getPhoneType()) {
            case TelephonyManager.PHONE_TYPE_NONE:
                items.add(new ContentItem("手机类型", "无信号"));
                break;
            case TelephonyManager.PHONE_TYPE_CDMA:
                items.add(new ContentItem("手机类型", "CMDA"));
                break;
            case TelephonyManager.PHONE_TYPE_SIP:
                items.add(new ContentItem("手机类型", "SIP"));
                break;
            case TelephonyManager.PHONE_TYPE_GSM:
                items.add(new ContentItem("手机类型", "GSM"));
                break;
        }
        items.add(new ContentItem("ISO国家码", tm.getSimCountryIso()));
        items.add(new ContentItem("SIM卡", tm.getSimOperator()));
        items.add(new ContentItem("服务商名称", tm.getSimOperatorName()));
        items.add(new ContentItem("SIM卡的序列号", tm.getSimSerialNumber()));
        items.add(new ContentItem("SIM的状态信息", tm.getSimState() + ""));
        items.add(new ContentItem("唯一的用户ID", tm.getSubscriberId()));
        items.add(new ContentItem("漫游", tm.isNetworkRoaming() ? "是" : "否"));
    }



}
