package com.shantanu.todoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.shantanu.todoapp.activities.DashboardActivity;
import com.shantanu.todoapp.activities.SignupActivity;
import com.shantanu.todoapp.sessions.PreferencesConstants;

public class MainActivity extends AppCompatActivity {
    private AppCompatButton start_journey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start_journey = findViewById(R.id.start_journey);
        start_journey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sessionManagement();
    }

    @Override
    protected void onStart() {
        super.onStart();
        sessionManagement();
    }

    private void sessionManagement(){
        SharedPreferences preferences = getSharedPreferences(PreferencesConstants.PREFS_NAME, Context.MODE_PRIVATE);
        String email = preferences.getString(PreferencesConstants.PREFS_USER_EMAIL, null);
        String name = preferences.getString(PreferencesConstants.PREFS_USER_NAME, null);

        if(email != null && name != null) {
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            finish();
        }
    }
}