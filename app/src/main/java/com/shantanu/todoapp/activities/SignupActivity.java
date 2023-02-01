package com.shantanu.todoapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.shantanu.todoapp.R;
import com.shantanu.todoapp.database.TodoDBHelper;
import com.shantanu.todoapp.database.userdata.UserDAO;
import com.shantanu.todoapp.database.userdata.UserDetails;
import com.shantanu.todoapp.sessions.PreferencesConstants;

import java.util.Objects;

public class SignupActivity extends AppCompatActivity {
    private TextView move_to_login;
    private TextInputEditText et_user_name, et_user_email, et_user_password, et_user_confirm_password;
    private AppCompatButton signup_button;
    private UserDAO dao;
    private SharedPreferences.Editor editor;
    private UserDetails details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // change activity title
        Objects.requireNonNull(getSupportActionBar()).setTitle("Create New Account");

        // obtain id
        obtainID();

        // initialize db
        TodoDBHelper helper = new TodoDBHelper(getApplicationContext());
        dao = new UserDAO(helper);

        // initialize preferences
        editor = getSharedPreferences(PreferencesConstants.PREFS_NAME, Context.MODE_PRIVATE).edit();

        move_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
    }

    private void obtainID(){
        move_to_login = findViewById(R.id.move_to_login);
        et_user_name = findViewById(R.id.user_name);
        et_user_email = findViewById(R.id.user_email);
        et_user_password = findViewById(R.id.user_password);
        et_user_confirm_password = findViewById(R.id.user_confirm_password);
        signup_button = findViewById(R.id.signup_button);
    }

    private void validation(){
        String user_name = Objects.requireNonNull(et_user_name.getText()).toString().trim();
        String user_email = Objects.requireNonNull(et_user_email.getText()).toString().trim();
        String user_password = Objects.requireNonNull(et_user_password.getText()).toString().trim();
        String user_confirm_password = Objects.requireNonNull(et_user_confirm_password.getText()).toString().trim();

        if(Objects.equals(user_name, "") || Objects.equals(user_email, "") || Objects.equals(user_password, "") || user_confirm_password.equals("")) {
            Toast.makeText(getApplicationContext(),"Please enter all fields", Toast.LENGTH_SHORT).show();
        }
        else if(user_password.length() <= 12) {
            Toast.makeText(getApplicationContext(),"Password must be at least 12 chars", Toast.LENGTH_SHORT).show();
        }
        else if(!Objects.equals(user_password, user_confirm_password)) {
            Toast.makeText(getApplicationContext(), "Password & confirm password not matched !", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(user_email).matches()) {
            Toast.makeText(getApplicationContext(),"Please enter email in proper format", Toast.LENGTH_SHORT).show();
        }
        else {
            /* insert new data or register new user */
            details = new UserDetails();
            details.setUser_name(user_name);
            details.setUser_email(user_email);
            details.setUser_password(user_password);

            boolean res = dao.insertNewUser(details);
            if(res) {
                Toast.makeText(getApplicationContext(), "New user successfully registered. And you may login to your account !", Toast.LENGTH_SHORT).show();

                // add data to preferences for session management
//                editor.putString(PreferencesConstants.PREFS_USER_NAME, details.getUser_name());
//                editor.putString(PreferencesConstants.PREFS_USER_EMAIL, details.getUser_email());
//                editor.commit();

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
            else
                Toast.makeText(getApplicationContext(), "New user not added !", Toast.LENGTH_SHORT).show();
        }
    }
}