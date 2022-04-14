package com.example.andoridnotes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private RecyclerView recyclerNotes;
    private NotesAdapter NotesAdapter;

    private static final String TAG = "MainActivity";
    private Notes objNotes;
    private ArrayList<Notes> notesList = new ArrayList<>();
    private ActivityResultLauncher<Intent> editActivityResultLauncher;
    private ActivityResultLauncher<Intent> editActivityResultLauncherForEditList;
    int Global_position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleEditActivityResult);
        editActivityResultLauncherForEditList = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleEditActivityResultForEditList);

        recyclerNotes = findViewById(R.id.recyclerNotes);
        if(!notesList.isEmpty()){
            notesList.clear();
        }
        notesList = loadFile();
        displayNotesList();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume Event");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause Event");
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opt_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.opt_add) {
            Intent intent = new Intent(this, EditActivity.class);
            editActivityResultLauncher.launch(intent);
        } else if (item.getItemId() == R.id.opt_info) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, EditActivity.class);
        int position = recyclerNotes.getChildLayoutPosition(view);
        Global_position = position;
        Notes n = notesList.get(position);
        intent.putExtra(Intent.EXTRA_TEXT,n);
        editActivityResultLauncherForEditList.launch(intent);
    }

    @Override
    public boolean onLongClick(View view) {
        int position = recyclerNotes.getChildLayoutPosition(view);
        Notes n = notesList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton(getString(R.string.ok_confirm_button), (dialog, id) -> {
            notesList.remove(n);
            Collections.sort(notesList);
            displayNotesList();
            saveNotes();
            dialog.dismiss();
        });
        builder.setNegativeButton(getString(R.string.cancel_confirm_button), (dialog, id) -> {
            dialog.dismiss();
        });

        builder.setTitle(getString(R.string.delete_note_title) +" '"+n.getNotesTitle()+"'?");

        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void displayNotesList(){
        NotesAdapter = new NotesAdapter(notesList, this);
        recyclerNotes.setAdapter(NotesAdapter);
        recyclerNotes.setLayoutManager(new LinearLayoutManager(this));
        NotesAdapter.notifyDataSetChanged();
        updateNotesCount();
    }

    public void handleEditActivityResult(ActivityResult result) {

        if (result == null || result.getData() == null) {
            Log.d(TAG, "handleEditActivityResult: NULL ActivityResult received");
            return;
        }

        Intent data = result.getData();
        if (result.getResultCode() == RESULT_OK) {
            objNotes = (Notes) data.getSerializableExtra("NOTE_OBJECT");

            if (objNotes == null) {
                Toast.makeText(this, "Note object is null", Toast.LENGTH_SHORT).show();
                return;
            }
            notesList.add(objNotes);
            Collections.sort(notesList);
            displayNotesList();
            saveNotes();
            Log.d(TAG, "handleEditActivityResult: Note object: " + objNotes);
        } else {
            Log.d(TAG, "handleEditActivityResult: result Code: " + result.getResultCode());
        }
    }

    public void handleEditActivityResultForEditList(ActivityResult result) {

        if (result == null || result.getData() == null) {
            Log.d(TAG, "handleEditActivityResult: NULL ActivityResult received");
            return;
        }

        Intent data = result.getData();
        if (result.getResultCode() == RESULT_OK) {
            objNotes = (Notes) data.getSerializableExtra("NOTE_OBJECT");

            if (objNotes == null) {
                Toast.makeText(this, "Note object is null", Toast.LENGTH_SHORT).show();
                return;
            }
            Notes objNotesForUpdate = notesList.get(Global_position);
            if(!objNotesForUpdate.equals(objNotes)) {
                int index = notesList.indexOf(objNotesForUpdate);
                notesList.set(index, objNotes);
                Collections.sort(notesList);
                displayNotesList();
                saveNotes();
            }
            Global_position=0;
            Log.d(TAG, "handleEditActivityResult: Note object: " + objNotes);
        } else {
            Log.d(TAG, "handleEditActivityResult: result Code: " + result.getResultCode());
        }
    }

    private void saveNotes() {
        try {
            FileOutputStream fileOutputStream = getApplicationContext().
                    openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fileOutputStream, "UTF-8"));
            writer.setIndent("  ");
            writer.beginArray();
            for (Notes objNotes : notesList) {
                writer.beginObject();
                writer.name("title").value(objNotes.getNotesTitle());
                writer.name("description").value(objNotes.getNotesDescription());
                writer.name("datetime").value(objNotes.getLastUpdatedTimeFormatted());
                writer.endObject();
            }
            writer.endArray();
            writer.close();
            fileOutputStream.close();
            Log.d(TAG, "saveNotes: JSON:\n" + notesList.toString());
            //Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private ArrayList<Notes> loadFile() {

        Log.d(TAG, "loadFile: Loading JSON File");
        ArrayList<Notes> nl = new ArrayList<>();
        try {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            Log.d(TAG, "String builder: "+sb.toString());
            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                String desc = jsonObject.getString("description");
                String datetime = jsonObject.getString("datetime");
                Notes notes = new Notes(title,desc);
                notes.SetLastUpdatedTimeFormatted(datetime);
                nl.add(notes);
            }

        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nl;
    }

    private void updateNotesCount() {
        int notesCount = 0;
        if (!notesList.isEmpty()) {
            notesCount = notesList.size();
        }
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.app_name) + " (" + notesCount + ")");
    }
}