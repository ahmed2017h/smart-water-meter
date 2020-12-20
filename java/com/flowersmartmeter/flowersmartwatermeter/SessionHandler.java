package com.flowersmartmeter.flowersmartwatermeter;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class SessionHandler {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_LOGO_URL = "logo_url";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PHONE_NUMBER = "phoneNumber";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_COLLECTOR_ID ="collector_id";
    private static final String KEY_METER_ID = "meter_id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_EXPIRES = "expires";
    private static final String KEY_EMPTY = "";
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public SessionHandler(Context mContext) {
        this.mContext = mContext;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.mEditor = mPreferences.edit();
    }
    public void loginUser(String logo_url,String username, String phonenumber,String email,String collector_id,String meter_id,String user_id) {
        mEditor.putString(KEY_LOGO_URL,logo_url);
        mEditor.putString(KEY_USERNAME, username);
        mEditor.putString(KEY_PHONE_NUMBER, phonenumber);
        mEditor.putString(KEY_EMAIL,email);
        mEditor.putString(KEY_COLLECTOR_ID,collector_id);
        mEditor.putString(KEY_METER_ID,meter_id);
        mEditor.putString(KEY_USER_ID,user_id);
        Date date = new Date();

        //Set user session for next 7 days
        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
        mEditor.putLong(KEY_EXPIRES, millis);
        mEditor.apply();
        mEditor.commit();
    }
    /**
     * Checks whether user is logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        Date currentDate = new Date();

        long millis = mPreferences.getLong(KEY_EXPIRES, 0);

        /* If shared preferences does not have a value
         then user is not logged in
         */
        if (millis == 0) {
            return false;
        }
        Date expiryDate = new Date(millis);

        /* Check if session is expired by comparing
        current date and Session expiry date
        */
        return currentDate.before(expiryDate);
    }
    /**
     * Fetches and returns user details
     *
     * @return user details
     */
    public User getUserDetails() {
        //Check if user is logged in first
        if (!isLoggedIn()) {
            return null;
        }
        User user = new User();
        user.setLogo_url(mPreferences.getString(KEY_LOGO_URL,KEY_EMPTY));
        user.setUsername(mPreferences.getString(KEY_USERNAME, KEY_EMPTY));
        user.setphonenumber(mPreferences.getString(KEY_PHONE_NUMBER, KEY_EMPTY));
        user.setEmail(mPreferences.getString(KEY_EMAIL,KEY_EMPTY));
        user.setCollector_id(mPreferences.getString(KEY_COLLECTOR_ID,KEY_EMPTY));
        user.setMeter_id(mPreferences.getString(KEY_METER_ID,KEY_EMPTY));
        user.setUser_id(mPreferences.getString(KEY_USER_ID,KEY_EMPTY));
        user.setSessionExpiryDate(new Date(mPreferences.getLong(KEY_EXPIRES, 0)));

        return user;
    }
    /**
     * Logs out user by clearing the session
     */
    public void logoutUser(){
        mEditor.clear();
        mEditor.commit();
    }


}
