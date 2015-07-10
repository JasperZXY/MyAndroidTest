package com.jasper.myandroidtest.listView.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.listView.entity.ContentItem;
import com.jasper.myandroidtest.listView.entity.GroupItem;
import com.jasper.myandroidtest.listView.entity.IItem;

import java.util.List;

/**
 * 分类显示<br/>
 * 这里需要注意两个方法getItemViewType跟getViewTypeCount，
 * getViewTypeCount返回的值一定要大于getItemViewType的返回值
 * @author Jasper
 */
public class Classify2Adapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<IItem> items;
    final int VIEW_TYPE_COUNT = 2;
    final int TYPE_GROUP = 0;
    final int TYPE_CHILD = 1;

    public Classify2Adapter(Context context, List<IItem> items) {
        this.items = items;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof GroupItem) {
            return TYPE_GROUP;
        }
        return TYPE_CHILD;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
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
        ViewHolderGroup holderGroup = null;
        ViewHolderChild holderChild = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case TYPE_GROUP:
                    holderGroup = new ViewHolderGroup();
                    convertView = mInflater.inflate(R.layout.phone_info_group, null);
                    holderGroup.group = (TextView) convertView.findViewById(R.id.tv_group);
                    convertView.setTag(holderGroup);
                    break;
                case TYPE_CHILD:
                    holderChild = new ViewHolderChild();
                    convertView = mInflater.inflate(R.layout.phone_info_child, null);
                    holderChild.title = (TextView) convertView.findViewById(R.id.tv_title);
                    holderChild.detail = (TextView) convertView.findViewById(R.id.tv_detail);
                    convertView.setTag(holderChild);
                    break;
            }
        } else {
            switch (type) {
                case TYPE_GROUP:
                    holderGroup = (ViewHolderGroup) convertView.getTag();
                    break;
                case TYPE_CHILD:
                    holderChild = (ViewHolderChild) convertView.getTag();
                    break;
            }
        }
        switch (type) {
            case TYPE_GROUP:
                holderGroup.group.setText(((GroupItem) info).getName());
                break;
            case TYPE_CHILD:
                holderChild.title.setText(((ContentItem) info).getTitle());
                holderChild.detail.setText(((ContentItem) info).getDetail());
                break;
        }
        return convertView;
    }

    private static class ViewHolderGroup {
        public TextView group;
    }

    private static class ViewHolderChild {
        public TextView title;
        public TextView detail;
    }
}
