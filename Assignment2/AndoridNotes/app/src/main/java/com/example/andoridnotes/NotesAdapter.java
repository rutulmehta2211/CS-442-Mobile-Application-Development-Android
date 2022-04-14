package com.example.andoridnotes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    private static final String TAG = "NotesAdapter";
    private final List<Notes> notesList;
    private final MainActivity mainAct;

    NotesAdapter(List<Notes> notesList, MainActivity ma) {
        this.notesList = notesList;
        mainAct = ma;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW MyViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new NotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: FILLING VIEW HOLDER Employee " + position);

        Notes notes = notesList.get(position);

        holder.NoteTitle.setText(notes.getNotesTitle80Char());
        holder.NoteDesc.setText(notes.getNotesDescription80Char());
        holder.NoteDate.setText(notes.getLastUpdatedTimeFormatted());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
