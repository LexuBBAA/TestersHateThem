package com.lexu.testershatethem;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AbstractActivity {

    private ViewPager viewPager = null;

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
