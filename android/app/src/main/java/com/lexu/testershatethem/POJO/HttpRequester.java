package com.lexu.testershatethem.POJO;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by lexu on 24.03.2018.
 */

public class HttpRequester {

    private static final String TAG = HttpRequester.class.getSimpleName();

    public static class NetworkPayload<T> {
        private T data = null;
        private String message = null;
        private int code = -1;

        private NetworkPayload() {}

        private void setData(T data) {
            this.data = data;
        }

        private void setMessage(String message) {
            this.message = message;
        }

        private void setCode(int code) {
            this.code = code;
        }

        public T getData() {
            return data;
        }

        public String getMessage() {
            return message;
        }

        public int getCode() {
            return code;
        }

        @Override
        public String toString() {
            return (data != null ? data.toString(): "null") + '\n' + message + '\n' + code + '\n';
        }

        public static class Builder<T> {
            private T data = null;
            private String message = null;
            private int code = -1;

            public Builder getBuilder() {
                return new Builder();
            }

            protected Builder setData(T data) {
                this.data = data;
                return this;
            }

            Builder setMessage(String message) {
                this.message = message;
                return this;
            }

            Builder setCode(int code) {
                this.code = code;
                return this;
            }

            NetworkPayload<T> build() {
                NetworkPayload<T> result = new NetworkPayload<T>();
                result.setData(this.data);
                result.setCode(this.code);
                result.setMessage(this.message);

                return result;
            }
        }
    }

    abstract static class NetworkCallbackFactory {
        static Callback loginCallback(final OnNetworkListener listener) {
            return new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, "onFailure: " + e.getLocalizedMessage());

                    NetworkPayload payload = new NetworkPayload.Builder<IOException>()
                            .setData(e)
                            .setMessage(e.getLocalizedMessage())
                            .setCode(100)
                            .build();
                    listener.onFailure(payload);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    NetworkPayload payload = null;

                    ResponseBody body = response.body();
                    if(body == null) {
                        Log.e(TAG, "onResponse: Response body is null");

                        payload = new NetworkPayload.Builder<String>()
                                .setData("Body is null")
                                .setMessage("Response body is null")
                                .setCode(101)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    String data = body.string();
                    JSONObject jsonData = null;
                    String token = null;
                    String type = null;
                    boolean status = true;
                    try {
                        jsonData = new JSONObject(data);
                        status = jsonData.getBoolean(NetworkRequests.NetworkUtils.REQUEST_STATUS);
                        if(status) {
                            token = jsonData.getString(NetworkRequests.NetworkUtils.TOKEN);
                            type = jsonData.getString(NetworkRequests.NetworkUtils.REGISTER_TYPE);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                        payload = new NetworkPayload.Builder<JSONException>()
                                .setData(e)
                                .setMessage(e.getLocalizedMessage())
                                .setCode(104)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    if(status) {
                        payload = new NetworkPayload.Builder<Pair<String, String>>()
                                .setData(new Pair<String, String>(token, type))
                                .setCode(200)
                                .setMessage("SUCCESS")
                                .build();
                        listener.onSuccess(payload);
                    } else {
                        payload = new NetworkPayload.Builder<Void>()
                                .setData(null)
                                .setCode(201)
                                .setMessage("Invalid credentials")
                                .build();
                        listener.onFailure(payload);
                    }
                }
            };
        }

        static Callback registerCallback(final OnNetworkListener listener) {
            return new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, "onFailure: " + e.getLocalizedMessage());

                    NetworkPayload payload = new NetworkPayload.Builder<IOException>()
                            .setData(e)
                            .setMessage(e.getLocalizedMessage())
                            .setCode(100)
                            .build();
                    listener.onFailure(payload);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    NetworkPayload payload = null;

                    ResponseBody body = response.body();
                    if(body == null) {
                        Log.e(TAG, "onResponse: Response body is null");

                        payload = new NetworkPayload.Builder<String>()
                                .setData("Body is null")
                                .setMessage("Response body is null")
                                .setCode(101)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    String data = body.string();
                    JSONObject jsonData = null;
                    String token = null;
                    boolean status = true;
                    String msg = "";
                    try {
                        jsonData = new JSONObject(data);
                        status = jsonData.getBoolean(NetworkRequests.NetworkUtils.REQUEST_STATUS);
                        msg = jsonData.getString(NetworkRequests.NetworkUtils.REQUEST_MESSAGE);
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                        payload = new NetworkPayload.Builder<JSONException>()
                                .setData(e)
                                .setMessage(e.getLocalizedMessage())
                                .setCode(104)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    if(status) {
                        payload = new NetworkPayload.Builder<String>()
                                .setData(msg)
                                .setCode(200)
                                .setMessage("SUCCESS")
                                .build();
                        listener.onSuccess(payload);
                    } else {
                        payload = new NetworkPayload.Builder<Void>()
                                .setData(null)
                                .setCode(201)
                                .setMessage(msg)
                                .build();
                        listener.onFailure(payload);
                    }
                }
            };
        }

        static Callback countriesCallback(final OnNetworkListener listener) {
            return new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, "onFailure: " + e.getLocalizedMessage());

                    NetworkPayload payload = new NetworkPayload.Builder<IOException>()
                            .setData(e)
                            .setMessage(e.getLocalizedMessage())
                            .setCode(100)
                            .build();
                    listener.onFailure(payload);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    NetworkPayload payload = null;

                    ResponseBody body = response.body();
                    if(body == null) {
                        Log.e(TAG, "onResponse: Response body is null");

                        payload = new NetworkPayload.Builder<String>()
                                .setData("Body is null")
                                .setMessage("Response body is null")
                                .setCode(101)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    String data = body.string();
                    JSONObject jsonData = null;
                    boolean status = true;
                    try {
                        jsonData = new JSONObject(data);
                        status = jsonData.getBoolean(NetworkRequests.NetworkUtils.REQUEST_STATUS);
                        if(!status) {
                            String msg = jsonData.getString(NetworkRequests.NetworkUtils.REQUEST_MESSAGE);
                            payload = new NetworkPayload.Builder<Void>()
                                    .setData(null)
                                    .setMessage(msg)
                                    .setCode(101)
                                    .build();

                            listener.onFailure(payload);
                            return;
                        }

                        JSONArray jsonArray = jsonData.getJSONArray(NetworkRequests.NetworkUtils.REQUEST_DATA);
                        payload = new NetworkPayload.Builder<ArrayList<Country>>()
                                .setData(Country.CountryParser.parseCountries(jsonArray))
                                .setCode(200)
                                .setMessage("SUCCESS")
                                .build();
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                        payload = new NetworkPayload.Builder<JSONException>()
                                .setData(e)
                                .setMessage(e.getLocalizedMessage())
                                .setCode(104)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    listener.onSuccess(payload);
                }
            };
        }

        static Callback citiesCallback(final OnNetworkListener listener) {
            return new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, "onFailure: " + e.getLocalizedMessage());

                    NetworkPayload payload = new NetworkPayload.Builder<IOException>()
                            .setData(e)
                            .setMessage(e.getLocalizedMessage())
                            .setCode(100)
                            .build();
                    listener.onFailure(payload);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    NetworkPayload payload = null;

                    ResponseBody body = response.body();
                    if(body == null) {
                        Log.e(TAG, "onResponse: Response body is null");

                        payload = new NetworkPayload.Builder<String>()
                                .setData("Body is null")
                                .setMessage("Response body is null")
                                .setCode(101)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    String data = body.string();
                    JSONObject jsonData = null;
                    boolean status = true;
                    try {
                        jsonData = new JSONObject(data);
                        status = jsonData.getBoolean(NetworkRequests.NetworkUtils.REQUEST_STATUS);
                        if(!status) {
                            String msg = jsonData.getString(NetworkRequests.NetworkUtils.REQUEST_MESSAGE);
                            payload = new NetworkPayload.Builder<Void>()
                                    .setData(null)
                                    .setMessage(msg)
                                    .setCode(101)
                                    .build();

                            listener.onFailure(payload);
                            return;
                        }

                        JSONArray jsonArray = jsonData.getJSONArray(NetworkRequests.NetworkUtils.REQUEST_DATA);
                        payload = new NetworkPayload.Builder<ArrayList<City>>()
                                .setData(City.CityParser.parseCities(jsonArray))
                                .setCode(200)
                                .setMessage("SUCCESS")
                                .build();
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                        payload = new NetworkPayload.Builder<JSONException>()
                                .setData(e)
                                .setMessage(e.getLocalizedMessage())
                                .setCode(104)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    listener.onSuccess(payload);
                }
            };
        }

        static Callback listingCallback(OnNetworkListener listener) {
            return new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, "onFailure: " + e.getLocalizedMessage());

                    NetworkPayload payload = new NetworkPayload.Builder<IOException>()
                            .setData(e)
                            .setMessage(e.getLocalizedMessage())
                            .setCode(100)
                            .build();
                    listener.onFailure(payload);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    NetworkPayload payload = null;

                    ResponseBody body = response.body();
                    if(body == null) {
                        Log.e(TAG, "onResponse: Response body is null");

                        payload = new NetworkPayload.Builder<String>()
                                .setData("Body is null")
                                .setMessage("Response body is null")
                                .setCode(101)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    String data = body.string();
                    JSONObject jsonData = null;
                    boolean status = true;
                    try {
                        jsonData = new JSONObject(data);
                        status = jsonData.getBoolean(NetworkRequests.NetworkUtils.REQUEST_STATUS);
                        if(!status) {
                            String msg = jsonData.getString(NetworkRequests.NetworkUtils.REQUEST_MESSAGE);
                            payload = new NetworkPayload.Builder<Void>()
                                    .setData(null)
                                    .setMessage(msg)
                                    .setCode(101)
                                    .build();

                            listener.onFailure(payload);
                            return;
                        }

                        JSONArray jsonArray = new JSONArray(jsonData.getString(NetworkRequests.NetworkUtils.REQUEST_DATA));
//                        JSONArray jsonArray = new JSONArray(data);
                        payload = new NetworkPayload.Builder<ArrayList<UserData>>()
                                .setData(UserData.UserParser.parseUser(jsonArray))
                                .setCode(200)
                                .setMessage("SUCCESS")
                                .build();
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                        payload = new NetworkPayload.Builder<JSONException>()
                                .setData(e)
                                .setMessage(e.getLocalizedMessage())
                                .setCode(104)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    Log.e(TAG, "onResponse: SUCCESS - LISTING");
                    listener.onSuccess(payload);
                }
            };
        }

        static Callback currentProfileCallback(OnNetworkListener listener) {
            return new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, "onFailure: " + e.getLocalizedMessage());

                    NetworkPayload payload = new NetworkPayload.Builder<IOException>()
                            .setData(e)
                            .setMessage(e.getLocalizedMessage())
                            .setCode(100)
                            .build();
                    listener.onFailure(payload);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    NetworkPayload payload = null;

                    ResponseBody body = response.body();
                    if(body == null) {
                        Log.e(TAG, "onResponse: Response body is null");

                        payload = new NetworkPayload.Builder<String>()
                                .setData("Body is null")
                                .setMessage("Response body is null")
                                .setCode(101)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    String data = body.string();
                    JSONObject jsonData = null;
                    boolean status = true;
                    try {
                        jsonData = new JSONObject(data);
                        status = jsonData.getBoolean(NetworkRequests.NetworkUtils.REQUEST_STATUS);
                        if(!status) {
                            String msg = jsonData.getString(NetworkRequests.NetworkUtils.REQUEST_MESSAGE);
                            payload = new NetworkPayload.Builder<Void>()
                                    .setData(null)
                                    .setMessage(msg)
                                    .setCode(101)
                                    .build();

                            listener.onFailure(payload);
                            return;
                        }

                        JSONObject json = jsonData.getJSONObject(NetworkRequests.NetworkUtils.REQUEST_DATA);
                        String email = json.getString(UserData.UserUtils.USER_EMAIL);
                        String name = json.getString(UserData.UserUtils.USER_NAME);
                        String phone = json.getString(UserData.UserUtils.USER_PHONE);
                        String desc = json.getString(UserData.UserUtils.USER_DESCRIPTION);
                        int rating = json.getInt(UserData.UserUtils.USER_RATING);
                        UserInstance.getInstance().setData(email, name, desc, phone, rating);
                        payload = new NetworkPayload.Builder<Void>()
                                .setData(null)
                                .setCode(200)
                                .setMessage("SUCCESS")
                                .build();
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                        payload = new NetworkPayload.Builder<JSONException>()
                                .setData(e)
                                .setMessage(e.getLocalizedMessage())
                                .setCode(104)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    listener.onSuccess(payload);
                }
            };
        }

        static Callback userProfileCallback(OnNetworkListener listener) {
            return new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, "onFailure: " + e.getLocalizedMessage());

                    NetworkPayload payload = new NetworkPayload.Builder<IOException>()
                            .setData(e)
                            .setMessage(e.getLocalizedMessage())
                            .setCode(100)
                            .build();
                    listener.onFailure(payload);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    NetworkPayload payload = null;

                    ResponseBody body = response.body();
                    if(body == null) {
                        Log.e(TAG, "onResponse: Response body is null");

                        payload = new NetworkPayload.Builder<String>()
                                .setData("Body is null")
                                .setMessage("Response body is null")
                                .setCode(101)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    String data = body.string();
                    JSONObject jsonData = null;
                    boolean status = true;
                    try {
                        jsonData = new JSONObject(data);
                        status = jsonData.getBoolean(NetworkRequests.NetworkUtils.REQUEST_STATUS);
                        if(!status) {
                            String msg = jsonData.getString(NetworkRequests.NetworkUtils.REQUEST_MESSAGE);
                            payload = new NetworkPayload.Builder<Void>()
                                    .setData(null)
                                    .setMessage(msg)
                                    .setCode(101)
                                    .build();

                            listener.onFailure(payload);
                            return;
                        }

                        JSONObject json = jsonData.getJSONObject(NetworkRequests.NetworkUtils.REQUEST_DATA);
                        payload = new NetworkPayload.Builder<UserData>()
                                .setData(UserData.UserParser.parseUser(json))
                                .setCode(200)
                                .setMessage("SUCCESS")
                                .build();
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                        payload = new NetworkPayload.Builder<JSONException>()
                                .setData(e)
                                .setMessage(e.getLocalizedMessage())
                                .setCode(104)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    listener.onSuccess(payload);
                }
            };
        }

        static Callback newTransactionCallback(OnNetworkListener listener) {
            return new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, "onFailure: " + e.getLocalizedMessage());

                    NetworkPayload payload = new NetworkPayload.Builder<IOException>()
                            .setData(e)
                            .setMessage(e.getLocalizedMessage())
                            .setCode(100)
                            .build();
                    listener.onFailure(payload);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    NetworkPayload payload = null;

                    ResponseBody body = response.body();
                    if(body == null) {
                        Log.e(TAG, "onResponse: Response body is null");

                        payload = new NetworkPayload.Builder<String>()
                                .setData("Body is null")
                                .setMessage("Response body is null")
                                .setCode(101)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    String data = body.string();
                    JSONObject jsonData = null;
                    boolean status = true;
                    try {
                        jsonData = new JSONObject(data);
                        status = jsonData.getBoolean(NetworkRequests.NetworkUtils.REQUEST_STATUS);
                        if(!status) {
                            String msg = jsonData.getString(NetworkRequests.NetworkUtils.REQUEST_MESSAGE);
                            payload = new NetworkPayload.Builder<Void>()
                                    .setData(null)
                                    .setMessage(msg)
                                    .setCode(101)
                                    .build();

                            listener.onFailure(payload);
                            return;
                        }

                        payload = new NetworkPayload.Builder<String>()
                                .setData("Transaction placed successfully")
                                .setCode(200)
                                .setMessage("SUCCESS")
                                .build();
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                        payload = new NetworkPayload.Builder<JSONException>()
                                .setData(e)
                                .setMessage(e.getLocalizedMessage())
                                .setCode(104)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    listener.onSuccess(payload);
                }
            };
        }

        static Callback acceptTransactionCallback(OnNetworkListener listener) {
            return new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, "onFailure: " + e.getLocalizedMessage());

                    NetworkPayload payload = new NetworkPayload.Builder<IOException>()
                            .setData(e)
                            .setMessage(e.getLocalizedMessage())
                            .setCode(100)
                            .build();
                    listener.onFailure(payload);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    NetworkPayload payload = null;

                    ResponseBody body = response.body();
                    if(body == null) {
                        Log.e(TAG, "onResponse: Response body is null");

                        payload = new NetworkPayload.Builder<String>()
                                .setData("Body is null")
                                .setMessage("Response body is null")
                                .setCode(101)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    String data = body.string();
                    JSONObject jsonData = null;
                    boolean status = true;
                    try {
                        jsonData = new JSONObject(data);
                        status = jsonData.getBoolean(NetworkRequests.NetworkUtils.REQUEST_STATUS);
                        if(!status) {
                            String msg = jsonData.getString(NetworkRequests.NetworkUtils.REQUEST_MESSAGE);
                            payload = new NetworkPayload.Builder<Void>()
                                    .setData(null)
                                    .setMessage(msg)
                                    .setCode(101)
                                    .build();

                            listener.onFailure(payload);
                            return;
                        }

                        payload = new NetworkPayload.Builder<String>()
                                .setData("Transaction accepted")
                                .setCode(200)
                                .setMessage("SUCCESS")
                                .build();
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                        payload = new NetworkPayload.Builder<JSONException>()
                                .setData(e)
                                .setMessage(e.getLocalizedMessage())
                                .setCode(104)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    listener.onSuccess(payload);
                }
            };
        }

        static Callback confirmTransactionCallback(OnNetworkListener listener) {
            return new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, "onFailure: " + e.getLocalizedMessage());

                    NetworkPayload payload = new NetworkPayload.Builder<IOException>()
                            .setData(e)
                            .setMessage(e.getLocalizedMessage())
                            .setCode(100)
                            .build();
                    listener.onFailure(payload);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    NetworkPayload payload = null;

                    ResponseBody body = response.body();
                    if(body == null) {
                        Log.e(TAG, "onResponse: Response body is null");

                        payload = new NetworkPayload.Builder<String>()
                                .setData("Body is null")
                                .setMessage("Response body is null")
                                .setCode(101)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    String data = body.string();
                    JSONObject jsonData = null;
                    boolean status = true;
                    try {
                        jsonData = new JSONObject(data);
                        status = jsonData.getBoolean(NetworkRequests.NetworkUtils.REQUEST_STATUS);
                        if(!status) {
                            String msg = jsonData.getString(NetworkRequests.NetworkUtils.REQUEST_MESSAGE);
                            payload = new NetworkPayload.Builder<Void>()
                                    .setData(null)
                                    .setMessage(msg)
                                    .setCode(101)
                                    .build();

                            listener.onFailure(payload);
                            return;
                        }

                        payload = new NetworkPayload.Builder<String>()
                                .setData("Transaction confirmed")
                                .setCode(200)
                                .setMessage("SUCCESS")
                                .build();
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                        payload = new NetworkPayload.Builder<JSONException>()
                                .setData(e)
                                .setMessage(e.getLocalizedMessage())
                                .setCode(104)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    listener.onSuccess(payload);
                }
            };
        }

        static Callback rateTransactionCallback(OnNetworkListener listener) {
            return new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, "onFailure: " + e.getLocalizedMessage());

                    NetworkPayload payload = new NetworkPayload.Builder<IOException>()
                            .setData(e)
                            .setMessage(e.getLocalizedMessage())
                            .setCode(100)
                            .build();
                    listener.onFailure(payload);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    NetworkPayload payload = null;

                    ResponseBody body = response.body();
                    if(body == null) {
                        Log.e(TAG, "onResponse: Response body is null");

                        payload = new NetworkPayload.Builder<String>()
                                .setData("Body is null")
                                .setMessage("Response body is null")
                                .setCode(101)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    String data = body.string();
                    JSONObject jsonData = null;
                    boolean status = true;
                    try {
                        jsonData = new JSONObject(data);
                        status = jsonData.getBoolean(NetworkRequests.NetworkUtils.REQUEST_STATUS);
                        if(!status) {
                            String msg = jsonData.getString(NetworkRequests.NetworkUtils.REQUEST_MESSAGE);
                            payload = new NetworkPayload.Builder<Void>()
                                    .setData(null)
                                    .setMessage(msg)
                                    .setCode(101)
                                    .build();

                            listener.onFailure(payload);
                            return;
                        }

                        payload = new NetworkPayload.Builder<String>()
                                .setData("Thank you for your feedback")
                                .setCode(200)
                                .setMessage("SUCCESS")
                                .build();
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                        payload = new NetworkPayload.Builder<JSONException>()
                                .setData(e)
                                .setMessage(e.getLocalizedMessage())
                                .setCode(104)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    listener.onSuccess(payload);
                }
            };
        }

        static Callback getTransactions(OnNetworkListener listener) {
            return new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, "onFailure: " + e.getLocalizedMessage());

                    NetworkPayload payload = new NetworkPayload.Builder<IOException>()
                            .setData(e)
                            .setMessage(e.getLocalizedMessage())
                            .setCode(100)
                            .build();
                    listener.onFailure(payload);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    NetworkPayload payload = null;

                    ResponseBody body = response.body();
                    if(body == null) {
                        Log.e(TAG, "onResponse: Response body is null");

                        payload = new NetworkPayload.Builder<String>()
                                .setData("Body is null")
                                .setMessage("Response body is null")
                                .setCode(101)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    String data = body.string();
                    JSONObject jsonData = null;
                    boolean status = true;
                    try {
                        jsonData = new JSONObject(data);
                        status = jsonData.getBoolean(NetworkRequests.NetworkUtils.REQUEST_STATUS);
                        if(!status) {
                            String msg = jsonData.getString(NetworkRequests.NetworkUtils.REQUEST_MESSAGE);
                            payload = new NetworkPayload.Builder<Void>()
                                    .setData(null)
                                    .setMessage(msg)
                                    .setCode(101)
                                    .build();

                            listener.onFailure(payload);
                            return;
                        }

                        JSONArray jsonArray = jsonData.getJSONArray(NetworkRequests.NetworkUtils.REQUEST_DATA);
                        payload = new NetworkPayload.Builder<ArrayList<Transaction>>()
                                .setData(Transaction.TransactionParser.parseTransactions(jsonArray))
                                .setCode(200)
                                .setMessage("SUCCESS")
                                .build();
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                        payload = new NetworkPayload.Builder<JSONException>()
                                .setData(e)
                                .setMessage(e.getLocalizedMessage())
                                .setCode(104)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    listener.onSuccess(payload);
                }
            };
        }

        static Callback rankingsCallback(OnNetworkListener listener) {
            return new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e(TAG, "onFailure: " + e.getLocalizedMessage());

                    NetworkPayload payload = new NetworkPayload.Builder<IOException>()
                            .setData(e)
                            .setMessage(e.getLocalizedMessage())
                            .setCode(100)
                            .build();
                    listener.onFailure(payload);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    NetworkPayload payload = null;

                    ResponseBody body = response.body();
                    if(body == null) {
                        Log.e(TAG, "onResponse: Response body is null");

                        payload = new NetworkPayload.Builder<String>()
                                .setData("Body is null")
                                .setMessage("Response body is null")
                                .setCode(101)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    String data = body.string();
                    JSONObject jsonData = null;
                    boolean status = true;
                    try {
                        jsonData = new JSONObject(data);
                        status = jsonData.getBoolean(NetworkRequests.NetworkUtils.REQUEST_STATUS);
                        if(!status) {
                            String msg = jsonData.getString(NetworkRequests.NetworkUtils.REQUEST_MESSAGE);
                            payload = new NetworkPayload.Builder<Void>()
                                    .setData(null)
                                    .setMessage(msg)
                                    .setCode(101)
                                    .build();

                            listener.onFailure(payload);
                            return;
                        }

                        JSONArray jsonArray = new JSONArray(jsonData.getString(NetworkRequests.NetworkUtils.REQUEST_DATA));
                        payload = new NetworkPayload.Builder<ArrayList<UserData>>()
                                .setData(UserData.UserParser.parseUser(jsonArray))
                                .setCode(200)
                                .setMessage("SUCCESS")
                                .build();
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                        payload = new NetworkPayload.Builder<JSONException>()
                                .setData(e)
                                .setMessage(e.getLocalizedMessage())
                                .setCode(104)
                                .build();
                        listener.onFailure(payload);
                        return;
                    }

                    Log.e(TAG, "onResponse: SUCCESS - RANKING");
                    listener.onSuccess(payload);
                }
            };
        }
    }

    public enum NetworkCalls {
        LOGIN, REGISTER, GET_COUNTRIES, GET_CITIES, GET_MY_PROFILE, GET_USERS_PROFILES, POST_NEW_TRANSACTION, CONFIRM_TRANSACTION, ACCEPT_TRANSACTION, RATE_TRANSACTION, GET_TRANSACTIONS, GET_RANKING, GET_BASE_DATA
    }

    private OkHttpClient mClient = null;

    public interface OnNetworkListener {
        void onSuccess(NetworkPayload payload);
        void onFailure(NetworkPayload payload);
    }

    public HttpRequester() {
        mClient = new OkHttpClient();
    }

    public void login(String email, String password, OnNetworkListener callback) throws NoSuchAlgorithmException {
        Bundle data = new Bundle();
        data.putString(NetworkRequests.NetworkUtils.LOGIN_USERNAME, email);
        data.putString(NetworkRequests.NetworkUtils.LOGIN_PASSWORD, NetworkRequests.NetworkUtils.md5(password));

        Request request = NetworkRequests.buildRequest(data, NetworkCalls.LOGIN);
        if(request == null) {
            NetworkPayload payload = new NetworkPayload.Builder<Void>()
                    .setData(null)
                    .setCode(100)
                    .setMessage("Could not make request")
                    .build();
            callback.onFailure(payload);
            return;
        }

        mClient.newCall(request).enqueue(NetworkCallbackFactory.loginCallback(callback));
    }

    public void register(String email, String password, String name,String description, String address, String phone, String type, String countryId, String cityId, OnNetworkListener callback) throws NoSuchAlgorithmException {
        Bundle data = new Bundle();
        data.putString(NetworkRequests.NetworkUtils.REGISTER_USERNAME, email);
        data.putString(NetworkRequests.NetworkUtils.REGISTER_PASSWORD, NetworkRequests.NetworkUtils.md5(password));
        data.putString(NetworkRequests.NetworkUtils.REGISTER_NAME, name);
        data.putString(NetworkRequests.NetworkUtils.REGISTER_ADDRESS, address);
        data.putString(NetworkRequests.NetworkUtils.REGISTER_PHONE, phone);
        data.putString(NetworkRequests.NetworkUtils.REGISTER_DESCRIPTION, description);
        data.putString(NetworkRequests.NetworkUtils.REGISTER_COUNTRY_ID, countryId);
        data.putString(NetworkRequests.NetworkUtils.REGISTER_CITY_ID, cityId);
        data.putString(NetworkRequests.NetworkUtils.REGISTER_TYPE, type);

        Request request = NetworkRequests.buildRequest(data, NetworkCalls.REGISTER);
        if(request == null) {
            NetworkPayload payload = new NetworkPayload.Builder<Void>()
                    .setData(null)
                    .setCode(100)
                    .setMessage("Could not make request")
                    .build();
            callback.onFailure(payload);
            return;
        }

        mClient.newCall(request).enqueue(NetworkCallbackFactory.registerCallback(callback));
    }

    public void countries(OnNetworkListener callback) {
        Request request = NetworkRequests.buildRequest(new Bundle(), NetworkCalls.GET_COUNTRIES);
        if(request == null) {
            NetworkPayload payload = new NetworkPayload.Builder<Void>()
                    .setData(null)
                    .setCode(100)
                    .setMessage("Could not make request")
                    .build();
            callback.onFailure(payload);
            return;
        }

        mClient.newCall(request).enqueue(NetworkCallbackFactory.countriesCallback(callback));
    }

    public void cities(String countryId, OnNetworkListener callback) {
        Bundle data = new Bundle();
        data.putString(NetworkRequests.NetworkUtils.CITY_COUNTRY_ID, countryId);
        Request request = NetworkRequests.buildRequest(data, NetworkCalls.GET_CITIES);
        if(request == null) {
            NetworkPayload payload = new NetworkPayload.Builder<Void>()
                    .setData(null)
                    .setCode(100)
                    .setMessage("Could not make request")
                    .build();
            callback.onFailure(payload);
            return;
        }

        mClient.newCall(request).enqueue(NetworkCallbackFactory.citiesCallback(callback));
    }

    public void users(OnNetworkListener callback) {
        Request request = NetworkRequests.buildRequest(new Bundle(), NetworkCalls.GET_BASE_DATA);
        if(request == null) {
            NetworkPayload payload = new NetworkPayload.Builder<Void>()
                    .setData(null)
                    .setCode(100)
                    .setMessage("Could not make request")
                    .build();
            callback.onFailure(payload);
            return;
        }

        mClient.newCall(request).enqueue(NetworkCallbackFactory.listingCallback(callback));
    }

    public void currentProfile(OnNetworkListener callback) {
        Request request = NetworkRequests.buildRequest(new Bundle(), NetworkCalls.GET_MY_PROFILE);
        if(request == null) {
            NetworkPayload payload = new NetworkPayload.Builder<Void>()
                    .setData(null)
                    .setCode(100)
                    .setMessage("Could not make request")
                    .build();
            callback.onFailure(payload);
            return;
        }

        mClient.newCall(request).enqueue(NetworkCallbackFactory.currentProfileCallback(callback));
    }

    public void userProfile(String userId, OnNetworkListener callback) {
        Bundle args = new Bundle();
        args.putString(NetworkRequests.NetworkUtils.USER_ID, userId);
        Request request = NetworkRequests.buildRequest(args, NetworkCalls.GET_USERS_PROFILES);
        if(request == null) {
            NetworkPayload payload = new NetworkPayload.Builder<Void>()
                    .setData(null)
                    .setCode(100)
                    .setMessage("Could not make request")
                    .build();
            callback.onFailure(payload);
            return;
        }

        mClient.newCall(request).enqueue(NetworkCallbackFactory.userProfileCallback(callback));
    }

    public void newTransaction(String userId, int quantity, Date date, OnNetworkListener callback) {
        Bundle args = new Bundle();
        args.putString(NetworkRequests.NetworkUtils.TRANSACTION_ID, userId);
        args.putInt(NetworkRequests.NetworkUtils.TRANSACTION_QUANTITY, quantity);
        args.putString(NetworkRequests.NetworkUtils.TRANSACTION_DATE, new SimpleDateFormat("YYYY-mm--DD", Locale.getDefault()).format(date));
        Request request = NetworkRequests.buildRequest(args, NetworkCalls.POST_NEW_TRANSACTION);
        if(request == null) {
            NetworkPayload payload = new NetworkPayload.Builder<Void>()
                    .setData(null)
                    .setCode(100)
                    .setMessage("Could not make request")
                    .build();
            callback.onFailure(payload);
            return;
        }

        mClient.newCall(request).enqueue(NetworkCallbackFactory.newTransactionCallback(callback));
    }

    public void acceptTransaction(String id, OnNetworkListener callback) {
        Bundle args = new Bundle();
        args.putString(NetworkRequests.NetworkUtils.TRANSACTION_ID, id);
        Request request = NetworkRequests.buildRequest(args, NetworkCalls.ACCEPT_TRANSACTION);
        if(request == null) {
            NetworkPayload payload = new NetworkPayload.Builder<Void>()
                    .setData(null)
                    .setCode(100)
                    .setMessage("Could not make request")
                    .build();
            callback.onFailure(payload);
            return;
        }

        mClient.newCall(request).enqueue(NetworkCallbackFactory.acceptTransactionCallback(callback));
    }

    public void confirmTransaction(String id, OnNetworkListener callback) {
        Bundle args = new Bundle();
        args.putString(NetworkRequests.NetworkUtils.TRANSACTION_ID, id);
        Request request = NetworkRequests.buildRequest(args, NetworkCalls.CONFIRM_TRANSACTION);
        if(request == null) {
            NetworkPayload payload = new NetworkPayload.Builder<Void>()
                    .setData(null)
                    .setCode(100)
                    .setMessage("Could not make request")
                    .build();
            callback.onFailure(payload);
            return;
        }

        mClient.newCall(request).enqueue(NetworkCallbackFactory.confirmTransactionCallback(callback));
    }

    public void rateTransaction(String id, int rating, String comment, OnNetworkListener callback) {
        Bundle args = new Bundle();
        args.putString(NetworkRequests.NetworkUtils.TRANSACTION_ID, id);
        args.putInt(NetworkRequests.NetworkUtils.TRANSACTION_RATING, rating);
        args.putString(NetworkRequests.NetworkUtils.TRANSACTION_COMMENT, comment);
        Request request = NetworkRequests.buildRequest(args, NetworkCalls.RATE_TRANSACTION);
        if(request == null) {
            NetworkPayload payload = new NetworkPayload.Builder<Void>()
                    .setData(null)
                    .setCode(100)
                    .setMessage("Could not make request")
                    .build();
            callback.onFailure(payload);
            return;
        }

        mClient.newCall(request).enqueue(NetworkCallbackFactory.rateTransactionCallback(callback));
    }

    public void getTransactions(OnNetworkListener callback) {
        Request request = NetworkRequests.buildRequest(new Bundle(), NetworkCalls.GET_TRANSACTIONS);
        if(request == null) {
            NetworkPayload payload = new NetworkPayload.Builder<Void>()
                    .setData(null)
                    .setCode(100)
                    .setMessage("Could not make request")
                    .build();
            callback.onFailure(payload);
            return;
        }

        mClient.newCall(request).enqueue(NetworkCallbackFactory.rateTransactionCallback(callback));
    }

    public void getRankings(OnNetworkListener callback) {
        Request request = NetworkRequests.buildRequest(new Bundle(), NetworkCalls.GET_RANKING);
        if(request == null) {
            NetworkPayload payload = new NetworkPayload.Builder<Void>()
                    .setData(null)
                    .setCode(100)
                    .setMessage("Could not make request")
                    .build();
            callback.onFailure(payload);
            return;
        }

        mClient.newCall(request).enqueue(NetworkCallbackFactory.rankingsCallback(callback));
    }
}
