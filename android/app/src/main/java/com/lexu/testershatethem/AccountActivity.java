package com.lexu.testershatethem;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.lexu.testershatethem.POJO.UserInstance;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("My Account");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        UserInstance instance = UserInstance.getInstance();

        TextView name = (TextView) findViewById(R.id.user_name_field);
        name.setText(instance.getName());
        TextView desc = (TextView) findViewById(R.id.user_description_field);
        desc.setText(instance.getDescription());
        TextView email = (TextView) findViewById(R.id.user_email_field);
        email.setText(instance.getEmail());
        TextView phone = (TextView) findViewById(R.id.user_phone_field);
        phone.setText(instance.getPhone());
        TextView rating = (TextView) findViewById(R.id.user_rating_field);
        rating.setText(instance.getRating());
    }
}
