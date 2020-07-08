package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "AppCompatActivity";
    EditText etEmail;
    EditText etUsername;
    EditText etPassword;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // get layout items
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsernameSU);
        etPassword = findViewById(R.id.etPasswordSU);
        btnSignUp = findViewById(R.id.btSignUpSU);

        // set signup action when signup button is clicked
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Signup button clicked");
                String email = etEmail.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                signUpUser(email, username, password);
            }
        });
    }

    private void signUpUser(String email, final String username, final String password) {
        // create ParseUser
        ParseUser user = new ParseUser();
        // set properties
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);

        // sign up user in background
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // signup was successful, login user and navigate to main activity
                    Toast.makeText(SignUpActivity.this, "signup successfull!", Toast.LENGTH_SHORT);
                    loginUser(username, password);

                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Log.e(TAG, "Issue with login", e);
                    return;
                }
            }
        });

    }

    // attempts to log in user with provided information
    private void loginUser(String username, String password) {
        Log.i(TAG, "attempting to login user " + username);
        // logs in on background thread instead of main thread or UI thread
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null) {
                    // to do: more specific error handling
                    Log.e(TAG, "Issue with login", e);
                    return;
                }
                // if username and password are correct, navigate to main activity
                goMainActivity();
                Toast.makeText(SignUpActivity.this, "login success!", Toast.LENGTH_SHORT);
            }
        });
    }

    // navigate to main activity
    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        // end activity once you've logged in (i.e. you can't go back to it)
        finish();
    }
}