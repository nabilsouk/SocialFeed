package com.internship.socialfeed;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.internship.socialfeed.data.LoginProvider;


public class LoginOptionsFragment extends Fragment implements
        View.OnClickListener,
        LoginProvider.OnLoginProviderListener {
    LoginProvider loginProvider ;
    private OnOptionsFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= getActivity().getLayoutInflater().inflate(R.layout.fragment_login_options, container, false);
        initializeViews(view);
        loginProvider = new LoginProvider(getActivity().getBaseContext(),this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mListener.changeToolbarTitle(getString(R.string.app_name));
    }

    public void loginButtonPressed() {
        if (mListener != null) {
            mListener.onLoginSelected();
        }
    }
    public void RegisterButtonPressed() {
        if (mListener != null) {
            mListener.onRegisterSelected();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (loginProvider != null)  loginProvider.onActivityResult(requestCode, resultCode, data);
    }

    public void FbloginButtonPressed() {
        loginProvider.loginViaFB(this);
    }
    public void GoogleloginButtonPressed() {
        loginProvider.loginViaGoogle(getActivity());
    }
    void initializeViews(View view) {
        view.findViewById(R.id.btnFbLogin).setOnClickListener(this);
        view.findViewById(R.id.btnGmailLogin).setOnClickListener(this);
        view.findViewById(R.id.btnEmailLogin).setOnClickListener(this);
        view.findViewById(R.id.btnRegister).setOnClickListener(this);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOptionsFragmentInteractionListener) {
            mListener = (OnOptionsFragmentInteractionListener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnFbLogin:
                FbloginButtonPressed();
                break;
            case R.id.btnGmailLogin:
                GoogleloginButtonPressed();
                break;
            case R.id.btnEmailLogin:
                loginButtonPressed();
                break;
            case R.id.btnRegister:
                RegisterButtonPressed();
                break;
        }
    }

    @Override
    public void onLoginSuccess(User user) {

    }

    @Override
    public void onLoginFailed(String error) {

    }

    @Override
    public void onLoginCanceled() {

    }

    public interface OnOptionsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onLoginSelected();
        void onRegisterSelected();
        void onLoggedIn();
        void onShowProgress();
        void onDismissProgress();
        void changeToolbarTitle(String title);
    }
}
