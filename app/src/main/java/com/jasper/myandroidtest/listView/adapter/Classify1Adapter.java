package com.jasper.myandroidtest.listView.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.listView.entity.ContentItem;
import com.jasper.myandroidtest.listView.entity.GroupItem;
import com.jasper.myandroidtest.listView.entity.IItem;

import java.util.List;

/**
 * 分类显示，用到的方式是，布局文件同时定义了Group跟Item,
 * 若要Group，则隐藏Item；若显示Item，则隐藏Group。
 */
public class Classify1Adapter extends BaseAdapter {
    private static final String TAG = "Classify1Adapter";
    private LayoutInflater mInflater;
    private List<IItem> items;

    public Classify1Adapter(Context context, List<IItem> items) {
        this.items = items;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object info = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.phone_info, null);
            holder.group = (TextView) convertView.findViewById(R.id.tv_group);
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.detail = (TextView) convertView.findViewById(R.id.tv_detail);
            holder.layoutContent = (LinearLayout) convertView.findViewById(R.id.layout_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (info instanceof GroupItem) {
            holder.group.setVisibility(View.VISIBLE);
            holder.layoutContent.setVisibility(View.GONE);
            holder.group.setText(((GroupItem) info).getName());
        } else if (info instanceof ContentItem) {
            holder.group.setVisibility(View.GONE);
            holder.layoutContent.setVisibility(View.VISIBLE);
            holder.title.setText(((ContentItem) info).getTitle());
            holder.detail.setText(((ContentItem) info).getDetail());
        } else {
            Log.e(TAG, "can't recognize this obj:" + info);
        }
        return convertView;
    }

    private static class ViewHolder {
        public TextView title;
        public TextView detail;
        public TextView group;
        public LinearLayout layoutContent;
    }
}
