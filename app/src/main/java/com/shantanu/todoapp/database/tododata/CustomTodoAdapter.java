package com.shantanu.todoapp.database.tododata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.shantanu.todoapp.R;
import java.util.ArrayList;

public class CustomTodoAdapter extends RecyclerView.Adapter<CustomTodoAdapter.ViewHolder> {

    private ArrayList<TodoDetails> todo_list;

    public CustomTodoAdapter(ArrayList<TodoDetails> todo_list) {
        this.todo_list = todo_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View list_item = inflater.inflate(R.layout.recycler_view_todo_single_item, parent, false);
        ViewHolder holder = new ViewHolder(list_item);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomTodoAdapter.ViewHolder holder, int position) {
        holder.todo_name.setText(todo_list.get(position).getTodo_title());
    }

    @Override
    public int getItemCount() {
        return todo_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView todo_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            todo_name = itemView.findViewById(R.id.todo_name);
        }
    }
}
