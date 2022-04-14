package com.example.andoridnotes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesViewHolder extends RecyclerView.ViewHolder {

    TextView NoteTitle;
    TextView NoteDesc;
    TextView NoteDate;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        NoteTitle = itemView.findViewById(R.id.txtNoteTitle);
        NoteDesc = itemView.findViewById(R.id.txtNotesDesc);
        NoteDate = itemView.findViewById(R.id.txtNotesDate);
    }
}
