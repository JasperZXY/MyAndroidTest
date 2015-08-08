package com.jasper.myandroidtest.listView.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.listView.entity.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * <ol>
 * <li>运行发现，如果ListView的高度不确定，会一直调用getView方法，所以需要设置高度，选择fill_parent或者设定数值高度</li>
 * </ol>
 */
public class SimpleListViewAdapter extends BaseAdapter {
    private static final String TAG = "SimpleListViewAdapter";
    private Context context;
    private LayoutInflater mInflater;
    private List<UserInfo> users;

    public SimpleListViewAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        users = new ArrayList<>();
        for (int i=0; i<50; i++) {
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
        Log.d(TAG, String.format("2 getView position:%s, converView:%s", position, convertView));
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
            holder.head.setImageResource(R.drawable.android);
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
