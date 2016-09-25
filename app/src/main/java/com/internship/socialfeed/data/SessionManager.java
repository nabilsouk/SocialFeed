package com.internship.socialfeed.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.internship.socialfeed.Configuration.Configuration;
import com.internship.socialfeed.components.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Manages the session of a logged in user.
 */

public class SessionManager {
    private static SessionManager sessionManager = null;
    private String KEY_USER = "user";
    // objects
    private User user;
    private boolean isLoggedIn;
    private SharedPreferences sharedPreferences;

    private SessionManager(){
        initSharedPreference();
        checkSessionState();
    }

    public static SessionManager getInstance(){
        if(sessionManager == null) sessionManager = new SessionManager();
        return sessionManager;
    }

    // SHARED PREF //

    /**
     * Initialize SharedPreference
     */
    private void initSharedPreference(){
        sharedPreferences = Configuration.getContext().getSharedPreferences("SessionManager", Context.MODE_PRIVATE) ;
    }

    /**
     * Checks isLoggedIn in sharedPreferences and sets isLoggedIn accordingly
     */
    private void checkSessionState() {
        // get logged in user
        user = getStoredUser();
        // check if valid
        isLoggedIn = (user != null && user.getId() != null);
    }

    /**
     *
     * @return stored user in sharedPreference
     */
    private User getStoredUser() {
        //Get the user's variables, if key is not found, will return null.
        String JSONString = sharedPreferences.getString(KEY_USER, null);
        // create user object from json
        if( JSONString != null ) {
            try {
                JSONObject userJSON = new JSONObject(JSONString);
                user = new User(userJSON);
            } catch (JSONException e) {
                user = null;
            }
        }
        return user;
    }

    /**
     * Stores the given user in SharedPreference.
     */
    private void storeUser(User user){
        // instantiate SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // stores each attribute in a variable, if attribute is null the editor will clear the corresponding value.
        editor.putString(KEY_USER, user.toJSONString());
        // commit changes
        editor.apply();
    }

    /**
     * Clears the data in sharedPreference
     */
    private void clearSharedPreference() {
        //Instantiate SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //clears all saved data in SharedPreferences with tag sm_tag
        editor.clear();
        editor.apply();
    }

    // //

    /**
     * Stores a new logged in session data.
     * @param user The logged in user.
     */
    public void storeSession(@NonNull User user){
        //calls storeUser method with user as argument
        storeUser(user);
        // validate input data
        checkSessionState();
    }

    /**
     *
     * @return user logged in if found, else returns null.
     */
    public User getSessionUser(){
        return this.user;
    }

    /**
     * Clears any stored session.
     */
    public void clearSession(){
        isLoggedIn = false;
        user = null;
        clearSharedPreference();
    }

    /**
     * Returns true if logged in, false otherwise.
     */
    public boolean isLoggedIn(){
        return isLoggedIn;
    }

    public enum SessionState{
        LOGGED_IN,LOGGED_OUT
    }
    public interface SessionManagerListener{
        public void onStateChanged(SessionState sessionState);
    }

}
