package com.anachat.chatsdk.internal.database;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lookup on 12/10/17.
 */

public class PreferencesManager {

    private static final String PREF_NAME = "com.ana.PREF_NAME";
    private static final String USER_NAME = "com.ana.sdk.USER_NAME";
    private static final String USER_NAME_LOGIN = "com.ana.sdk.USER_NAME_LOGIN";
    private static final String FIRST_LAUNCH = "com.ana.sdk.FIRST_TIME";
    private static final String TOKEN_SYNC = "com.ana.sdk.token_sync";
    private static final String THEME_COLOR = "com.ana.sdk.theme";
    private static final String BUSINESS_ID = "com.ana.sdk.business_id";
    private static final String FCM_TOKEN = "com.ana.sdk.fcm_token";
    private static final String BASE_URL = "com.ana.sdk.base_url";
    private static final String EVENTS_DATA = "com.ana.sdk.events.data";
    private static final String IS_HISTORY_SYNCED = "com.ana.sdk.history_sync";
    private static final String URL_Status = "com.ana.sdk.url_browser";

    private static PreferencesManager sInstance;
    private final SharedPreferences mPref;

    private PreferencesManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized PreferencesManager getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (PreferencesManager.class) {
                if (sInstance == null)
                    sInstance = new PreferencesManager(context);
            }

        }
        return sInstance;
    }

    public void setUserName(String value) {
        mPref.edit()
                .putString(USER_NAME, value)
                .apply();
    }

    public String getUserNameLogin() {
        return mPref.getString(USER_NAME_LOGIN, "");
    }


    public void setUserNameLogin(String value) {
        mPref.edit()
                .putString(USER_NAME_LOGIN, value)
                .apply();
    }

    public String getUserName() {
        return mPref.getString(USER_NAME, "");
    }

    public void setFirstLaunch(Boolean value) {
        mPref.edit()
                .putBoolean(FIRST_LAUNCH, value)
                .apply();
    }

    public void setIsHistorySynced(Boolean value) {
        mPref.edit()
                .putBoolean(IS_HISTORY_SYNCED, value)
                .apply();
    }

    public Boolean getTokenSync() {
        return mPref.getBoolean(TOKEN_SYNC, false);
    }

    public void setTokenSync(Boolean value) {
        mPref.edit()
                .putBoolean(TOKEN_SYNC, value)
                .apply();
    }

    public Boolean getFirstLaunch() {
        return mPref.getBoolean(FIRST_LAUNCH, true);
    }

    public Boolean getHistorySynced() {
        return mPref.getBoolean(IS_HISTORY_SYNCED, false);
    }


    public void setThemeColor(String value) {
        mPref.edit()
                .putString(THEME_COLOR, value)
                .apply();
    }

    public String getThemeColor() {
        return mPref.getString(THEME_COLOR, "#7cb342");
    }

    public void setFcmToken(String value) {
        mPref.edit()
                .putString(FCM_TOKEN, value)
                .apply();
    }

    public String getFcmToken() {
        return mPref.getString(FCM_TOKEN, "");
    }

    public void remove(String key) {
        mPref.edit()
                .remove(key)
                .apply();
    }

    public void setBusinessId(String value) {
        mPref.edit()
                .putString(BUSINESS_ID, value)
                .apply();
    }

    public String getBusinessId() {
        return mPref.getString(BUSINESS_ID, "");
    }

    public void setBaseUrl(String value) {
        mPref.edit()
                .putString(BASE_URL, value)
                .apply();
    }

    public String getBaseUrl() {
        return mPref.getString(BASE_URL, "");
    }


    public void setEventsData(String value) {
        mPref.edit()
                .putString(EVENTS_DATA, value)
                .apply();
    }

    public String getEventsData() {
        return mPref.getString(EVENTS_DATA, "");
    }


    public Boolean getUrlStatus() {
        return mPref.getBoolean(URL_Status, true);
    }

    public void setUrlStatus(Boolean value) {
        mPref.edit()
                .putBoolean(URL_Status, value)
                .apply();
    }

    public boolean clear() {
        return mPref.edit()
                .clear()
                .commit();
    }
}
