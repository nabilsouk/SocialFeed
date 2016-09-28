package com.internship.socialfeed;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.internship.socialfeed.Configuration.Configuration;
import com.internship.socialfeed.authentication.AuthenticationActivity;
import com.internship.socialfeed.data.SessionManager;
import com.internship.socialfeed.ui.ItemsListActivity;

/**
 * Activity class which is started first, and controls the base flow
 * of the application.
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        delay(1000);
    }
    
    /**
     * Delays the execution of the program
     * @param delay is the dime of delay in milliseconds
     * */
    private void delay(int delay) {
        Runnable r = new Runnable() {
            @Override
            public void run(){
                if(!SessionManager.getInstance().isLoggedIn()) {
                    Intent intent = new Intent(getBaseContext(), AuthenticationActivity.class);
                    //start and wait for response from AuthenticateActivity
                    startActivityForResult(intent, Configuration.AUTHENTICATION_REQUEST);
                }
                else {
                    Intent intent = new Intent(getBaseContext(), ItemsListActivity.class);
                    //clear activity task
                    startActivityForResult(intent, Configuration.AUTHENTICATION_REQUEST);
                }
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, delay);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(Configuration.TAG,"SplashActivity"+resultCode);

        if (resultCode == RESULT_CANCELED){
            Log.i(Configuration.TAG,"finish");
            finish();
        }
        //check request code and session instance
        else if(resultCode == Configuration.AUTHENTICATION_REQUEST && SessionManager.getInstance().isLoggedIn()){
            Intent intent = new Intent(getBaseContext(), ItemsListActivity.class);
            startActivityForResult(intent, Configuration.AUTHENTICATION_REQUEST);
        }
        else{
            Intent intent = new Intent(getBaseContext(), AuthenticationActivity.class);
            //start and wait for response from AuthenticateActivity
            startActivityForResult(intent, Configuration.AUTHENTICATION_REQUEST);
        }
    }
}
