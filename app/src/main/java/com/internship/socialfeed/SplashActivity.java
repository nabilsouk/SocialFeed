package com.internship.socialfeed;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
                    startActivity(intent);
                    finish();
                }
            }
        };

        Handler h = new Handler();
        h.postDelayed(r, delay);
    }

}
