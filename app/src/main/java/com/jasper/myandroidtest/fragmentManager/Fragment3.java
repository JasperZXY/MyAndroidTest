package com.jasper.myandroidtest.fragmentManager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jasper.myandroidtest.R;

public class Fragment3 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment03, null);
        final Fragment fragment = this;
        view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction tx = fm.beginTransaction();
                //如果不调用hide，Fragment3跟Fragment4会重叠
                tx.hide(fragment);
                tx.add(R.id.container, new Fragment4(), "four");
                tx.addToBackStack(null);
                tx.commit();
            }
        });
        return view;
    }

}