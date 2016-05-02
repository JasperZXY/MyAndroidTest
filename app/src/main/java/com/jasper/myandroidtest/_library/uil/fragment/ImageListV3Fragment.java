/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.jasper.myandroidtest._library.uil.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest._library.uil.Constants;
import com.jasper.myandroidtest.utils.UIUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Arrays;
import java.util.List;

public class ImageListV3Fragment extends AbsListViewBaseFragment {

    public static final int INDEX = 103;
    private DisplayImageOptions options;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.uil_fr_image_list, container, false);
        listView = (ListView) rootView.findViewById(android.R.id.list);
        listView.setAdapter(new ImageAdapter(getActivity(), Arrays.asList(Constants.IMAGES)));
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startImagePagerActivity(position);
            }
        });

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.img_default)
                .showImageForEmptyUri(R.drawable.img_empty)
                .showImageOnFail(R.drawable.img_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
//                .displayer(new RoundedBitmapDisplayer(30))
                .build();

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class ImageAdapter extends BaseAdapter {
        private Context context;
        private List<String> list;

        private int width = -1;
        private int height = -1;
        private boolean isFirst = true;
        private int margin = 10;

        public ImageAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LinearLayout layout = new LinearLayout(context);
                convertView = layout;
                viewHolder.imageView = new ImageView(context);
                viewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                layout.addView(viewHolder.imageView);
                convertView.setTag(viewHolder);

                if (isFirst) {
                    height = width = UIUtil.getScreenWidth();
                    width -= margin * 2;
                    height -= margin * 2;
                    isFirst = false;
                }
                ViewGroup.MarginLayoutParams layoutParams =
                        (ViewGroup.MarginLayoutParams) viewHolder.imageView.getLayoutParams();
                layoutParams.width = width;
                layoutParams.height = height;
                layoutParams.leftMargin = margin;
                layoutParams.rightMargin = margin;
                layoutParams.topMargin = margin;
                layoutParams.bottomMargin = margin;
                viewHolder.imageView.setLayoutParams(layoutParams);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ImageLoader.getInstance().displayImage(getItem(position), viewHolder.imageView, options);
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView imageView;
    }

}
