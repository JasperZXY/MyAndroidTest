package com.jasper.myandroidtest.fragment.reader;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jasper.myandroidtest.R;
import com.jasper.myandroidtest.utils.IOUtil;

import java.io.InputStream;

public class DetailsFragment extends Fragment {

    public static DetailsFragment newInstance(int index) {
        DetailsFragment f = new DetailsFragment();

        Bundle args = new Bundle();
        args.putInt(ReaderManager.INDEX, index);
        f.setArguments(args);

        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt(ReaderManager.INDEX, 0);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            //测试结果，当横屏时转换为横屏，就会container == null
            Log.i("xxx", "onCreateView container == null");
            return null;
        }

        ScrollView scroller = new ScrollView(getActivity());
        scroller.setBackgroundResource(R.drawable.bg);
        TextView text = new TextView(getActivity());
        text.setTextColor(Color.DKGRAY);
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                4, getActivity().getResources().getDisplayMetrics());
        text.setPadding(padding, padding, padding, padding);
        scroller.addView(text);
        int index = getShownIndex();
        if (index >= 0 && index < ReaderManager.getInstance().getItems().size()) {
            ReaderManager.Item item = ReaderManager.getInstance().getItems().get(index);
            getActivity().setTitle(item.name);

            AssetManager am = getActivity().getAssets();
            InputStream is = null;
            try {
                is = am.open(item.path);
                String note = IOUtil.inputStream2String(is);
                if (note == null || note == "") {
                    text.setText("error");
                }
                text.setText(note);
            } catch (Exception e) {
                text.setText("error:" + e.getLocalizedMessage());
            } finally {
                IOUtil.close(is);
            }
        }
        return scroller;
    }
}