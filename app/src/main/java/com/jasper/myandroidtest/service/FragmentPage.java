package com.jasper.myandroidtest.service;


import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jasper.myandroidtest.R;

public class FragmentPage extends Fragment {
    private static final String TAG = FragmentPage.class.getSimpleName();
    private TextView tv1;
    private TextView tv2;
    private MyReceiver myReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, null);
        tv1 = (TextView) view.findViewById(R.id.tv1);
        tv2 = (TextView) view.findViewById(R.id.tv2);
//        final Intent serviceIntent = new Intent(MyService.MY_SERVICE_ACTION);
        final Intent serviceIntent = new Intent(getActivity(), MyService.class);
        view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceIntent.putExtra(MyService.TYPE, ServiceType.TEST4);
                getActivity().startService(serviceIntent);
            }
        });

        serviceIntent.putExtra(MyService.TYPE, ServiceType.TEST3);
        getActivity().startService(serviceIntent);

        //监听广播
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyService.MY_SERVICE_RECIVER);
        intentFilter.addCategory(ServiceType.TEST3);
        intentFilter.addCategory(ServiceType.TEST4);
        getActivity().registerReceiver(myReceiver, intentFilter);
        return view;
    }

    @Override
    public void onDestroy() {
        //注销广播
        getActivity().unregisterReceiver(myReceiver);
        super.onDestroy();
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getStringExtra(MyService.TYPE)) {
                case ServiceType.TEST3:
                    tv1.setText(intent.getStringExtra("data"));
                    break;
                case ServiceType.TEST4:
                    tv2.setText(intent.getStringExtra("data"));
                    break;
            }
        }
    }

}
