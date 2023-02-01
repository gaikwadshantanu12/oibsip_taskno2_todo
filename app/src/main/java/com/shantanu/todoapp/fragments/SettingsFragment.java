package com.shantanu.todoapp.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.shantanu.todoapp.R;
import com.shantanu.todoapp.activities.LoginActivity;
import com.shantanu.todoapp.database.TodoDBHelper;
import com.shantanu.todoapp.database.notesdata.NotesDAO;
import com.shantanu.todoapp.database.tododata.TodoDAO;
import com.shantanu.todoapp.database.userdata.UserDAO;
import com.shantanu.todoapp.database.userdata.UserDetails;
import com.shantanu.todoapp.sessions.PreferencesConstants;

public class SettingsFragment extends Fragment {

    private AppCompatButton logout, delete_account;
    private SharedPreferences.Editor editor;
    private int user_id;
    private UserDetails userDetails;
    private UserDAO userDAO;
    private TodoDBHelper helper;
    private TextInputEditText your_name, your_email;
    private TodoDAO todoDAO;
    private NotesDAO notesDAO;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // obtain ids
        obtainID(view);

        // get current user logged in user id
        user_id = requireActivity().getSharedPreferences(PreferencesConstants.PREFS_NAME, Context.MODE_PRIVATE).getInt(PreferencesConstants.PREFS_USER_ID,999);

        // get current user logged in details
        userDetails = userDAO.getUserInfoFromUserID(user_id);

        // set data
        setDataToTextField();

        // logout current user & expire session
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString(PreferencesConstants.PREFS_USER_NAME, null);
                editor.putString(PreferencesConstants.PREFS_USER_EMAIL, null);
                editor.commit();

                startActivity(new Intent(requireActivity().getApplicationContext(), LoginActivity.class));
            }
        });

        // delete account forever
        delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccountForever();
            }
        });

        return view;
    }

    private void obtainID(View view) {
        helper = new TodoDBHelper(getContext());
        userDAO = new UserDAO(helper);
        notesDAO = new NotesDAO(helper);
        todoDAO = new TodoDAO(helper);
        progressDialog = new ProgressDialog(getContext());

        your_name = view.findViewById(R.id.your_name);
        your_email = view.findViewById(R.id.your_email);
        logout = view.findViewById(R.id.logout);
        delete_account = view.findViewById(R.id.delete_account);

        editor = (SharedPreferences.Editor) requireActivity().getSharedPreferences(PreferencesConstants.PREFS_NAME, Context.MODE_PRIVATE).edit();
    }

    private void setDataToTextField() {
        your_name.setText(userDetails.getUser_name());
        your_email.setText(userDetails.getUser_email());
    }

    private void deleteAccountForever() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Warning !!!");
        builder.setMessage("On deleting your account, you will lost all your notes & daily noted task !!");
        builder.setPositiveButton("Delete Forever", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.setTitle("Please Wait");
                progressDialog.setMessage("Deleting all your TODO & Notes");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                if(todoDAO.deleteAllTODOOfParticularUser(userDetails.getUser_id()) &&
                        notesDAO.deleteAllNotesOfParticularUser(userDetails.getUser_id()) &&
                        userDAO.deleteUserByID(userDetails.getUser_id())) {
                    Toast.makeText(getContext(), "Deleted everything", Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                    editor.putString(PreferencesConstants.PREFS_USER_NAME, null);
                    editor.putString(PreferencesConstants.PREFS_USER_EMAIL, null);
                    editor.commit();
                    startActivity(new Intent(requireActivity().getApplicationContext(), LoginActivity.class));
                }
                else {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}