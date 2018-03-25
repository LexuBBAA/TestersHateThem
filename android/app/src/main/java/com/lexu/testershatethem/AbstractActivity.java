package com.lexu.testershatethem;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

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

    public static class UsersFragment extends Fragment {
        private static final String TAG = UsersFragment.class.getSimpleName();

        public static final String DATA_KEY = "data";
        public static final String FRAGMENT_TYPE = "fragment_type";

        private int fragmentType = -1;
        private ArrayList<UserData> data = null;
        private OnNavigationToDetailsListener mOnNavigationToDetailsListener = null;

        public interface OnRecylerItemClickedListener {
            void onClick(ListAdapter.DataHolder holder);
        }

        public UsersFragment() {
            super();
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);

            if(context instanceof OnNavigationToDetailsListener) {
                mOnNavigationToDetailsListener = (OnNavigationToDetailsListener) context;
            }

            Bundle args = getArguments();
            if(args != null) {
                if(args.containsKey(DATA_KEY)) {
                    this.data = (ArrayList<UserData>) args.getSerializable(DATA_KEY);
                }

                if(args.containsKey(FRAGMENT_TYPE)) {
                    this.fragmentType = args.getInt(FRAGMENT_TYPE);
                }
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_users_layout, container);

            RecyclerView list = (RecyclerView) view.findViewById(R.id.list_view);
            ListAdapter adapter = new ListAdapter(getContext(), this.data);
            adapter.setOnRecylerItemClickedListener(new OnRecylerItemClickedListener() {
                @Override
                public void onClick(ListAdapter.DataHolder holder) {
                    mOnNavigationToDetailsListener.onNavigate(holder.getItem());
                }
            });
            list.setAdapter(adapter);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }
}

class ListAdapter extends RecyclerView.Adapter {

    private AbstractActivity.UsersFragment.OnRecylerItemClickedListener mOnRecylerItemClickedListener = null;
    private Context mContext = null;
    private ArrayList<UserData> data = null;

    ListAdapter(Context context, ArrayList<UserData> data) {
        super();
        this.data = data;
        mContext = context;
    }

    void setOnRecylerItemClickedListener(AbstractActivity.UsersFragment.OnRecylerItemClickedListener onRecylerItemClickedListener) {
        mOnRecylerItemClickedListener = onRecylerItemClickedListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_card_layout, parent);

        return new DataHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof DataHolder) {
            ((DataHolder) holder).bind(this.data.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return this.data != null ? this.data.size(): 0;
    }

    class DataHolder extends RecyclerView.ViewHolder {
        private UserData data;

        //region Widgets TODO: Update / implement Holder.
        //endregion

        public DataHolder(View itemView) {
            super(itemView);
        }

        void bind(UserData data) {
            this.data = data;
        }

        UserData getItem() {
            return this.data;
        }
    }
}

interface OnNavigationToDetailsListener {
    void onNavigate(UserData user);
}