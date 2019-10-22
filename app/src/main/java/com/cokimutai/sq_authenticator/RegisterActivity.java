package com.cokimutai.sq_authenticator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cokimutai.sq_authenticator.data.UserDbHelper;
import com.cokimutai.sq_authenticator.utils.ValidateUserInput;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity implements
        View.OnClickListener {

    private final AppCompatActivity activity = RegisterActivity.this;

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;

    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;

    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;

    private ValidateUserInput validateUserInput;
    private UserDbHelper db;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        locateTheViews();
        setListeners();
        initObjects();
    }
    
    private void locateTheViews() {
        textInputLayoutName = findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword =  findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = findViewById(R.id.textInputLayoutConfirmPassword);

        textInputEditTextName =   findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail =   findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword =   findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword =   findViewById(R.id.textInputEditTextConfirmPassword);

        appCompatButtonRegister =  findViewById(R.id.appCompatButtonRegister);

        appCompatTextViewLoginLink = findViewById(R.id.appCompatTextViewLoginLink);

    }

    private void setListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);

    }
    private void initObjects() {
        validateUserInput = new ValidateUserInput(activity);
        db = new UserDbHelper(activity);
        user = new User();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appCompatButtonRegister:
                postDataToSQLite();
                break;

            case R.id.appCompatTextViewLoginLink:
                finish();
                break;
        }
    }

    private void postDataToSQLite() {
        if (!validateUserInput.isInputEditTextFilled(textInputEditTextName, textInputLayoutName,
                getString(R.string.name_err_msg))) {
            return;
        }
        if (!validateUserInput.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail,
                getString(R.string.email_err_msg))) {
            return;
        }
        if (!validateUserInput.isInputEditTextEmailValid(textInputEditTextEmail, textInputLayoutEmail,
                getString(R.string.email_err_msg))) {
            return;
        }
        if (!validateUserInput.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword,
                getString(R.string.password_err_msg))) {
            return;
        }
        if (!validateUserInput.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.password_match_err_msg))) {
            return;
        }

        if (!db.checkUser(textInputEditTextEmail.getText().toString().trim())) {

            user.setName(textInputEditTextName.getText().toString().trim());
            user.setEmail(textInputEditTextEmail.getText().toString().trim());
            user.setPassword(textInputEditTextPassword.getText().toString().trim());

            db.addUser(user);

            Toast.makeText(this, getString(R.string.registration_successful_msg), Toast.LENGTH_LONG).show();
            exitRegistration();


        } else {
            Toast.makeText(this, getString(R.string.email_exists_err_msg), Toast.LENGTH_LONG).show();
        }


    }

    private void exitRegistration() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
