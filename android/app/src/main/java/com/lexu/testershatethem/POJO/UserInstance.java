package com.lexu.testershatethem.POJO;

/**
 * Created by lexu on 24.03.2018.
 */

public final class UserInstance {

    private static final String TAG = UserInstance.class.getSimpleName();

    //region Utils
    public interface OnUserUpdateListener {
        enum ResponseCode {
            SUCCESS, ERROR, INVALID
        }

        void onSuccess(ResponseCode code);
        void onFail(ResponseCode code, String message);
    }
    //endregion

    private String sessionId = null;

    private String mEmail = null;
    private String mName = null;
    private String mDescription = null;
    private String mPhone = null;
    private String mAddress = null;
    private String mType = null;

    private static final Object LOCK = new Object();

    //region Singleton
    private static UserInstance INSTANCE = new UserInstance();

    public static UserInstance getInstance() {
        return INSTANCE;
    }

    private UserInstance() {
    }

    public synchronized void setToken(String token, OnUserUpdateListener listener) {
        if(INSTANCE.sessionId == null) {
            INSTANCE.sessionId = token;
            listener.onSuccess(OnUserUpdateListener.ResponseCode.SUCCESS);
        } else {
            listener.onFail(OnUserUpdateListener.ResponseCode.ERROR, "Session already active");
        }
    }
    //endregion

    public void setData(String email, String type, String name, String description, String phone, String address, OnUserUpdateListener listener) {
        boolean result = true;

        synchronized (LOCK) {
            if (INSTANCE.mName == null) {
                INSTANCE.mName = name;
            } else {
                result = false;
            }

            if (INSTANCE.mDescription == null) {
                INSTANCE.mDescription = description;
            } else {
                result = false;
            }

            if (INSTANCE.mAddress == null) {
                INSTANCE.mAddress = address;
            } else {
                result = false;
            }

            if (INSTANCE.mPhone == null) {
                INSTANCE.mPhone = phone;
            } else {
                result = false;
            }

            if(INSTANCE.mEmail == null) {
                INSTANCE.mEmail = email;
            } else {
                result = false;
            }

            if(INSTANCE.mType == null) {
                INSTANCE.mType = type;
            } else {
                result = false;
            }

            if (result) {
                listener.onSuccess(OnUserUpdateListener.ResponseCode.SUCCESS);
            } else {
                listener.onFail(OnUserUpdateListener.ResponseCode.ERROR, "Data was already set");
            }
        }
    }

    //region Getters
    public synchronized String getSessionId() {
        return this.sessionId;
    }

    public String getName() {
        synchronized (LOCK) {
            return mName;
        }
    }

    public String getDescription() {
        synchronized (LOCK) {
            return mDescription;
        }
    }

    public String getAddress() {
        synchronized (LOCK) {
            return mAddress;
        }
    }

    public String getPhone() {
        synchronized (LOCK) {
            return mPhone;
        }
    }

    public String getEmail() {
        synchronized (LOCK) {
            return mEmail;
        }
    }

    public String getType() {
        synchronized (LOCK) {
            return mType;
        }
    }
    //endregion
}
