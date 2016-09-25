package com.internship.socialfeed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.internship.socialfeed.Configuration.Configuration;
import com.internship.socialfeed.HttpRequests.FacebookSharesCount;
import com.internship.socialfeed.authentication.AuthenticationActivity;
import com.internship.socialfeed.data.LoginProvider;
import com.internship.socialfeed.data.SessionManager;

public class LogOutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);
        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //clear session
                SessionManager.getInstance().clearSession();
                setResult(Configuration.AUTHENTICATION_REQUEST);
                finish();
            }
        });

        findViewById(R.id.btnFacebookShareCount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FacebookSharesCount().execute("http://graph.facebook.com/?id=http://www.google.com");
            }
        });
    }

}
