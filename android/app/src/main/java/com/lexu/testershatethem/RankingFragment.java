package com.lexu.testershatethem;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lexu.testershatethem.POJO.UserData;

import java.util.ArrayList;

/**
 * Created by lexu on 25.03.2018.
 */

public class RankingFragment extends AbstractFragment {
    private static final String TAG = RankingFragment.class.getSimpleName();

    private ArrayList<UserData> data = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof OnNavigationToDetailsListener) {
            mOnNavigationToDetailsListener = (OnNavigationToDetailsListener) context;
        }
    }

    private ListView list = null;
    private UsersListAdapter adapter = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }

    public void setData(ArrayList<UserData> users) {
        adapter = new UsersListAdapter(getContext(), R.layout.user_card_layout, users);
        if(list != null) {
            list.setAdapter(adapter);
        }
    }
}