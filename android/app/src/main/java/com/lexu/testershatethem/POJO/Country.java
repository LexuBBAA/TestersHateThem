package com.lexu.testershatethem.POJO;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lexu on 24.03.2018.
 */

public class Country {

    static final class CountryUtils {
        public static final String COUNTRY_ID = "id";
        public static final String COUNTRY_NAME = "name";
    }

    private String mId = null;
    private String mName = null;

    private Country(String id, String name) {
        mId = id;
        mName = name;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public static class CountryParser {
        public static ArrayList<Country> parseCountries(@NonNull JSONArray jsonData) throws JSONException {
            ArrayList<Country> result = new ArrayList<Country>();
            for(int i = 0; i < jsonData.length(); i++) {
                JSONObject jsonCountry = jsonData.getJSONObject(i);
                String id = jsonCountry.getString(CountryUtils.COUNTRY_ID);
                String name = jsonCountry.getString(CountryUtils.COUNTRY_NAME);

                result.add(new Country(id, name));
            }
            return result;
        }
    }
}
