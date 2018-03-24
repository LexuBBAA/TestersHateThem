package com.lexu.testershatethem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lexu.testershatethem.POJO.HttpRequester;

import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private AppCompatEditText emailInput = null;
    private AppCompatEditText passwordInput = null;
    private AppCompatButton loginButton = null;
    private TextView registerLink = null;

    private HttpRequester mRequestManager = new HttpRequester();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.emailInput = (AppCompatEditText) findViewById(R.id.email_input_field);
        this.passwordInput = (AppCompatEditText) findViewById(R.id.password_input_field);
        this.loginButton = (AppCompatButton) findViewById(R.id.login_button);
        this.registerLink = (TextView) findViewById(R.id.register_link);

        this.loginButton.setOnClickListener(this);
        this.registerLink.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        v.setClickable(false);
        v.setFocusable(false);

        switch (v.getId()) {
            case R.id.login_button:
                try {
                    mRequestManager.login(
                            this.emailInput.getText().toString(),
                            this.passwordInput.getText().toString(),
                            new HttpRequester.OnNetworkListener() {
                                @Override
                                public void onSuccess(HttpRequester.NetworkPayload payload) {
                                    Log.e(TAG, "onSuccess: " + payload.toString());
                                    LoginActivity.this.runOnUiThread(() -> {
                                        v.setClickable(true);
                                        v.setFocusable(true);
                                    });
                                }

                                @Override
                                public void onFailure(HttpRequester.NetworkPayload payload) {
                                    Log.e(TAG, "onFailure: " + payload.toString());
                                    LoginActivity.this.runOnUiThread(() -> {
                                        v.setClickable(true);
                                        v.setFocusable(true);
                                    });
                                }
                            }
                    );
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.register_link:
                break;

            default:
                Log.e(TAG, "onClick: Unknown view clicked: v.getId() = " + v.getId() );
        }
    }
}
