package com.internship.socialfeed.Configuration;

import android.content.Context;

/**
 * Created by Nader on 07-Sep-16.
 */

/**
 * Sets application context
 */
public class Configuration {

    private static Context context;
    public static int AUTHENTICATION_REQUEST = 10;
    public static String TAG = "Application_flow";
    public static void setContext(Context context1) {
        context = context1;
    }

    public static Context getContext() {
        return context;
    }
}
