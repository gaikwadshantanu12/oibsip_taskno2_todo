package com.shantanu.todoapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.shantanu.todoapp.R;
import com.shantanu.todoapp.RecyclerItemClickListener;
import com.shantanu.todoapp.database.TodoDBHelper;
import com.shantanu.todoapp.database.notesdata.CustomNotesAdapter;
import com.shantanu.todoapp.database.notesdata.NotesDAO;
import com.shantanu.todoapp.database.notesdata.NotesDetails;
import com.shantanu.todoapp.database.userdata.UserDAO;
import com.shantanu.todoapp.database.userdata.UserDetails;
import com.shantanu.todoapp.sessions.PreferencesConstants;
import java.util.ArrayList;
import java.util.Objects;

public class NotesFragment extends Fragment {

    private ExtendedFloatingActionButton add_fab;
    private int user_id;
    private TodoDBHelper helper;
    private UserDetails userDetails;
    private NotesDAO notesDAO;
    private UserDAO userDAO;
    private NotesDetails notesDetails;
    private RecyclerView recyclerView;
    private ArrayList<NotesDetails> notes_array_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        // obtain id
        obtainID(view);

        user_id = requireActivity().getSharedPreferences(PreferencesConstants.PREFS_NAME, Context.MODE_PRIVATE).getInt(PreferencesConstants.PREFS_USER_ID,999);

        userDetails = userDAO.getUserInfoFromUserID(user_id);

        // add_fab onclick listener
        fabButtonClicked();

        /* handle recycler view */
        handleRecyclerView();

        /* recyclerview click listener */
        recyclerClickListener();

        return view;
    }

    private void obtainID(View view) {
        add_fab = view.findViewById(R.id.add_fab);
        recyclerView = view.findViewById(R.id.recycler_view);
        helper = new TodoDBHelper(getContext());
        notesDAO = new NotesDAO(helper);
        userDAO = new UserDAO(helper);
        notesDetails = new NotesDetails();
    }

    private void handleRecyclerView() {
        notes_array_list = notesDAO.getAllNotesOfParticularUser(user_id);
        CustomNotesAdapter adapter = new CustomNotesAdapter(notes_array_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void fabButtonClicked() {
        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNotes();
            }
        });
    }

    /* recycler view listener */
    private void recyclerClickListener(){
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Toast.makeText(getContext(), todo_array_list.get(position).getTodo_title(), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Notes Operation");
                builder.setMessage("What you want to do with selected note ?");
                builder.setPositiveButton("Edit Note", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateNote(position);
                    }
                });
                builder.setNegativeButton("Delete Note", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteNote(position);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    /* save notes using dialog box */
    private void saveNotes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        View custom_layout = getLayoutInflater().inflate(R.layout.custom_alert_dialog_layout_for_notes, null);
        builder.setView(custom_layout);

        TextInputEditText title = custom_layout.findViewById(R.id.notes_title);
        TextInputEditText description = custom_layout.findViewById(R.id.notes_description);
        TextView main_title = custom_layout.findViewById(R.id.main_title);
        main_title.setText("Add New Notes");

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(Objects.requireNonNull(title.getText()).toString().trim().equals("") || Objects.requireNonNull(description.getText()).toString().trim().equals("")) {
                    Toast.makeText(getContext(), "Please enter title & description", Toast.LENGTH_SHORT).show();
                }
                else {
                    notesDetails.setNotes_title(title.getText().toString().trim());
                    notesDetails.setNotes_description(description.getText().toString().trim());
                    notesDetails.setUser_id(userDetails.getUser_id());
                    boolean res = notesDAO.insertNewNotes(notesDetails);

                    if(res)
                        Toast.makeText(getContext(),"Notes successfully inserted", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), "Notes not added", Toast.LENGTH_SHORT).show();

                    handleRecyclerView();
                }
            }
        });
        builder.setNegativeButton("Abort", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /* delete notes using dialog box */
    private void deleteNote(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Are you sure ?");
        builder.setMessage("Do you really want to delete it ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean res = notesDAO.deleteNotesByID(notes_array_list.get(position).getNotes_id());

                if(res)
                    Toast.makeText(getContext(),"Notes successfully deleted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "Notes not deleted", Toast.LENGTH_SHORT).show();

                handleRecyclerView();
            }
        });

        builder.setNegativeButton("No, Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /* update note using dialog box */
    private void updateNote(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        View custom_layout = getLayoutInflater().inflate(R.layout.custom_alert_dialog_layout_for_notes, null);
        builder.setView(custom_layout);

        TextInputEditText title = custom_layout.findViewById(R.id.notes_title);
        TextInputEditText description = custom_layout.findViewById(R.id.notes_description);
        TextView main_title = custom_layout.findViewById(R.id.main_title);
        main_title.setText("Update Notes");
        title.setText(notes_array_list.get(position).getNotes_title());
        description.setText(notes_array_list.get(position).getNotes_description());

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(Objects.requireNonNull(title.getText()).toString().trim().equals("") || Objects.requireNonNull(description.getText()).toString().trim().equals("")) {
                    Toast.makeText(getContext(), "Please enter title & description", Toast.LENGTH_SHORT).show();
                }
                else {
                    notesDetails.setNotes_title(title.getText().toString().trim());
                    notesDetails.setNotes_description(description.getText().toString().trim());
                    notesDetails.setUser_id(userDetails.getUser_id());
                    boolean res = notesDAO.updateNotes(notesDetails,notes_array_list.get(position).getNotes_id());

                    if(res)
                        Toast.makeText(getContext(),"Notes successfully updated", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), "Notes not updated", Toast.LENGTH_SHORT).show();

                    handleRecyclerView();
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