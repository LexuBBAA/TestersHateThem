package com.lexu.testershatethem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lexu.testershatethem.POJO.UserData;

public class DetailsActivity extends AppCompatActivity {

    private UserData mUserData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mUserData = (UserData) getIntent().getSerializableExtra(MainActivity.USER_DATA);

        //TODO
    }
}
