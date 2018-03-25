package com.lexu.testershatethem.POJO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lexu on 25.03.2018.
 */

public class Transaction {

    public interface TransactionUtils {
        String TRANSACTION_ID = "tid";
        String TRANSACTION_STATE = "state";
        String TRANSACTION_QUANTITY = "quantity";
        String TRANSACTION_START_DATE = "start_date";
        String TRANSACTION_DUE_DATE = "due_date";
        String TRANSACTION_FINISH_DATE = "finish_date";
        String TRANSACTION_RATING = "rating";
        String TRANSACTION_MESSAGE = "message";
        String TRANSACTION_USER_ID = "uid";
        String TRANSACTION_USER_NAME = "name";
    }

    private String mID = null;
    private int mState = -1;
    private int mQuantity = -1;
    private String mStartDate = null;
    private String mDueDate = null;
    private String mEndDate = null;
    private int mRating = -1;
    private String mMessage = null;
    private String mUserId = null;
    private String mUserName = null;

    private Transaction(String ID, int state, int quantity, String startDate, String dueDate, String endDate, int rating, String message, String userId, String userName) {
        mID = ID;
        mState = state;
        mQuantity = quantity;
        mStartDate = startDate;
        mDueDate = dueDate;
        mEndDate = endDate;
        mRating = rating;
        mMessage = message;
        mUserId = userId;
        mUserName = userName;
    }

    private Transaction(String ID, int quantity, String dueDate, String userId) {
        mID = ID;
        mQuantity = quantity;
        mDueDate = dueDate;
        mUserId = userId;
    }

    public static class TransactionParser {
        public static ArrayList<Transaction> parseTransactions(JSONArray jsonArray) throws JSONException {
            ArrayList<Transaction> result = new ArrayList<Transaction>();
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                String id = json.getString(TransactionUtils.TRANSACTION_ID);
                int state = json.getInt(TransactionUtils.TRANSACTION_STATE);
                int quantity = json.getInt(TransactionUtils.TRANSACTION_QUANTITY);
                String startDate = json.getString(TransactionUtils.TRANSACTION_START_DATE);
                String dueDate = json.getString(TransactionUtils.TRANSACTION_DUE_DATE);
                String endDate = json.getString(TransactionUtils.TRANSACTION_FINISH_DATE);
                int rating = json.getInt(TransactionUtils.TRANSACTION_RATING);
                String message = json.getString(TransactionUtils.TRANSACTION_MESSAGE);
                String userId = json.getString(TransactionUtils.TRANSACTION_USER_ID);
                String userName = json.getString(TransactionUtils.TRANSACTION_USER_NAME);

                result.add(new Transaction(id, state, quantity, startDate, dueDate, endDate, rating, message, userId, userName));
            }

            return result;
        }
    }
}
