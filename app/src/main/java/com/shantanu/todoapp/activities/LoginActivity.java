package com.shantanu.todoapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {

    private TextView move_to_signup;
    private TextInputEditText et_user_email, et_user_password;
    private AppCompatButton login_button;
    private UserDetails details;
    private UserDAO dao;
    private TodoDBHelper helper;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // change activity title
        Objects.requireNonNull(getSupportActionBar()).setTitle("Log In");

        // obtain all IDS
        obtainID();

        editor = (SharedPreferences.Editor) getSharedPreferences(PreferencesConstants.PREFS_NAME, Context.MODE_PRIVATE).edit();

        // initialize database
        helper = new TodoDBHelper(getApplicationContext());
        dao = new UserDAO(helper);

        move_to_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
    }

    private void obtainID(){
        move_to_signup = findViewById(R.id.move_to_signup);
        et_user_email = findViewById(R.id.user_email);
        et_user_password = findViewById(R.id.user_password);
        login_button = findViewById(R.id.login_button);
    }

    public void validation() {
        String user_email = Objects.requireNonNull(et_user_email.getText()).toString().trim();
        String user_password = Objects.requireNonNull(et_user_password.getText()).toString().trim();

        if(user_email.equals("") || user_password.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter email & password", Toast.LENGTH_SHORT).show();
        }
        else {
            details = new UserDetails();
            details.setUser_email(user_email);
            details.setUser_password(user_password);

            UserDetails det = dao.loginUser(details);

            if(det == null)
                Toast.makeText(getApplicationContext(), "Make sure you have already signup or check the details !", Toast.LENGTH_SHORT).show();
            else {
                // debugging
                // String debug = "User ID - " + det.getUser_id()+", Name - " + det.getUser_name() + ", Email - " + det.getUser_email() + ", Password - " + det.getUser_password();
                // Toast.makeText(getApplicationContext(), debug, Toast.LENGTH_SHORT).show();

                // add data to preferences for session management
                editor.putString(PreferencesConstants.PREFS_USER_NAME, det.getUser_name());
                editor.putString(PreferencesConstants.PREFS_USER_EMAIL, det.getUser_email());
                editor.putInt(PreferencesConstants.PREFS_USER_ID, det.getUser_id());
                editor.commit();

                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                finish();
            }
        }
    }
}