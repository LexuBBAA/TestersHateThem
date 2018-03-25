package com.lexu.testershatethem;

import android.os.Bundle;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.lexu.testershatethem.POJO.UserData;
import com.lexu.testershatethem.POJO.UserInstance;

import java.util.ArrayList;

public class MainActivity extends AbstractActivity {

    private ViewPager viewPager = null;
    private DrawerLayout mDrawerLayout = null;

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
        TabLayout tabs = (TabLayout) findViewById(R.id.submenu);
        tabs.setupWithViewPager(this.viewPager);
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
        //TODO: Navigate to HistoryActivity
    }

    @Override
    protected void navigateAccount() {
        //TODO: Navigate to AccountActivity
    }

    @Override
    protected void navigateHome() {
        //Auto-generated method stub.
    }
}

class PagerAdapter extends FragmentPagerAdapter {
    private ArrayList<UserData> users = null;
    private ArrayList<UserData> ranking = null;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    void setData(
            ArrayList<UserData> users, ArrayList<UserData> ranking
    ) {
        this.users = users;
        this.ranking = ranking;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = new AbstractActivity.UsersFragment();
        Bundle args = new Bundle();
        args.putInt(AbstractActivity.UsersFragment.FRAGMENT_TYPE, position);
        switch (position) {
            case 0:
                args.putSerializable(AbstractActivity.UsersFragment.DATA_KEY, this.users);
                break;
            default:
                args.putSerializable(AbstractActivity.UsersFragment.DATA_KEY, this.ranking);
        }

        f.setArguments(args);
        return f;
    }
}