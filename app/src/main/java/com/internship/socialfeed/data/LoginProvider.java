package com.internship.socialfeed.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.internship.socialfeed.Configuration.Configuration;
import com.internship.socialfeed.components.Provider;
import com.internship.socialfeed.components.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.google.android.gms.analytics.internal.zzy.e;
import static com.internship.socialfeed.components.Provider.EMAIL_LOGIN;

/**
 * Provides user login via different ways(Facebook Login, Google Login, Email Login)
 */
public class LoginProvider implements FacebookCallback<LoginResult>,
        GoogleApiClient.OnConnectionFailedListener {
    private Context context;
    // fb
    private CallbackManager callbackManager;
    private OnLoginProviderListener loginListener;
    // google
    private static final int RC_GOOGLE_LOGIN = 9001;
    private static GoogleApiClient mGoogleApiClient;
    private GoogleSignInResult result;

    public LoginProvider(Context context, OnLoginProviderListener loginRequested) {
        this.loginListener = loginRequested;
        this.context = context;
        FacebookSdk.sdkInitialize(context);
    }

    /**
     * Handles activity results in case of social login.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(Configuration.TAG,"login provider "+requestCode+"    "+resultCode);
        if (requestCode == RC_GOOGLE_LOGIN) {
            handleGoogleLoginResult(data);
        }
        else {
            Log.i(Configuration.TAG,"result");
            if(callbackManager != null)  callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Handles the data received by google login activity result.
     * @param data
     */
    private void handleGoogleLoginResult(Intent data) {
        if (data != null) {
            result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result != null) {
                try {
                    GoogleSignInAccount account = result.getSignInAccount();
                    if (account!=null) {
                        User user = new User(result.getSignInAccount().getDisplayName(), result.getSignInAccount().getEmail(), "");
                        user.setId(result.getSignInAccount().getId());
                        user.setProvider(Provider.GOOGLE_LOGIN);
                        Log.i(Configuration.TAG, "Google login success");
                        SessionManager.getInstance().storeSession(user);
                        loginListener.onLoginSuccess(user);
                    }
                    } catch (Exception e) {
                        // Do Nothing
                }
            }
            //logout
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            }
        }
    }

    // FACEBOOK //

    /**
     * Requests to login the user using the Facebook SDK.
     * The result will be handled in the callbackManager.
     */
    public void loginViaFB(Activity activity){
        // checks if an accessToken already exists
        if(AccessToken.getCurrentAccessToken() != null ){
            Log.i("logged","Logged in");
        }
        callbackManager = CallbackManager.Factory.create();
        //requests fblogin with public profile and user friends permissions
        LoginManager.getInstance().registerCallback(callbackManager,this);
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile","user_friends"));

    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        // request fb login
        loginFacebookUser(loginResult.getAccessToken());

        //Handles the result of login and changes the JSON Object to User Object with equivalent values.
        GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject json, GraphResponse response) {
                        if (response.getError() != null) {
                            // handle error
                            Log.i(Configuration.TAG,"Fb Login Error ERROR");
                        } else {
                            Log.i(Configuration.TAG,json.toString());
                            User user;
                            try {
                                String name = null;
                                String id = json.getString("id");
                                if (!json.isNull("name") ) name = json.getString("name");
                                user = new User(name, "", "");
                                user.setId(id);
                                user.setProvider(Provider.Fb_LOGIN);
                                Log.i(Configuration.TAG, "fb login success");
                                SessionManager.getInstance().storeSession(user);
                                loginListener.onLoginSuccess(user);
                            } catch (JSONException e) {
                                Log.i(Configuration.TAG, " n: " + e.toString());
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

    /**
     * Triggers the google login process.
     * @param fragmentActivity
     */
    public void loginViaGoogle(FragmentActivity fragmentActivity) {
        // check if not initialized previously
        if (mGoogleApiClient == null) {
            // init sign in objects
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .enableAutoManage(fragmentActivity /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            Log.i(Configuration.TAG,"result null");
        }
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        fragmentActivity.startActivityForResult(signInIntent, RC_GOOGLE_LOGIN);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        loginListener.onLoginFailed("Connection Error");
    }

    /**
     * Method to login user via email, sends request to SessionManager to store session.
     * @param emailInput is the email of the user.
     * @param passwordInput is the password of the user.
     */
    public void loginViaEmail(String emailInput, String passwordInput) {
        User user = new User(emailInput, passwordInput);
        user.setId((int)Math.random()*100 + "");
        user.setProvider(EMAIL_LOGIN);
        SessionManager.getInstance().storeSession(user);
    }

    /**
     * Method to logout current user
     */
    public void logOut() {
        User user = SessionManager.getInstance().getSessionUser();
        Log.i(Configuration.TAG, "provider" + user.getProvider());
        if (user.getProvider() != null) {
            switch (user.getProvider()) {
                case EMAIL_LOGIN:
                    SessionManager.getInstance().clearSession();
                    loginListener.onLogOutSuccess();
                    break;
                case Fb_LOGIN:
                    LoginManager.getInstance().logOut();
                    SessionManager.getInstance().clearSession();
                    loginListener.onLogOutSuccess();
                    break;
                case GOOGLE_LOGIN:
                    SessionManager.getInstance().clearSession();
                    loginListener.onLogOutSuccess();
            }
        } else {
            SessionManager.getInstance().clearSession();
            loginListener.onLogOutSuccess();
        }
    }

    /**
     * Handles the registration of a user.
     */
    public void registerUser(String name, String email, String password) {
        User user = new User(name, email, password);
        user.setId((int)Math.random()*100 + "");
        user.setProvider(EMAIL_LOGIN);
        SessionManager.getInstance().storeSession(user);
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

        void onLogOutSuccess();
    }

    // API //
}
