package com.jasper.myandroidtest.listView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jasper.myandroidtest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasper on 2015/6/8.
 */
public class MyListViewAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflater;
    private List<UserInfo> users;

    public MyListViewAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        users = new ArrayList<>();
        for (int i=0; i<100; i++) {
            UserInfo userInfo = new UserInfo();
            userInfo.setHead("");
            userInfo.setName("名字" + i);
            userInfo.setPhone("135" + (int) (Math.random() * 1e8));
            users.add(userInfo);
        }
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserInfo info = users.get(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_user_info, null);
            holder = new ViewHolder();
            holder.head = (ImageView) convertView.findViewById(R.id.img_head);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.phone = (TextView) convertView.findViewById(R.id.tv_phone);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (info.getHead() == null || info.getHead() == "") {
            holder.head.setImageResource(R.drawable.ic_launcher);
        } else {
            //加载本地或网络图片
        }
        holder.name.setText(info.getName());
        holder.phone.setText(info.getPhone());
        return convertView;
    }

    private static class ViewHolder {
        public ImageView head;
        public TextView name;
        public TextView phone;
    }
}
