package com.jasper.myandroidtest.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.jasper.myandroidtest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * GridView 一行显示
 */
public class GridViewActivity extends Activity {
    private static final int IMG_WIDTH = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);

        GridView gridView = (GridView) findViewById(R.id.gridview);
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.icon);
        list.add(R.drawable.icon_home_sel);
        list.add(R.drawable.icon_selfinfo_sel);
        list.add(R.drawable.icon);
        list.add(R.drawable.icon_home_sel);
        list.add(R.drawable.icon_selfinfo_sel);
        list.add(R.drawable.icon);
        list.add(R.drawable.icon_home_sel);
        list.add(R.drawable.icon_selfinfo_sel);
        list.add(R.drawable.icon);
        list.add(R.drawable.icon_home_sel);
        list.add(R.drawable.icon_selfinfo_sel);
        list.add(R.drawable.icon);
        list.add(R.drawable.icon_home_sel);
        list.add(R.drawable.icon_selfinfo_sel);
        list.add(R.drawable.icon);
        list.add(R.drawable.icon_home_sel);
        list.add(R.drawable.icon_selfinfo_sel);
        list.add(R.drawable.icon);
        list.add(R.drawable.icon_home_sel);
        list.add(R.drawable.icon_selfinfo_sel);
        gridView.setAdapter(new MyAdapter(this, list));

        ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
        //要实现一行显示，重点是这两行代码
        layoutParams.width = IMG_WIDTH * list.size();
        gridView.setNumColumns(list.size());
    }

    private class MyAdapter extends BaseAdapter {
        private Context context;
        private List<Integer> list;

        public MyAdapter(Context context, List<Integer> list) {
            this.context = context;
            this.list = list;
        }

        public int getCount() {
            return list.size();
        }

        public Object getItem(int position) {
            return list.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(IMG_WIDTH, IMG_WIDTH));
            imageView.setPadding(5, 5, 5, 5);
            imageView.setImageResource(list.get(position));
            return imageView;
        }
    }

}
