package com.internship.socialfeed;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.internship.socialfeed.data.LoginProvider;

public class AuthenticationActivity extends AppCompatActivity implements
        LoginOptionsFragment.OnOptionsFragmentInteractionListener,
        LoginFragment.OnLoginFragmentInteractionListener,
        RegisterFragment.OnRegisterFragmentInteractionListener,
        LoginProvider.OnLoginProviderListener
{
    ProgressDialog pdLoader;
    Handler handler = new Handler();

    Fragment fragment;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        initPlLoader();
        initFragment();
        initToolbar();
    }

    private void initPlLoader() {
        pdLoader = new ProgressDialog(this);
        pdLoader.setMessage("Loading...");
        pdLoader.setCancelable(false);
        pdLoader.setIndeterminate(true);
    }

    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new LoginOptionsFragment();
        fragmentTransaction.add(R.id.optionLoginFragment, fragment);
        fragmentTransaction.commit();
    }

    void initToolbar() {
        toolbar =(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("SocialFeed");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
    }


    @Override
    public void onLoginSelected() {
        Fragment LoginFragment = new LoginFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.optionLoginFragment, LoginFragment, "login");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onLoggedIn(User user) {
        Toast.makeText(getApplication(),user.toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegisterSelected() {
        Fragment RegisterFragment = new RegisterFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.optionLoginFragment, RegisterFragment,"register");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onLoggedIn() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRegisterSubmitSelected(User user) {
        Toast.makeText(getApplication(),user.toString(),Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onShowProgress() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (pdLoader != null && !pdLoader.isShowing()) {
                    pdLoader.dismiss();
                }
            }
        });
    }

    @Override
    public void onDismissProgress() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (pdLoader != null && pdLoader.isShowing()) {
                    pdLoader.dismiss();
                }
            }
        });
    }

    @Override
    public void changeToolbarTitle(String title) {
        if (toolbar != null)    toolbar.setTitle(title);
    }

    @Override
    public void onLoginSuccess(User user) {
        Toast.makeText(this,user.toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoginFailed(String error) {
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginCanceled() {

    }

}
