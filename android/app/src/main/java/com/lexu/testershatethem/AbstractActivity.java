package com.lexu.testershatethem;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lexu.testershatethem.POJO.UserData;

import java.util.ArrayList;

/**
 * Created by lexu on 25.03.2018.
 */

abstract class AbstractActivity extends AppCompatActivity {

    protected DrawerLayout mDrawerLayout = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    protected abstract void navigateRanking();

    protected abstract void navigateUsers();

    protected abstract void navigateHistory();

    protected abstract void navigateAccount();

    protected abstract void navigateHome();
}

class UsersListAdapter extends ArrayAdapter<UserData> {

    private OnNavigationToDetailsListener mOnNavigationToDetailsListener = null;

    private ArrayList<UserData> data = null;

    UsersListAdapter(Context context, int layout, ArrayList<UserData> data) {
        super(context, layout);
        this.data = data;
        if(context instanceof OnNavigationToDetailsListener) {
            mOnNavigationToDetailsListener = (OnNavigationToDetailsListener) context;
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_card_layout, null);
        }
        ((TextView) convertView.findViewById(R.id.user_name_field)).setText(this.data.get(position).getName());
        ((TextView) convertView.findViewById(R.id.user_city_field)).setText(this.data.get(position).getCity());
        ((TextView) convertView.findViewById(R.id.user_address_field)).setText(this.data.get(position).getAddress());
        convertView.setOnClickListener(v -> mOnNavigationToDetailsListener.onNavigate(UsersListAdapter.this.data.get(position)));

        return convertView;
    }
}

interface OnNavigationToDetailsListener {
    void onNavigate(UserData user);
}