package com.example.andoridnotes;

import android.util.JsonWriter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class Notes implements Serializable, Comparable<Notes> {
    private final String notesTitle;
    private final String notesDescription;
    private Date notesLastUpdatedTime;

    Notes(String notesTitle, String notesDescription) {
        this.notesTitle = notesTitle;
        this.notesDescription = notesDescription;
        this.notesLastUpdatedTime = new Date();
    }

    public String getNotesTitle() {
        return notesTitle;
    }

    public String getNotesTitle80Char(){
        if (this.notesTitle.length() > 80) {
            return String.format("%."+ 80 +"s...", this.notesTitle);
        } else {
            return this.notesTitle;
        }
    }

    public String getNotesDescription() {
        return notesDescription;
    }

    public String getNotesDescription80Char(){
        if (this.notesDescription.length() > 80) {
            return String.format("%."+ 80 +"s...", this.notesDescription);
        } else {
            return this.notesDescription;
        }
    }

    public Date getNotesLastUpdatedTime(){
        return notesLastUpdatedTime;
    }

    public void SetLastUpdatedTimeFormatted(String dateString) throws ParseException {
            SimpleDateFormat formatter = new SimpleDateFormat("E MMM dd, hh:mm aa", Locale.ENGLISH);
            Date date = formatter.parse(dateString);
            this.notesLastUpdatedTime = date;
    }

    public String getLastUpdatedTimeFormatted() {
        DateFormat dateFormat = new SimpleDateFormat("E MMM dd, hh:mm aa", Locale.US);
        return dateFormat.format(this.notesLastUpdatedTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Notes notes = (Notes) o;
        return  notesTitle.equals(notes.getNotesTitle()) &&
                notesDescription.equals(notes.getNotesDescription());
    }

    @NonNull
    public String toString() {

        try {
            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.setIndent("  ");
            jsonWriter.beginObject();
            jsonWriter.name("title").value(getNotesTitle());
            jsonWriter.name("description").value(getNotesDescription());
            jsonWriter.endObject();
            jsonWriter.close();
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public int compareTo(Notes notes) {
        return notes.getNotesLastUpdatedTime().compareTo(getNotesLastUpdatedTime());
    }

    static class SortByDate implements Comparator<Notes> {
        @Override
        public int compare(Notes a, Notes b) {
            return a.notesLastUpdatedTime.compareTo(b.notesLastUpdatedTime);
        }
    }
}

