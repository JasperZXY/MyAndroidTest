package com.jasper.myandroidtest.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 *  抽象BaseAdapter，减少重复的写Adapter中的代码
 */
public abstract class MyBaseAdapter<Data, Holder> extends BaseAdapter {
    protected LayoutInflater inflater;
    protected Context context;
    protected List<Data> list;

    public MyBaseAdapter(Context context, List<Data> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Data getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = newHolder();
            convertView = inflater.inflate(getResource(), parent, false);
            onInitView(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        setData(getItem(position), holder);
        return convertView;
    }

    protected abstract Holder newHolder();
    protected abstract void onInitView(View convertView, Holder holder);
    protected abstract void setData(Data data, Holder holder);
    protected abstract int getResource();

}