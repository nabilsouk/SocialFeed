package com.internship.socialfeed;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Manages the session of the user
 */

public class SessionManager {
    private static SessionManager sessionManager = null;
    private static User user;
    private static boolean isLoggedIn;
    private static Context context;
    private static String sm_tag = "SessionManager";
    private SessionManager(){}

    public static SessionManager getInstance(){
        if(sessionManager == null) sessionManager = new SessionManager();
        return sessionManager;
    }

    private static void setContext(Context context1){
        context = context1;
    }

    /**
     * Stores the user in the session.
     * @param user1 is the user needed to be stored
     */
    public static void storeSession(User user1){
        user = user1;
        //calls storeUser method with user as argument
        storeUser(user1);
        isLoggedIn = true;
    }

    /**
     *
     * @return user logged in if found, else returns null.
     */
    public static User getSessionUser(){
        //Checks if user is logged in
        if (isLoggedIn) {
            //Instantiate SharedPreferences
            SharedPreferences sharedPreferences = context.getSharedPreferences(sm_tag, Context.MODE_PRIVATE);
            //Get the user's variables, if key is not found, will return null.
            String email = sharedPreferences.getString("Email", "");
            String password = sharedPreferences.getString("Password", "");
            User user = new User(email, password);
            return user;
        }
        else
        //if not logged in returns null
            return null;
        }

    /**
     * Clears the session and sets isLoggedIn to false.
     */
    public static void clearSession(){
        isLoggedIn = false;
        //Instantiate SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(sm_tag,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //clears all saved data in SharedPreferences with tag sm_tag
        editor.clear();
    }

    /**
     *
     * @return true if logged in false otherwise
     */
    public static boolean isLoggedIn(){
        return isLoggedIn;
    }

    /**
     * Stores user in SharedPreference as three variables(name,email,password).
     * @param user
     */
    public static void storeUser(User user){
        //Instantiate SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(sm_tag,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //Stores each attribute in a variable, if attribute is null the editor will clear the corresponding value.
        editor.putString("Email", user.getEmail());
        editor.putString("Password", user.getPassword());
        //commit changes
        editor.commit();
    }
}
