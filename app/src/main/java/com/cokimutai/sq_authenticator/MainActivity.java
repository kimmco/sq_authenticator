package com.cokimutai.sq_authenticator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cokimutai.sq_authenticator.data.UserDbHelper;
import com.cokimutai.sq_authenticator.utils.ValidateUserInput;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener{
    private final AppCompatActivity activity = MainActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonLogin;

    private AppCompatTextView textViewLinkRegister;

    private ValidateUserInput validateUserInput;
    private UserDbHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locateTheViews();
        setListeners();
        initObjects();
    }

    private void setListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    private void locateTheViews() {
        nestedScrollView = findViewById(R.id.nestedScrollView);

        textInputLayoutEmail =   findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword =   findViewById(R.id.textInputLayoutPassword);

        textInputEditTextEmail =   findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword =   findViewById(R.id.textInputEditTextPassword);

        appCompatButtonLogin =  findViewById(R.id.appCompatButtonLogin);

        textViewLinkRegister = findViewById(R.id.textViewLinkRegister);

    }
    private void initObjects() {
        db = new UserDbHelper(activity);
        validateUserInput = new ValidateUserInput(activity);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.appCompatButtonLogin:
                validateFromDb();
                break;
            case  R.id.textViewLinkRegister:
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                break;
        }

    }

    private void validateFromDb() {
        if (!validateUserInput.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.email_err_msg))) {
            return;
        }
        if (!validateUserInput.isInputEditTextEmailValid(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.email_err_msg))) {
            return;
        }
        if (!validateUserInput.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.email_err_msg))) {
            return;
        }

        if (db.checkUser(textInputEditTextEmail.getText().toString().trim()
                , textInputEditTextPassword.getText().toString().trim())) {


            Intent accountsIntent = new Intent(activity, UsersRegisteredActivity.class);
            accountsIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
            clearInputEditText();
            startActivity(accountsIntent);


        } else {

            Snackbar.make(nestedScrollView, getString(R.string.invalid_email_password_err_msg), Snackbar.LENGTH_LONG).show();
        }
    }
    private void clearInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }
}