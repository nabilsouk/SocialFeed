package com.internship.socialfeed.data;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.internship.socialfeed.SessionManager;
import com.internship.socialfeed.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Provides user login via different ways(Facebook Login, Google Login, Email Login)
 */

public class LoginProvider implements FacebookCallback<LoginResult>,
        GoogleApiClient.OnConnectionFailedListener {
    private Context context;
    // fb
    private CallbackManager callbackManager;
    private OnLoginProviderListener loginListener;
    private LoginResult loginResult;
    // google
    private static final int RC_GOOGLE_LOGIN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInResult result;

    public LoginProvider(Context context, OnLoginProviderListener loginRequested) {
        this.loginListener = loginRequested;
        this.context = context;
    }

    /**
     * Handles activity results in case of social login.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_GOOGLE_LOGIN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            mGoogleApiClient.connect();
        }
        else {
            Log.i("activity result","result");
            if(callbackManager != null)  callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    // FACEBOOK //

    /**
     * Requests to login the user using the Facebook SDK.
     * The result will be handled in the callbackmanager.
     *
     * Requests facebook login of user and waits for
     * the sends a callbackManager listener to receive callbacks.
     *
     */
    public void loginViaFB(Fragment fragment){
        FacebookSdk.sdkInitialize(context);
        LoginManager.getInstance().logOut();
        // checks if an accessToken already exists
        if(AccessToken.getCurrentAccessToken() != null ){
            Log.i("logged","Logged in");
        }
        callbackManager = CallbackManager.Factory.create();
        //requests fblogin with public profile and friends of the user
        LoginManager.getInstance().registerCallback(callbackManager,this);
        LoginManager.getInstance().logInWithReadPermissions(fragment, Arrays.asList("public_profile","user_friends"));
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        // get access token
        Log.i("Token",loginResult.getAccessToken().getUserId());
        this.loginResult = loginResult;
        // request aoi login
       // loginFacebookUser();

        //Handles the result of login and changes the JSON Object to User Object with equivalent values.
        GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject json, GraphResponse response) {
                        if (response.getError() != null) {
                            // handle error
                            System.out.println("ERROR");
                        } else {

                            User user;
                            try {
                                //String email = json.getString("email");
                                //String id = json.getString("id");
                                String firstname = json.getString("first_name");
                                String lastname = json.getString("last_name");
                                String name = firstname+" "+lastname;
                                user = new User(name, "", "");
                                loginListener.onLoginSuccess(user);
                                Log.i("Success"," n: "+name);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                user = new User("", "", "");
                            }

                        }
                    }

                }).executeAsync();
    }

    @Override
    public void onCancel() {
        if (loginListener != null) {
            loginListener.onLoginCanceled();
        }
    }

    @Override
    public void onError(FacebookException error) {
        loginListener.onLoginFailed(error.toString());
    }

    // GOOGLE //

    public void loginViaGoogle(FragmentActivity fragmentActivity){
        // check if not initialized previously
        if (result == null) {
            // init sign in objects
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .enableAutoManage(fragmentActivity /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
             result = opr.get();
            Log.i("googleLog",result.getSignInAccount().getEmail());
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                   Log.i("googleLog",googleSignInResult.getSignInAccount().getEmail());
                }
            });
        }
        // request signin
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        fragmentActivity.startActivityForResult(signInIntent, RC_GOOGLE_LOGIN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        loginListener.onLoginFailed("Connection Error");
    }

    public void loginViaEmail(String emailInput, String passwordInput) {
        User user = new User(emailInput, passwordInput);
        SessionManager.getInstance().storeSession(user);

    }
    public void logOut(){
        SessionManager.getInstance().clearSession();
    }

    public void register(String name, String email, String password) {

    }

    private void loginFacebookUser(AccessToken accessToken) {

    }

    // CALLBACK //

    /**
     * Interface definition for callbacks to be invoked
     * when events occur in {@link LoginProvider}.
     */
    public interface OnLoginProviderListener {
        /**
         * Called once Login is successful.
         * @param user
         */
        void onLoginSuccess(User user);

        /**
         * Called once login is failed
         * @param error is the message describing the error
         */
        void onLoginFailed(String error);

        /**
         * Called once login is canceled
         */
        void onLoginCanceled();
    }

    // API //


}
