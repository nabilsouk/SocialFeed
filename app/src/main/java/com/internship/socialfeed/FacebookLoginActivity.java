package com.internship.socialfeed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by Nader on 30-Aug-16.
 */

public class FacebookLoginActivity extends AppCompatActivity implements FacebookCallback<LoginResult> {
    private CallbackManager callbackManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.main);
        if(AccessToken.getCurrentAccessToken()!=null ){
            Log.i("logged","Logged in");

        }
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,this);
        LoginManager.getInstance().logInWithReadPermissions(FacebookLoginActivity.this, Arrays.asList("public_profile","user_friends"));

    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject json, GraphResponse response) {
                        if (response.getError() != null) {
                            // handle error
                            System.out.println("ERROR");
                        } else {
                            System.out.println("Success");
                            try {

                                String jsonresult = String.valueOf(json);
                                TextView textView =(TextView)findViewById(R.id.textview);
                                textView.setText(jsonresult);

                                String email = json.getString("email");
                                String id = json.getString("id");
                                String firstname = json.getString("first_name");
                                String lastname = json.getString("last_name");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }).executeAsync();

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,(int)resultCode,data);
    }




}
