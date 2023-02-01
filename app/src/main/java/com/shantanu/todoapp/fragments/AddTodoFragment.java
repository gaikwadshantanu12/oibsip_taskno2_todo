package com.shantanu.todoapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.shantanu.todoapp.database.tododata.CustomTodoAdapter;
import com.shantanu.todoapp.R;
import com.shantanu.todoapp.RecyclerItemClickListener;
import com.shantanu.todoapp.database.tododata.TodoDAO;
import com.shantanu.todoapp.database.TodoDBHelper;
import com.shantanu.todoapp.database.tododata.TodoDetails;
import com.shantanu.todoapp.database.userdata.UserDAO;
import com.shantanu.todoapp.database.userdata.UserDetails;
import com.shantanu.todoapp.sessions.PreferencesConstants;

import java.util.ArrayList;
import java.util.Objects;

public class AddTodoFragment extends Fragment {

    private TextInputEditText todo_name;
    private RecyclerView recyclerView;
    private AppCompatButton add_new_todo;
    private TodoDAO todo_dao;
    private TodoDBHelper helper;
    private TodoDetails todoDetails;
    private UserDetails userDetails;
    private int user_id;
    private UserDAO userDAO;
    private ArrayList<TodoDetails> todo_array_list;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

        // getting current logged in user id From DashboardActivity to AddTodoFragment
        user_id = requireActivity().getSharedPreferences(PreferencesConstants.PREFS_NAME, Context.MODE_PRIVATE).getInt(PreferencesConstants.PREFS_USER_ID,999);

        /*
        if(user_id == 999) {
            Toast.makeText(getContext(), "user id not received", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(getContext(), String.valueOf(user_id) + " = user id received in AddToDoFragment", Toast.LENGTH_SHORT).show();
         */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_todo, container, false);

        // obtain all ids
        obtainIDS(view);

        // getting all info of current user from user id
        userDetails = userDAO.getUserInfoFromUserID(user_id);

        /* debugging details object
        String debug = "User ID - " + userDetails.getUser_id()+", Name - " + userDetails.getUser_name() + ", Email - " + userDetails.getUser_email() + ", Password - " + userDetails.getUser_password();
        if(userDetails != null)
            Toast.makeText(getContext(), "user details not null, " + debug, Toast.LENGTH_SHORT).show();
         */
        add_new_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });

        /* handling with recycler view */
        handleRecyclerView();

        /* recyclerview click listener */
        recyclerClickListener();

        return view;
    }

    private void obtainIDS(View view) {
        todo_name = view.findViewById(R.id.todo_name);
        add_new_todo = view.findViewById(R.id.add_new_todo);
        recyclerView = view.findViewById(R.id.recycler_view);

        helper = new TodoDBHelper(getContext());
        todo_dao = new TodoDAO(helper);
        todoDetails = new TodoDetails();
        userDAO = new UserDAO(helper);
    }

    private void validation() {
        String todo = Objects.requireNonNull(todo_name.getText()).toString().trim();

        if(todo.equals("")) {
            Toast.makeText(getContext(), "Please provide todo name", Toast.LENGTH_SHORT).show();
        }
        else {
            todoDetails.setTodo_title(todo);
            todoDetails.setUser_id(userDetails.getUser_id());

            boolean res = todo_dao.insertNewTODO(todoDetails);
            if(res) {
                Toast.makeText(getContext(), "Todo inserted into database", Toast.LENGTH_SHORT).show();
                todo_name.setText("");
                handleRecyclerView();
            }
            else
                Toast.makeText(getContext(), "Todo not inserted into database", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleRecyclerView() {
        todo_array_list = todo_dao.getAllTodoOfParticularUser(user_id);
        CustomTodoAdapter adapter = new CustomTodoAdapter(todo_array_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void recyclerClickListener(){
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Toast.makeText(getContext(), todo_array_list.get(position).getTodo_title(), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("TODO Operation");
                builder.setMessage("What you want to do with selected TODO ?");
                builder.setPositiveButton("Edit TODO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateTodo(position);
                    }
                });
                builder.setNegativeButton("Delete TODO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTodo(position);
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

    private void deleteTodo(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Are you sure ?");
        builder.setMessage("Do you really want to delete it ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes, Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean res = todo_dao.deleteTodo(todo_array_list.get(position).getTodo_id());

                if(res)
                    Toast.makeText(getContext(),"TODO successfully deleted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "TODO not deleted", Toast.LENGTH_SHORT).show();

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
    private void updateTodo(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        View custom_layout = getLayoutInflater().inflate(R.layout.custom_alert_dialog_layout_for_todo, null);
        builder.setView(custom_layout);

        TextInputEditText todo = custom_layout.findViewById(R.id.todo_name);
        todo.setText(todo_array_list.get(position).getTodo_title());

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(Objects.requireNonNull(todo.getText()).toString().trim().equals("")) {
                    Toast.makeText(getContext(), "Please enter todo title", Toast.LENGTH_SHORT).show();
                }
                else {
                    todoDetails.setTodo_title(todo.getText().toString().trim());
                    todoDetails.setUser_id(userDetails.getUser_id());

                    boolean res = todo_dao.updateTodo(todoDetails,todo_array_list.get(position).getTodo_id());

                    if(res)
                        Toast.makeText(getContext(),"TODO successfully updated", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), "TODO not updated", Toast.LENGTH_SHORT).show();

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