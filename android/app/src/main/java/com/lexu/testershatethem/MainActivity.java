package com.lexu.testershatethem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lexu.testershatethem.POJO.HttpRequester;
import com.lexu.testershatethem.POJO.UserData;
import com.lexu.testershatethem.POJO.UserInstance;

import java.util.ArrayList;

public class MainActivity extends AbstractActivity implements OnNavigationToDetailsListener, HttpRequester.OnNetworkListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String USER_DATA = "data";
    public static final String RANKINGS_USERS_DATA = "rankings";

    private ViewPager viewPager = null;
    private DrawerLayout mDrawerLayout = null;

    private HttpRequester mRequester = new HttpRequester();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        initMenu();

        this.viewPager = (ViewPager) findViewById(R.id.view_pager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.setOnNavigationListener(MainActivity.this);
        this.viewPager.setAdapter(adapter);
        TabLayout tabs = (TabLayout) findViewById(R.id.submenu);
        tabs.setupWithViewPager(this.viewPager);
    }

    private ArrayList<UserData> users = null;
    private ArrayList<UserData> rankes = null;
    @Override
    protected void onStart() {
        super.onStart();

//        mRequester.users(new HttpRequester.OnNetworkListener() {
//            @Override
//            public void onSuccess(HttpRequester.NetworkPayload payload) {
//                if (payload.getCode() == 200) {
//                    users = (ArrayList<UserData>) payload.getData();
//                    if (viewPager != null) {
//                        ((UsersFragment) ((PagerAdapter) viewPager.getAdapter()).getItem(0)).setData(users);
//                    }
//
//                    MainActivity.this.runOnUiThread(MainActivity.this::getSecondBatch);
//                }
//            }
//
//            @Override
//            public void onFailure(HttpRequester.NetworkPayload payload) {
//
//            }
//        });
    }

    private void getSecondBatch() {
        new Handler().postDelayed(() -> mRequester.getRankings(new HttpRequester.OnNetworkListener() {
            @Override
            public void onSuccess(HttpRequester.NetworkPayload payload) {
                if(payload.getCode() == 200) {
                    rankes = (ArrayList<UserData>) payload.getData();
                    if (viewPager != null) {
                        ((RankingFragment) ((PagerAdapter) viewPager.getAdapter()).getItem(1)).setData(rankes);
                    }
                }
            }

            @Override
            public void onFailure(HttpRequester.NetworkPayload payload) {

            }
        }), 10000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        NavigationView nav = (NavigationView) mDrawerLayout.findViewById(R.id.navigation_container);

        menu = nav.getMenu();

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initMenu() {
        NavigationView nav = (NavigationView) mDrawerLayout.findViewById(R.id.navigation_container);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawer(Gravity.START);

                switch (item.getItemId()) {
                    case R.id.home:
                        navigateHome();
                        break;
                    case R.id.account:
                        navigateAccount();
                        break;
                    case R.id.history:
                        navigateHistory();
                        break;
                    case R.id.users:
                        navigateUsers();
                        break;
                    case R.id.ranking:
                        navigateRanking();
                        break;
                    case R.id.logout:
                        UserInstance.getInstance().logout();
                        onBackPressed();
                        break;
                }

                return true;
            }
        });
    }

    @Override
    protected void navigateRanking() {
        if(this.viewPager.getCurrentItem() != 1) {
            this.viewPager.setCurrentItem(1);
        }
    }

    @Override
    protected void navigateUsers() {
        if(this.viewPager.getCurrentItem() != 0) {
            this.viewPager.setCurrentItem(0);
        }
    }

    @Override
    protected void navigateHistory() {
        mRequester.getTransactions(new HttpRequester.OnNetworkListener() {
            @Override
            public void onSuccess(HttpRequester.NetworkPayload payload) {
                MainActivity.this.runOnUiThread(() -> {
                    int code = payload.getCode();
                    if(code != 200) {
                        String msg = payload.getMessage();
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                        return;
                    }

                    Intent navigate = new Intent(MainActivity.this, TransactionHistoryActivity.class);
                    startActivity(navigate);
                });
            }

            @Override
            public void onFailure(HttpRequester.NetworkPayload payload) {
                Log.e(TAG, "onFailure: " + payload.getMessage() + " code: " + payload.getCode() );
            }
        });
    }

    @Override
    protected void navigateAccount() {
        mRequester.currentProfile(new HttpRequester.OnNetworkListener() {
            @Override
            public void onSuccess(HttpRequester.NetworkPayload payload) {
                MainActivity.this.runOnUiThread(() -> {
                    int code = payload.getCode();
                    if(code != 200) {
                        String msg = payload.getMessage();
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                        return;
                    }

                    Intent navigate = new Intent(MainActivity.this, AccountActivity.class);
                    startActivity(navigate);
                });
            }

            @Override
            public void onFailure(HttpRequester.NetworkPayload payload) {
                Log.e(TAG, "onFailure: " + payload.getMessage() + " code: " + payload.getCode() );
            }
        });
    }

    @Override
    protected void navigateHome() {
        //Auto-generated method stub.
    }

    @Override
    public void onNavigate(UserData user) {
        mRequester.userProfile(user.getId(), MainActivity.this);
    }

    @Override
    public void onSuccess(HttpRequester.NetworkPayload payload) {
        MainActivity.this.runOnUiThread(() -> {
            int code = payload.getCode();
            if (code != 200) {
                String msg = payload.getMessage();
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                return;
            }

            navigateToDetails((UserData) payload.getData());
        });
    }

    @Override
    public void onFailure(HttpRequester.NetworkPayload payload) {
        Log.e(TAG, "onFailure: " + payload.getMessage() + " code: " + payload.getCode() );
    }

    private void navigateToDetails(UserData user) {
        Intent navigate = new Intent(MainActivity.this, DetailsActivity.class);
        navigate.putExtra(MainActivity.USER_DATA, user);
        startActivity(navigate);
    }
}

class PagerAdapter extends FragmentPagerAdapter implements OnNavigationToDetailsListener {
    private ArrayList<UserData> mData;

    PagerAdapter(FragmentManager fm) {
        super(fm);
    }

//    void setData(
//            ArrayList<UserData> users, ArrayList<UserData> ranking
//    ) {
//        this.users = users;
//        this.ranking = ranking;
//    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Users";

            case 1:
                return "Ranking";
        }
        return "";
    }

    @Override
    public int getCount() {
        return 2;
    }

    private ArrayList<Fragment> frags = new ArrayList<Fragment>();

    @Override
    public Fragment getItem(int position) {
        if(frags.size() > position) {
            return frags.get(position);
        }

        Fragment f = null;
        switch (position) {
            case 0:
                f = new UsersFragment();
                break;
            case 1:
                f = new RankingFragment();
                break;
        }

        frags.add(f);
        return f;
    }

    private OnNavigationToDetailsListener mOnNavigationToDetailsListener = null;

    void setOnNavigationListener(OnNavigationToDetailsListener onNavigationListener) {
         this.mOnNavigationToDetailsListener = onNavigationListener;
    }

    @Override
    public void onNavigate(UserData user) {
        mOnNavigationToDetailsListener.onNavigate(user);
    }
}