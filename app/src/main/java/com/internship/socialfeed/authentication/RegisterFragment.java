package com.internship.socialfeed.authentication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.internship.socialfeed.R;
import com.internship.socialfeed.components.User;
import com.internship.socialfeed.data.LoginProvider;


public class RegisterFragment extends Fragment implements LoginProvider.OnLoginProviderListener {
    static EditText etName;
    static EditText etEmail;
    static EditText etPassword;
    static TextInputLayout inputLayoutName;
    static TextInputLayout inputLayoutEmail;
    static TextInputLayout inputLayoutPassword;
    static  Button btnRegisterSubmit;
    private RegisterFragment.OnRegisterFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_register,container,false);
        initViews(view);
        return view;
    }
    private void initViews(View view) {

        etName = (EditText) view.findViewById(R.id.inputName);
        etEmail =  (EditText) view.findViewById(R.id.inputEmail);
        etPassword = (EditText)view.findViewById(R.id.inputPassword);

        inputLayoutName = (TextInputLayout) view.findViewById(R.id.inputLayoutName);
        inputLayoutEmail = (TextInputLayout) view.findViewById(R.id.inputLayoutEmail);
        inputLayoutPassword = (TextInputLayout) view.findViewById(R.id.inputLayoutPassword);

        inputLayoutName.setErrorEnabled(true);
        inputLayoutEmail.setErrorEnabled(true);
        inputLayoutPassword.setErrorEnabled(true);

        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    validateName();
                }
                if (b){
                    inputLayoutName.setError(null);
                }
            }
        });
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

        btnRegisterSubmit = (Button) view.findViewById(R.id.btnRegisterSubmit);
        btnRegisterSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }
    /** Handles the press of the submit button, and if credentials are valid,
     * an instant of the LoginProvider class is created, which is used to call
     * registerUser method and passing the credentials to it.
     */
    private void submitForm() {
        if (validateName() && validateEmail() && validatePassword()) {
            String name = getInputName();
            String email = getInputEmail();
            String password = getInputPassword();
            LoginProvider loginProvider = new LoginProvider(getContext(), RegisterFragment.this);
            loginProvider.registerUser(name,email,password);
            User user = new User(name, email, password);
            mListener.onRegisterSubmitSelected(user);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mListener.changeToolbarTitle("Register");
    }
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginOptionsFragment.OnOptionsFragmentInteractionListener) {
            mListener = (RegisterFragment.OnRegisterFragmentInteractionListener) context;
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

    /**
     *
     * @return the Password TextField String Trimmed.
     */
    private String getInputPassword() {
        return etPassword.getText().toString().trim();
    }

    /**
     * Validates the name inserted in the name TextField,
     * if name is not valid, shows an error message on the inputLayout
     * @return true if name is valid, false otherwise.
     */
    private boolean validateName() {
        if (getInputName().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            return false;
        } else {
            inputLayoutName.setError(null);
        }

        return true;
    }

    /**
     *
     * @return the Name TextField String Trimmed.
     */
    private String getInputName() {
        return etName.getText().toString().trim();
    }
    /**
     * Validates the email inserted in the email TextField,
     * if email is not valid, shows an error message on the inputLayout
     * @return true if email is valid, false otherwise.
     */
    private boolean validateEmail() {
        String email = getInputEmail();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
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
    private String getInputEmail() {
        return etEmail.getText().toString().trim();
    }
    /**
     * Validates the password inserted in the password TextField,
     * if password is not valid, shows an error message on the inputLayout
     * @return true if password is valid, false otherwise.
     */
    private boolean validatePassword() {
        if (getInputPassword().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
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

    @Override
    public void onLoginSuccess(User user) {

    }

    @Override
    public void onLoginFailed(String error) {

    }

    @Override
    public void onLoginCanceled() {

    }

    @Override
    public void onLogOutSuccess() {

    }


    public interface OnRegisterFragmentInteractionListener {
        /**
         * Indicates register submit button selected.
         */
        void onRegisterSubmitSelected(User user);
        /**
         * Indicates progress taking place.
         */
        void onShowProgress();
        /**
         * Indicates current progress finished.
         */
        void onDismissProgress();
        /**
         * Triggered to change toolbar title.
         */
        void changeToolbarTitle(String title);

    }

}
