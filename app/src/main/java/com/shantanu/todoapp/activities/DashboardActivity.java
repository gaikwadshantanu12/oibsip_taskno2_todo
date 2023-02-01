package com.shantanu.todoapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.shantanu.todoapp.R;
import com.shantanu.todoapp.database.userdata.UserDetails;
import com.shantanu.todoapp.fragments.AddTodoFragment;
import com.shantanu.todoapp.fragments.NotesFragment;
import com.shantanu.todoapp.fragments.SettingsFragment;
import com.shantanu.todoapp.sessions.PreferencesConstants;

import java.util.Objects;

public class DashboardActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // change activity title
        Objects.requireNonNull(getSupportActionBar()).setTitle("User Dashboard");

        // get user id of current user from LoginActivity
        user_id = getSharedPreferences(PreferencesConstants.PREFS_NAME,Context.MODE_PRIVATE).getInt(PreferencesConstants.PREFS_USER_ID,999);

        // debugging user_id
        /*
        if(user_id == 999) {
            Toast.makeText(getApplicationContext(), "user id not received", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getApplicationContext(), String.valueOf(user_id) + " = user id received in DashboardActivity", Toast.LENGTH_SHORT).show();
         */

        navigationView = findViewById(R.id.bottom_navigation_view);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new AddTodoFragment()).commit();

        navigationView.setSelectedItemId(R.id.todo);

        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.todo: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new AddTodoFragment()).commit();
                        return true;
                    }

                    case R.id.settings: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new SettingsFragment()).commit();
                        return true;
                    }

                    case R.id.notes: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new NotesFragment()).commit();
                        return true;
                    }
                }

                return false;
            }
        });
    }
}