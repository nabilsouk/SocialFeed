package com.internship.socialfeed;

import android.app.Application;

import com.internship.socialfeed.Configuration.Configuration;

/**
 * Created by Nader on 07-Sep-16.
 */

/**
 * Main Activity class which initializes application base components.
 */
public class SocialFeed extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
         Configuration.setContext(getApplicationContext());
    }


}
