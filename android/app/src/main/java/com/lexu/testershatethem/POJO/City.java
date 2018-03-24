package com.lexu.testershatethem.POJO;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lexu on 24.03.2018.
 */

public class City {

    static final class CityUtils {
        public static final String CITY_ID = "cid";
        public static final String CITY_NAME = "name";
    }

    private String mId = null;
    private String mName = null;

    private City(String id, String name) {
        mId = id;
        mName = name;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    @Override
    public String toString() {
        return "Id: " + mId + '\n' + "Name: " + mName;
    }

    public static class CityParser {
        public static ArrayList<City> parseCities(@NonNull JSONArray jsonData) throws JSONException {
            ArrayList<City> result = new ArrayList<City>();
            for(int i = 0; i < jsonData.length(); i++) {
                JSONObject jsonCountry = jsonData.getJSONObject(i);
                String id = jsonCountry.getString(CityUtils.CITY_ID);
                String name = jsonCountry.getString(CityUtils.CITY_NAME);

                result.add(new City(id, name));
            }

            result.add(0, new City("", "City"));
            return result;
        }
    }
}
