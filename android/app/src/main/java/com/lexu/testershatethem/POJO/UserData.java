package com.lexu.testershatethem.POJO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by lexu on 25.03.2018.
 */

public class UserData implements Serializable {

    interface UserUtils {
        String USER_ID = "id";
        String USER_NAME = "name";
        String USER_DESCRIPTION = "description";
        String USER_EMAIL = "email";
        String USER_PHONE = "phone";
        String USER_CITY = "city";
        String USER_COUNTRY = "country";
        String USER_RATING = "rating";
    }

    private String mId = null;
    private String mName = null;
    private String mDescription = null;
    private String mEmail = null;
    private String mPhone = null;
    private String mCity = null;
    private String mCountry = null;

    private float mRating = -1;

    private UserData(String id, String name, String description, String email, String phone, String city, String country) {
        mId = id;
        mName = name;
        mDescription = description;
        mEmail = email;
        mPhone = phone;
        mCity = city;
        mCountry = country;
    }

    //region GETTERS
    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getCity() {
        return mCity;
    }

    public String getCountry() {
        return mCountry;
    }

    public float getRating() {
        return mRating;
    }
    //endregion

    public static class UserParser {
        static ArrayList<UserData> parseUser(JSONArray jsonArray) throws JSONException {
            ArrayList<UserData> result = new ArrayList<UserData>();
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString(UserUtils.USER_ID);
                String name = jsonObject.getString(UserUtils.USER_NAME);
                String desc = jsonObject.getString(UserUtils.USER_DESCRIPTION);
                String email = jsonObject.getString(UserUtils.USER_EMAIL);
                String phone = jsonObject.getString(UserUtils.USER_PHONE);
                String city = jsonObject.getString(UserUtils.USER_CITY);
                String country = jsonObject.getString(UserUtils.USER_COUNTRY);
                float rating = (float) jsonObject.getDouble(UserUtils.USER_RATING);

                result.add(new UserData(id, name, desc, email, phone, city, country));
            }

            return result;
        }
    }
}
