package com.lexu.testershatethem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lexu.testershatethem.POJO.HttpRequester;
import com.lexu.testershatethem.POJO.UserData;
import com.lexu.testershatethem.POJO.UserInstance;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, HttpRequester.OnNetworkListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    static final String USER_EMAIL = "email";
    static final int REGISTER_COMPLETE = 200;

    private AppCompatEditText emailInput = null;
    private AppCompatEditText passwordInput = null;
    private AppCompatButton loginButton = null;
    private TextView registerLink = null;

    private HttpRequester mRequestManager = new HttpRequester();
    private ArrayList<UserData> users = null;
    private ArrayList<UserData> ranked = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle("Login");
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        this.emailInput = (AppCompatEditText) findViewById(R.id.email_input_field);
        this.passwordInput = (AppCompatEditText) findViewById(R.id.password_input_field);
        this.loginButton = (AppCompatButton) findViewById(R.id.login_button);
        this.registerLink = (TextView) findViewById(R.id.register_link);

        this.loginButton.setOnClickListener(this);
        this.registerLink.setOnClickListener(this);

        View invisibleView = (View) findViewById(R.id.invisible_view);
        invisibleView.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == AppCompatActivity.RESULT_OK && resultCode == REGISTER_COMPLETE) {
            String email = data.getStringExtra(USER_EMAIL);
            this.emailInput.setText(email != null ? email: "");
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(final View v) {
        v.setClickable(false);
        v.setFocusable(false);

        switch (v.getId()) {
            case R.id.login_button:
                if(UserInstance.getInstance().getSessionId() != null) {
                    postInToast("A session is already active");
                    v.setClickable(true);
                    v.setFocusable(true);
                    return;
                }

                try {
                    mRequestManager.login(
                            this.emailInput.getText().toString(),
                            this.passwordInput.getText().toString(),
                            new HttpRequester.OnNetworkListener() {
                                @Override
                                public void onSuccess(final HttpRequester.NetworkPayload payload) {
                                    Log.e(TAG, "onSuccess: " + payload.toString());
                                    LoginActivity.this.runOnUiThread(() -> {
                                        int code = payload.getCode();
                                        if(code != 200) {
                                            String msg = payload.getMessage();
                                            postInToast(msg);
                                            return;
                                        }

                                        Pair<String, String> pair = (Pair<String, String>) payload.getData();
                                        String token = pair.first;
                                        String type = pair.second;
                                        UserInstance.getInstance().setToken(token, type, new UserInstance.OnUserUpdateListener() {
                                            @Override
                                            public void onSuccess(ResponseCode code) {
                                                LoginActivity.this.runOnUiThread(() -> {
                                                    LoginActivity.this.login();
                                                    LoginActivity.this.finish();
                                                });
                                            }

                                            @Override
                                            public void onFail(ResponseCode code, String message) {
                                                LoginActivity.this.runOnUiThread(() -> {
                                                    v.setClickable(true);
                                                    v.setFocusable(true);
                                                });
                                            }
                                        });
                                    });
                                }

                                @Override
                                public void onFailure(final HttpRequester.NetworkPayload payload) {
                                    Log.e(TAG, "onFailure: " + payload.toString());
                                    LoginActivity.this.runOnUiThread(() -> {
                                        String message = payload.getMessage();
                                        postInToast(message);
                                        v.setClickable(true);
                                        v.setFocusable(true);
                                    });
                                }
                            }
                    );
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    v.setClickable(true);
                    v.setFocusable(true);
                }
                break;

            case R.id.register_link:
                goToRegister();
                break;

            case R.id.invisible_view:
                this.emailInput.setText("test@test.com");
                this.passwordInput.setText("test");
                v.setClickable(true);
                v.setFocusable(true);
                break;

            default:
                Log.e(TAG, "onClick: Unknown view clicked: v.getId() = " + v.getId() );
        }
    }

    private void getUserData() {
//        mRequestManager.users(new HttpRequester.OnNetworkListener() {
//            @Override
//            public void onSuccess(HttpRequester.NetworkPayload payload) {
//                int code = payload.getCode();
//                if(code != 200) {
//                    String msg = payload.getMessage();
//                    postInToast(msg);
//                    return;
//                }
//
//                LoginActivity.this.users = (ArrayList<UserData>) payload.getData();
//                mRequestManager.getRankings(LoginActivity.this);
//            }
//
//            @Override
//            public void onFailure(HttpRequester.NetworkPayload payload) {
//                Log.e(TAG, "onFailure: " + payload.toString());
//                LoginActivity.this.runOnUiThread(() -> {
//                    String message = payload.getMessage();
//                    postInToast(message);
//                });
//            }
//        });
        mRequestManager.users(LoginActivity.this);
    }

    private void login() {
        Intent navigate = new Intent(LoginActivity.this, MainActivity.class);
//        navigate.putExtra(MainActivity.USER_DATA, this.users);
//        navigate.putExtra(MainActivity.RANKINGS_USERS_DATA, this.ranked);

        startActivity(navigate);
        this.loginButton.setClickable(true);
        this.loginButton.setFocusable(true);
    }

    private void goToRegister() {
        Intent navigate = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivityForResult(navigate, AppCompatActivity.RESULT_OK);
    }

    private void postInSnackbar(String msg) {
        Snackbar.make(this.loginButton.getRootView(), msg, Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", v -> {
                    this.loginButton.callOnClick();
                })
                .show();
    }

    private void postInToast(String msg) {
        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSuccess(final HttpRequester.NetworkPayload payload) {
        LoginActivity.this.runOnUiThread(() -> {
        int code = payload.getCode();
        if(code != 200) {
            String msg = payload.getMessage();
            postInToast(msg);
        }
        });
    }

    @Override
    public void onFailure(final HttpRequester.NetworkPayload payload) {
        Log.e(TAG, "onFailure: " + payload.toString());
        LoginActivity.this.runOnUiThread(() -> {
            String message = payload.getMessage();
            postInToast(message);
        });
    }
}
