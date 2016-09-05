package com.internship.socialfeed;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.internship.socialfeed.data.LoginProvider;

public class LoginFragment extends Fragment implements
        View.OnClickListener,
        LoginProvider.OnLoginProviderListener
{
    static EditText etEmail;
    static EditText etPassword;
    static Button btnSubmit;
    static Button btnRegister;
    static TextInputLayout inputLayoutEmail;
    static TextInputLayout inputLayoutPassword;
    private LoginFragment.OnLoginFragmentInteractionListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_login,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initViews(view);
       // onLoginPressed(view);

    }

    /**
     * Initialize the views of the fragment
     * @param view
     */
    private void initViews(View view) {
        etEmail =  (EditText) view.findViewById(R.id.input_email);
        etPassword = (EditText)view.findViewById(R.id.input_password);
        btnSubmit= (Button) view.findViewById(R.id.btnSubmit);
        btnRegister = (Button)view.findViewById(R.id.btnRegister);

        inputLayoutEmail = (TextInputLayout) view.findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) view.findViewById(R.id.input_layout_password);

        inputLayoutEmail.setErrorEnabled(true);
        inputLayoutPassword.setErrorEnabled(true);
        btnSubmit.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

             etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                 @Override
                 public void onFocusChange(View view, boolean b) {
                    if(!b){
                        validateEmail();
                    }
                     if (b){
                         inputLayoutEmail.setError(null);
                     }
                 }
             });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    validatePassword();
                }
                if (b){
                    inputLayoutPassword.setError(null);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
        mListener.changeToolbarTitle("Login");
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginOptionsFragment.OnOptionsFragmentInteractionListener) {
            mListener = (LoginFragment.OnLoginFragmentInteractionListener) context;
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
        switch (view.getId()){
            case R.id.btnSubmit:
                submitButtonPressed();
                break;
            case R.id.btnRegister:
                registerButtonPressed();
                break;
        }
    }


    /** Handles the press of the submit button, and if credentials are valid,
     * an instant of the LoginProvider class is created, which is used to call
     * loginViaEmail method and passing the credentials to it.
     */
    private void submitButtonPressed() {

        // validate inputs
        if (validateEmail() && validatePassword()) {
            // inputs are valid
            // inform parent
            mListener.onShowProgress();
            LoginProvider loginProvider = new LoginProvider(getContext(), this);
            loginProvider.loginViaEmail(getEmailInput(),getPasswordInput());
            // inform parent of execution end
        }
        // no need for else statement, validation will show errors
    }

    /**
     * Validates the email inserted in the email TextField,
     * if email is not valid, shows an error message on the inputLayout
     * @return true if email is valid, false otherwise.
     */
    private boolean validateEmail() {
        // get email user input
        String email = getEmailInput();
        // check if valid
        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            //requestFocus(etEmail);
            return false;
        } else {
            inputLayoutEmail.setError(null);
        }

        return true;
    }

    /**
     *
     * @return the Email TextField String Trimmed.
     */
    private String getEmailInput() {
        return etEmail.getText().toString().trim();
    }

    /**
     *
     * @return the Password TextField String Trimmed.
     */
    private String getPasswordInput() {return etPassword.getText().toString().trim();}

    /**
     * Validates the password inserted in the password TextField,
     * if password is not valid, shows an error message on the password inputLayout
     * @return true if password is valid, false otherwise.
     */
    private boolean validatePassword() {
        if (getPasswordInput().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
           // requestFocus(etPassword);
            return false;
        } else {
            inputLayoutPassword.setError(null);
        }

        return true;
    }


    /**
     * Checks the validity of the email according to email pattern
     * @param email is the email string being validated
     * @return true if email is valid, false otherwise.
     */
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Calls parent's onRegisterSelected function.
     */
    private void registerButtonPressed() {
        mListener.onRegisterSelected();
    }

    @Override
    public void onLoginSuccess(User user) {
        mListener.onDismissProgress();
    }

    @Override
    public void onLoginFailed(String error) {

    }

    @Override
    public void onLoginCanceled() {

    }


    public interface OnLoginFragmentInteractionListener {
        // TODO: Update argument type and name
        void onLoggedIn(User user);
        void onRegisterSelected();
        void onShowProgress();
        void onDismissProgress();
        void changeToolbarTitle(String title);
    }

}
