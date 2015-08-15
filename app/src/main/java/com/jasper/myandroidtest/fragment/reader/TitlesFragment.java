package com.jasper.myandroidtest.fragment.reader;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jasper.myandroidtest.R;

public class TitlesFragment extends ListFragment {
    private static final String CUR_CHOICE = "curChoice";
    private boolean mDualPane;
    private int mCurCheckPosition = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1,
                ReaderManager.getInstance().getTitles()));

        View detailsFrame = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            mCurCheckPosition = savedInstanceState.getInt(CUR_CHOICE, 0);
        }

        if (mDualPane) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showDetails(mCurCheckPosition);
        }
    }

    //横竖屏切换的数据保存，在onActivityCreated中进行数据恢复
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CUR_CHOICE, mCurCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        showDetails(position);
    }

    private void showDetails(int index) {
        mCurCheckPosition = index;

        if (mDualPane) {
            getListView().setItemChecked(index, true);

            DetailsFragment details = (DetailsFragment)
                    getFragmentManager().findFragmentById(R.id.details);
            if (details == null || details.getShownIndex() != index) {
                details = DetailsFragment.newInstance(index);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.details, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            Intent intent = new Intent();
            intent.setClass(getActivity(), DetailsActivity.class);
            intent.putExtra(ReaderManager.INDEX, index);
            startActivity(intent);
        }
    }
}