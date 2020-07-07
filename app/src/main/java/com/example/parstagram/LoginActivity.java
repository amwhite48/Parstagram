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

// class to manage activity of logging in
// activity that launches when app launches
public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private EditText etUsername;
    private EditText etPassword;
    private Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // if someone is logged in, navigate to main activity instead of login activity
        if(ParseUser.getCurrentUser() != null) {
            goMainActivity();
        }

        // get layout items
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btLogin);

        // set actions when button is clicked
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Login button clicked");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
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
                Toast.makeText(LoginActivity.this, "login success!", Toast.LENGTH_SHORT);
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