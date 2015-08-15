package com.jasper.myandroidtest.fragment;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.jasper.myandroidtest.R;

public class ListFragmentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_fragment);
    }

    /**
     * 继承ListFragment，不需要重写onCreateView()方法
     */
    public static class MyListFragment extends ListFragment {
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            String[] strings = new String[20];
            for (int i=0; i<strings.length; i++) {
                strings[i] = "string" + i;
            }
            setListAdapter(new ArrayAdapter<>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    strings));
        }
    }

}
