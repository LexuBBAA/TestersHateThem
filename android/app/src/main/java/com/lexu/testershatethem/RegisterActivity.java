package com.lexu.testershatethem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lexu.testershatethem.POJO.City;
import com.lexu.testershatethem.POJO.Country;
import com.lexu.testershatethem.POJO.HttpRequester;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private AppCompatEditText emailInput = null;
    private AppCompatEditText passwordInput = null;
    private AppCompatEditText confirmPasswordInput = null;
    private AppCompatEditText nameInput = null;
    private AppCompatEditText descriptionInput = null;
    private AppCompatEditText phoneInput = null;
    private AppCompatEditText addressInput = null;
    private RadioGroup typeInput = null;
    private AppCompatSpinner countrySpinner = null;
    private AppCompatSpinner citySpinner = null;

    private AppCompatButton registerButton = null;
    private HttpRequester mRequester = new HttpRequester();
    private ArrayList<Country> mCountries = null;
    private HashMap<String, ArrayList<City>> mCities = new HashMap<String, ArrayList<City>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Register");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        this.emailInput = (AppCompatEditText) findViewById(R.id.email_input_field);
        this.passwordInput = (AppCompatEditText) findViewById(R.id.password_input_field);
        this.confirmPasswordInput = (AppCompatEditText) findViewById(R.id.confirm_password_input_field);
        this.nameInput = (AppCompatEditText) findViewById(R.id.name_input_field);
        this.descriptionInput = (AppCompatEditText) findViewById(R.id.description_input_field);
        this.phoneInput = (AppCompatEditText) findViewById(R.id.phone_input_field);
        this.addressInput = (AppCompatEditText) findViewById(R.id.address_input_field);
        this.typeInput = (RadioGroup) findViewById(R.id.type_radio_group);
        this.countrySpinner = (AppCompatSpinner) findViewById(R.id.country_input_field);
        this.citySpinner = (AppCompatSpinner) findViewById(R.id.city_input_field);
        this.registerButton = (AppCompatButton) findViewById(R.id.register_button);

        setupUI();

        this.registerButton.setClickable(false);
        this.registerButton.setFocusable(false);

        getCountries();
    }

    private void setupUI() {
        this.typeInput.check(R.id.producer_type_radio_btn);
        this.registerButton.setOnClickListener(RegisterActivity.this);
    }

    @Override
    public void onClick(View v) {
        v.setClickable(false);
        v.setFocusable(false);

        switch (v.getId()) {
            case R.id.producer_type_radio_btn:
                this.typeInput.check(R.id.producer_type_radio_btn);
                enableView(v);
                break;

            case R.id.consumer_type_radio_btn:
                this.typeInput.check(R.id.consumer_type_radio_btn);
                enableView(v);
                break;

            case R.id.register_button:
                if (validateFields()) {
                    sendRegisterRequest();
                } else {
                    enableView(v);
                }
                break;

            default:
                Country selectedCountry = (Country) this.countrySpinner.getSelectedItem();
                getCities(selectedCountry.getId());
                enableView(v);
                ((View) v.getParent()).setClickable(false);
                ((View) v.getParent()).setFocusable(false);

        }
    }

    private void getCities(String countryName) {
        final String[] countryId = new String[1];
        for (Country c : mCountries) {
            if (c.getName().contentEquals(countryName)) {
                countryId[0] = c.getId();
                break;
            }
        }

        if (countryId[0].isEmpty()) {
            return;
        } else if(mCities.containsKey(countryId[0])) {
            ArrayList<City> cities = mCities.get(countryId[0]);
            String[] citiesArr = new String[cities.size()];
            for(int i = 0; i < cities.size(); i++) {
                citiesArr[i] = cities.get(i).getName();
            }
            ArrayAdapter<String> citySpinAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, citiesArr);
            citySpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            citySpinner.setAdapter(citySpinAdapter);
            return;
        }

        mRequester.cities(countryId[0], new HttpRequester.OnNetworkListener() {
            @Override
            public void onSuccess(HttpRequester.NetworkPayload payload) {
                RegisterActivity.this.runOnUiThread(() -> {
                    int code = payload.getCode();
                    if (code != 200) {
                        String msg = payload.getMessage();
                        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
                        RegisterActivity.this.enableView(registerButton);
                        return;
                    }

                    Log.e(TAG, "onSuccess: " + ((ArrayList<City>) payload.getData()).get(0).toString());

                    ArrayList<City> cities = (ArrayList<City>) payload.getData();
                    mCities.put(countryId[0], cities);
                    String[] citiesArr = new String[cities.size()];
                    for(int i = 0; i < cities.size(); i++) {
                        citiesArr[i] = cities.get(i).getName();
                    }
                    ArrayAdapter<String> citySpinAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, citiesArr);
                    citySpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(citySpinAdapter);
                });
            }

            @Override
            public void onFailure(HttpRequester.NetworkPayload payload) {
                final String msg = payload.getMessage();

                Log.e(TAG, "onFailure: " + msg);
            }
        });
    }

    private void getCountries() {
        mRequester.countries(new HttpRequester.OnNetworkListener() {
            @Override
            public void onSuccess(final HttpRequester.NetworkPayload payload) {
                RegisterActivity.this.runOnUiThread(() -> {
                    int code = payload.getCode();
                    if (code != 200) {
                        String msg = payload.getMessage();
                        Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
                        RegisterActivity.this.enableView(registerButton);
                        return;
                    }

                    Log.e(TAG, "onSuccess: " + ((ArrayList<Country>) payload.getData()).get(0).toString());
                    mCountries = (ArrayList<Country>) payload.getData();
                    String[] countries = new String[mCountries.size()];
                    for (int i = 0; i < mCountries.size(); i++) {
                        countries[i] = mCountries.get(i).getName();
                    }

                    final ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, countries);
                    countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String c = countryAdapter.getItem(position);
                            if (position != 0) {
                                getCities(c);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    countrySpinner.setAdapter(countryAdapter);
                    setupUI();
                });
            }

            @Override
            public void onFailure(HttpRequester.NetworkPayload payload) {
                final String msg = payload.getMessage();

                Log.e(TAG, "onFailure: " + msg);
            }
        });
    }

    private void sendRegisterRequest() {
        String email = this.emailInput.getText().toString();
        String pass = this.passwordInput.getText().toString();
        String name = this.nameInput.getText().toString();
        String desc = this.descriptionInput.getText().toString();
        String phone = this.phoneInput.getText().toString();
        String address = this.addressInput.getText().toString();
        String type = this.typeInput.getCheckedRadioButtonId() == R.id.producer_type_radio_btn ? "provider" : "consumer";
        String countryId = "";
        for(Country c: mCountries) {
            if(c.getName().contentEquals(this.countrySpinner.getSelectedItem().toString())) {
                countryId = c.getId();
                break;
            }
        }

        if(countryId.isEmpty()) {
            ((TextView) this.countrySpinner.getSelectedItem()).setError("Please select a country");
            return;
        }

        String cityId = "";
        for(City c: mCities.get(countryId)) {
            if(c.getName().contentEquals(this.citySpinner.getSelectedItem().toString())) {
                cityId = c.getId();
                break;
            }
        }

        if(cityId.isEmpty()) {
            ((TextView) this.citySpinner.getSelectedItem()).setError("Please select a city");
            return;
        }
        try {
            mRequester.register(email, pass, name, desc, address, phone, type, countryId, cityId, new HttpRequester.OnNetworkListener() {
                @Override
                public void onSuccess(final HttpRequester.NetworkPayload payload) {
                    RegisterActivity.this.runOnUiThread(() -> {
                        int code = payload.getCode();
                        if (code != 200) {
                            String msg = payload.getMessage();
                            Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
                            RegisterActivity.this.enableView(registerButton);
                            return;
                        }

                        navigateToLogin(AppCompatActivity.RESULT_OK);
                    });
                }

                @Override
                public void onFailure(HttpRequester.NetworkPayload payload) {
                    final String msg = payload.getMessage();

                    Log.e(TAG, "onFailure: " + msg);
                    RegisterActivity.this.runOnUiThread(() -> {
                        RegisterActivity.this.enableView(registerButton);
                    });
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void navigateToLogin(int status) {
        switch (status) {
            case AppCompatActivity.RESULT_OK:
                Intent data = getIntent().putExtra(LoginActivity.USER_EMAIL, this.emailInput.getText().toString());
                setResult(LoginActivity.REGISTER_COMPLETE, data);
                finish();
                break;

            case AppCompatActivity.RESULT_CANCELED:
                finish();
                break;
        }
    }

    private boolean validateFields() {
        boolean result = true;

        String email = this.emailInput.getText().toString();
        if (email.isEmpty() || !CommonUtils.validEmail(email)) {
            if (email.isEmpty()) {
                this.emailInput.setError("Field cannot be empty");
            } else {
                this.emailInput.setError("Invalid email");
            }
            result = false;
        }

        String password = this.passwordInput.getText().toString();
        String confirmPassword = this.confirmPasswordInput.getText().toString();
        if (password.isEmpty() || !CommonUtils.validPassword(password, confirmPassword)) {
            if (password.isEmpty()) {
                this.passwordInput.setError("Field cannot be empty");
            } else {
                this.passwordInput.setError("Passwords do not match");
            }
            result = false;
        }

        String name = this.nameInput.getText().toString();
        if (name.isEmpty()) {
            this.nameInput.setError("Field cannot be empty");
            result = false;
        }

        String address = this.addressInput.getText().toString();
        if (address.isEmpty()) {
            this.addressInput.setError("Field cannot be empty");
            result = false;
        }

        String country = this.countrySpinner.getSelectedItem().toString();
        if (country.toLowerCase().contentEquals(this.countrySpinner.getChildAt(0).toString().toLowerCase())) {
            ((TextView) this.countrySpinner.getSelectedItem()).setError("Please select a country");
            result = false;
        }

        String city = this.citySpinner.getSelectedItem().toString();
        if (city.toLowerCase().contentEquals(this.citySpinner.getChildAt(0).toString().toLowerCase())) {
            ((TextView) this.citySpinner.getSelectedItem()).setError("Please select a city");
            result = false;
        }

        return result;
    }

    private void enableView(View v) {
        v.setClickable(true);
        v.setFocusable(true);
    }
}