package com.shantanu.todoapp.database.notesdata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shantanu.todoapp.R;
import com.shantanu.todoapp.database.tododata.CustomTodoAdapter;
import com.shantanu.todoapp.database.tododata.TodoDetails;

import java.util.ArrayList;

public class CustomNotesAdapter extends RecyclerView.Adapter<CustomNotesAdapter.ViewHolder>{

    private ArrayList<NotesDetails> notes_list;

    public CustomNotesAdapter(ArrayList<NotesDetails> notes_list) {
        this.notes_list = notes_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View list_item = inflater.inflate(R.layout.recycler_view_notes_single_item, parent, false);
        ViewHolder holder = new ViewHolder(list_item);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomNotesAdapter.ViewHolder holder, int position) {
        holder.notes_title.setText(notes_list.get(position).getNotes_title());
        holder.notes_description.setText(notes_list.get(position).getNotes_description());
    }

    @Override
    public int getItemCount() {
        return notes_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView notes_title, notes_description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notes_title = itemView.findViewById(R.id.notes_title);
            notes_description = itemView.findViewById(R.id.notes_description);
        }
    }
}
