package com.jasper.myandroidtest.fragment.reader;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReaderManager {
    private static final String TAG = "ReaderManager";
    public static final String INDEX = "index";
    private static ReaderManager readerManager = new ReaderManager();
    private List<Item> items;
    private List<String> titles;
    private boolean isInit = false;

    public static ReaderManager getInstance() {
        return readerManager;
    }

    private ReaderManager() {}

    public void init(Context context) {
        if (isInit) {
            return;
        }
        items = new ArrayList<>();
        titles = new ArrayList<>();
        AssetManager am = context.getAssets();
        long curTime = SystemClock.elapsedRealtime();
        getPath(am, "note");
        Log.i(TAG, "init 花费时间：" + (SystemClock.elapsedRealtime() - curTime) + "ms");
        isInit = true;
        //这里不能调用close方法，不然会抛异常，因为系统也要进行调用
//        am.close();
    }

    private void getPath(AssetManager am, String dir) {
        String[] ps = null;
        try {
            ps = am.list(dir);
        } catch (IOException e) {
            Log.e(TAG, "am.list error", e);
        }
        if (ps == null || ps.length == 0) {   //间接判断这个不是文件夹
            String name = dir;
            int start = name.lastIndexOf("/");
            if (start > 0) {
                name = name.substring(start + 1, name.length());
            }
            int end = name.lastIndexOf(".txt");
            if (end > 0) {
                name = name.substring(0, end);
            }
            titles.add(name);
            items.add(new Item(name, dir));
        } else {
            List<String> list = new ArrayList<>(Arrays.asList(ps));
            Collections.sort(list, new Comparator<String>() {
                @Override
                public int compare(String lhs, String rhs) {
                    return lhs.compareToIgnoreCase(rhs);
                }
            });
            for (String path : list) {
                getPath(am, dir + "/" + path);
            }
        }
    }

    public void release() {
        if (items != null) {
            items.clear();
        }
        if (titles != null) {
            titles.clear();
        }
        isInit = false;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<String> getTitles() {
        return titles;
    }

    public class Item {
        public String name;
        public String path;
        public Item(String name, String path) {
            this.name = name;
            this.path = path;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "name='" + name + '\'' +
                    ", path='" + path + '\'' +
                    '}';
        }
    }
}
