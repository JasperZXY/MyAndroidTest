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
package com.jasper.myandroidtest.library.uil.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.base.MyBaseAdapter;
import com.jasper.myandroidtest.library.uil.Constants;
import com.jasper.myandroidtest.utils.UIUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.Arrays;
import java.util.List;

public class ImageListV2Fragment extends AbsListViewBaseFragment {

    public static final int INDEX = 102;
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
                .displayer(new RoundedBitmapDisplayer(30))
                .build();

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class ImageAdapter extends MyBaseAdapter<String, ViewHolder> {
        private int width = -1;
        private int height = -1;
        private boolean isFirst = true;

        public ImageAdapter(Context context, List<String> list) {
            super(context, list);
        }

        @Override
        public int getResource() {
            return R.layout.uil_item_list_image_v2;
        }

        @Override
        protected ViewHolder newHolder() {
            return new ViewHolder();
        }

        @Override
        protected void onInitView(View convertView, ViewHolder holder) {
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            ViewGroup.MarginLayoutParams layoutParams =
                    (ViewGroup.MarginLayoutParams) holder.image.getLayoutParams();
            if (isFirst) {
                height = width = UIUtil.getScreenWidth();
                width -= layoutParams.leftMargin + layoutParams.rightMargin;
                height -= layoutParams.topMargin + layoutParams.bottomMargin;
                isFirst = false;
            }
            // 解决滑动的时候图片闪一下，控件大小要已知，现在闪动的概率大大减小
            holder.image.getLayoutParams().height = height;
            holder.image.getLayoutParams().width = width;
        }

        @Override
        protected void setData(String url, ViewHolder holder) {
            ImageLoader.getInstance().displayImage(url, holder.image, options);
        }
    }

    static class ViewHolder {
        ImageView image;
    }

}
