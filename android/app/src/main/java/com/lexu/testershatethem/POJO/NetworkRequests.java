package com.lexu.testershatethem.POJO;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.security.NoSuchAlgorithmException;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.lexu.testershatethem.POJO.NetworkRequests.NetworkUtils.CITIES;
import static com.lexu.testershatethem.POJO.NetworkRequests.NetworkUtils.CITY_COUNTRY_ID;
import static com.lexu.testershatethem.POJO.NetworkRequests.NetworkUtils.COUNTRIES;
import static com.lexu.testershatethem.POJO.NetworkRequests.NetworkUtils.PROFILE;
import static com.lexu.testershatethem.POJO.NetworkRequests.NetworkUtils.TOKEN;
import static com.lexu.testershatethem.POJO.NetworkRequests.NetworkUtils.TRANSACTION_DATE;
import static com.lexu.testershatethem.POJO.NetworkRequests.NetworkUtils.TRANSACTION_ID;
import static com.lexu.testershatethem.POJO.NetworkRequests.NetworkUtils.TRANSACTION_QUANTITY;
import static com.lexu.testershatethem.POJO.NetworkRequests.NetworkUtils.USER_ID;

/**
 * Created by lexu on 24.03.2018.
 */

final class NetworkRequests {

    static class NetworkUtils {
        private static final int API_PORT = 8000;
        private static final String LOGIN = "login";
        private static final String REGISTER = "register";
        private static final String LISTING = "listing";
        private static final String NEW_TRANSACTION = "transactions";
        private static final String ACCEPT_TRANSACTION = "approve";
        private static final String CLOSE_TRANSACTION = "finish";
        private static final String REVIEW_TRANSACTION = "rating";
        static final String RANKING = "rankings";
        static final String PROFILE = "profile";
        static final String COUNTRIES = "countries";
        static final String CITIES = "cities/";

        private static final String API_URL = "http://192.168.0.105:" + API_PORT + '/';

        static final String TOKEN = "token";
        static final String REQUEST_STATUS = "success";
        static final String REQUEST_MESSAGE = "message";
        static final String REQUEST_DATA = "data";

        //region LOGIN
        static final String LOGIN_USERNAME = "email";
        static final String LOGIN_PASSWORD = "password";
        //endregion

        //region REGISTER
        static final String REGISTER_USERNAME = "email";
        static final String REGISTER_NAME = "user_name";
        static final String REGISTER_PASSWORD = "password";
        static final String REGISTER_DESCRIPTION = "desc";
        static final String REGISTER_ADDRESS = "address";
        static final String REGISTER_PHONE = "phone";
        static final String REGISTER_TYPE = "user_type";
        static final String REGISTER_COUNTRY_ID = "country_id";
        static final String REGISTER_CITY_ID = "city_id";
        //endregion

        //region MAIN_SCREEN
        static final String USER_ID = "id";
        //endregion

        //region COUNTRY + CITY
        static final String CITY_COUNTRY_ID = "country_id";
        //endregion

        //region TRANSACTIONS
        static final String TRANSACTION_ID = "id";
        static final String TRANSACTION_QUANTITY = "quantity";
        static final String TRANSACTION_DATE = "date";
        static final String TRANSACTION_RATING = "rating";
        static final String TRANSACTION_COMMENT = "comment";
        //endregion

        static String md5(String str) throws NoSuchAlgorithmException {
//            byte[] bytes = str.getBytes();
//
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            bytes = md.digest(bytes);
//
//            StringBuilder buffer = new StringBuilder();
//            for (byte aByte : bytes) {
//                buffer.append(Integer.toHexString((aByte & 0xFF) | 0x100).substring(1, 3));
//            }
//            return buffer.toString();
            return str;
        }
    }

    private static class PostRequestBuilder {
        static RequestBody loginBody(@NonNull String email, @NonNull String password) {
            return new FormBody.Builder()
                    .add(NetworkUtils.LOGIN_USERNAME, email)
                    .add(NetworkUtils.LOGIN_PASSWORD, password)
                    .build();
        }

        static RequestBody registerBody(@NonNull String email, @NonNull String password, @NonNull String name, @NonNull String address, @NonNull String phone, @NonNull String description, @NonNull String countryId, @NonNull String cityId, @NonNull String type) {
            return new FormBody.Builder()
                    .add(NetworkUtils.REGISTER_USERNAME, email)
                    .add(NetworkUtils.REGISTER_NAME, name)
                    .add(NetworkUtils.REGISTER_PASSWORD, password)
                    .add(NetworkUtils.REGISTER_ADDRESS, address)
                    .add(NetworkUtils.REGISTER_PHONE, phone)
                    .add(NetworkUtils.REGISTER_DESCRIPTION, description)
                    .add(NetworkUtils.REGISTER_COUNTRY_ID, countryId)
                    .add(NetworkUtils.REGISTER_CITY_ID, cityId)
                    .add(NetworkUtils.REGISTER_TYPE, type)
                    .build();
        }

        static RequestBody userProfile(String profileId) {
            return new FormBody.Builder()
                    .add(NetworkUtils.PROFILE, profileId)
                    .build();
        }

        static RequestBody newTransaction(String id, int quantity, String date) {
            return new FormBody.Builder()
                    .add(TRANSACTION_ID, id)
                    .add(TRANSACTION_QUANTITY, String.valueOf(quantity))
                    .add(TRANSACTION_DATE, String.valueOf(date))
                    .build();
        }

        static RequestBody reviewTransaction(int rating, String comment) {
            return new FormBody.Builder()
                    .add(NetworkUtils.TRANSACTION_RATING, String.valueOf(rating))
                    .add(NetworkUtils.TRANSACTION_COMMENT, comment)
                    .build();
        }
    }

    @Nullable
    static Request buildRequest(@NonNull Bundle data, @NonNull HttpRequester.NetworkCalls callType) {
        switch (callType) {
            case LOGIN:
                //region Build Login Request
                String email = data.getString(NetworkUtils.LOGIN_USERNAME);
                String password = data.getString(NetworkUtils.LOGIN_PASSWORD);

                if(email == null) {
                    email = "";
                }

                if(password == null) {
                    password = "";
                }

                return new Request.Builder()
                        .url(NetworkUtils.API_URL + NetworkUtils.LOGIN)
                        .post(PostRequestBuilder.loginBody(email, password))
                        .build();
                //endregion
            case REGISTER:
                //region Build Register Request
                email = data.getString(NetworkUtils.REGISTER_USERNAME);
                password = data.getString(NetworkUtils.REGISTER_PASSWORD);
                String name = data.getString(NetworkUtils.REGISTER_NAME);
                String address = data.getString(NetworkUtils.REGISTER_ADDRESS);
                String phone = data.getString(NetworkUtils.REGISTER_PHONE);
                String description = data.getString(NetworkUtils.REGISTER_DESCRIPTION);
                String type = data.getString(NetworkUtils.REGISTER_TYPE);
                String countryId = data.getString(NetworkUtils.REGISTER_COUNTRY_ID);
                String cityId = data.getString(NetworkUtils.REGISTER_CITY_ID);

                if(email == null) {
                    email = "";
                }

                if(password == null) {
                    password = "";
                }

                if(name == null) {
                    name = "";
                }

                if(address == null) {
                    address = "";
                }

                if(phone == null) {
                    phone = "";
                }

                if(description == null) {
                    description = "";
                }

                if(type == null) {
                    type = "";
                }

                if(countryId == null) {
                    countryId = "";
                }

                if(cityId == null) {
                    cityId = "";
                }

                return new Request.Builder()
                        .url(NetworkUtils.API_URL + NetworkUtils.REGISTER)
                        .post(
                                PostRequestBuilder.registerBody(
                                        email,
                                        password,
                                        name,
                                        address,
                                        phone,
                                        description,
                                        countryId,
                                        cityId,
                                        type
                                )
                        )
                        .build();
                //endregion
            case GET_BASE_DATA:
                //region Listing Request
                return new Request.Builder()
                        .addHeader(TOKEN, UserInstance.getInstance().getSessionId())
                        .url(NetworkUtils.API_URL + NetworkUtils.LISTING)
                        .build();
                //endregion
            case GET_COUNTRIES:
                //region Build Countries Request
                return new Request.Builder()
                        .url(NetworkUtils.API_URL + COUNTRIES)
                        .build();
                //endregion
            case GET_CITIES:
                //region Cities Request
                countryId = data.getString(CITY_COUNTRY_ID);
                if(countryId == null) {
                    countryId = "";
                }
                return new Request.Builder()
                        .url(NetworkUtils.API_URL + CITIES + countryId)
                        .build();
                //endregion
            case GET_MY_PROFILE:
                //region Current User Profile Request
                return new Request.Builder()
                        .addHeader(TOKEN, UserInstance.getInstance().getSessionId())
                        .url(NetworkUtils.API_URL + PROFILE)
                        .build();
                //endregion
            case GET_USERS_PROFILES:
                //region Users Profiles Request
                String profileId = data.getString(USER_ID);
                return new Request.Builder()
                        .addHeader(TOKEN, UserInstance.getInstance().getSessionId())
                        .post(PostRequestBuilder.userProfile(profileId))
                        .url(NetworkUtils.API_URL + NetworkUtils.PROFILE)
                        .build();
                //endregion
            case POST_NEW_TRANSACTION:
                //region New Transaction Request
                String id = data.getString(NetworkUtils.TRANSACTION_ID);
                int quantity = data.getInt(NetworkUtils.TRANSACTION_QUANTITY);
                String dateString = data.getString(NetworkUtils.TRANSACTION_DATE);
                return new Request.Builder()
                        .addHeader(TOKEN, UserInstance.getInstance().getSessionId())
                        .post(PostRequestBuilder.newTransaction(id, quantity, dateString))
                        .url(NetworkUtils.API_URL + NetworkUtils.NEW_TRANSACTION)
                        .build();
                //endregion
            case ACCEPT_TRANSACTION:
                //region Accept Transaction:
                id = data.getString(NetworkUtils.TRANSACTION_ID);
                return new Request.Builder()
                        .addHeader(NetworkUtils.TOKEN, UserInstance.getInstance().getSessionId())
                        .url(NetworkUtils.API_URL + NetworkUtils.NEW_TRANSACTION + '/' + id + NetworkUtils.ACCEPT_TRANSACTION)
                        .build();
                //endregion
            case CONFIRM_TRANSACTION:
                //region Confirm Transaction Request
                id = data.getString(TRANSACTION_ID);
                return new Request.Builder()
                        .addHeader(NetworkUtils.TOKEN, UserInstance.getInstance().getSessionId())
                        .url(NetworkUtils.API_URL + NetworkUtils.NEW_TRANSACTION + '/' + id + NetworkUtils.CLOSE_TRANSACTION)
                        .build();

                //endregion
            case RATE_TRANSACTION:
                //region Rate Transaction
                id = data.getString(NetworkUtils.TRANSACTION_ID);
                int rating = data.getInt(NetworkUtils.TRANSACTION_RATING);
                String comment = data.getString(NetworkUtils.TRANSACTION_COMMENT);
                return new Request.Builder()
                        .addHeader(NetworkUtils.TOKEN, UserInstance.getInstance().getSessionId())
                        .post(PostRequestBuilder.reviewTransaction(rating, comment))
                        .url(NetworkUtils.API_URL + NetworkUtils.NEW_TRANSACTION + '/' + id + NetworkUtils.REVIEW_TRANSACTION)
                        .build();
                //endregion
            case GET_TRANSACTIONS:
                //region Get Transactions Request
                return new Request.Builder()
                        .url(NetworkUtils.API_URL + NetworkUtils.NEW_TRANSACTION)
                        .addHeader(NetworkUtils.TOKEN, UserInstance.getInstance().getSessionId())
                        .build();
                //endregion
            case GET_RANKING:
                //region Get Ranking Request
                return new Request.Builder()
                        .addHeader(NetworkUtils.TOKEN, UserInstance.getInstance().getSessionId())
                        .url(NetworkUtils.API_URL + NetworkUtils.RANKING)
                        .build();
                //endregion
            default:
                return null;
        }
    }
}
