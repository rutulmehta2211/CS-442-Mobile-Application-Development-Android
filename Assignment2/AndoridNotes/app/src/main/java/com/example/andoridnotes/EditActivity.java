package com.example.andoridnotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    private static final String TAG = "EditActivity";
    private EditText txtNoteTitleEditActivity;
    private EditText txtNoteDescEditActivity;
    ArrayList<Notes> notesList = new ArrayList<>();
    Notes editNoteObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        txtNoteTitleEditActivity = findViewById(R.id.txtNoteTitleEditActivity);
        txtNoteDescEditActivity = findViewById(R.id.txtNoteDescEditActivity);

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            editNoteObject = (Notes)intent.getSerializableExtra(Intent.EXTRA_TEXT);
            txtNoteTitleEditActivity.setText(editNoteObject.getNotesTitle());
            txtNoteDescEditActivity.setText(editNoteObject.getNotesDescription());
        }
    }

    @Override
    public void onBackPressed() {
        String title = txtNoteTitleEditActivity.getText().toString();
        String desc = txtNoteDescEditActivity.getText().toString();
        if(!title.isEmpty()) {
            if (!editNoteObject.equals(new Notes(title, desc))) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setPositiveButton(getString(R.string.yes_confirm_button), (dialog, id) -> {
                    Notes n = new Notes(title,desc);
                    Intent data = new Intent();
                    data.putExtra("NOTE_OBJECT", n);
                    setResult(RESULT_OK, data);
                    finish();
                    dialog.dismiss();
                });
                builder.setNegativeButton(getString(R.string.no_confirm_button), (dialog, id) -> {
                    dialog.dismiss();
                });

                builder.setTitle(getString(R.string.confirm_alert_title));
                builder.setMessage(getString(R.string.confirm_alert_message)+" '"+title+"'?");

                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else{
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setPositiveButton(getString(R.string.ok_confirm_button), (dialog, id) -> {
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
                dialog.dismiss();
            });
            builder.setNegativeButton(getString(R.string.cancel_confirm_button), (dialog, id) -> {
                dialog.dismiss();
            });

            builder.setTitle(getString(R.string.empty_title_alert_title));
            builder.setMessage(getString(R.string.empty_title_alert_message));

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opt_menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.opt_add_edit) {
            String title = txtNoteTitleEditActivity.getText().toString();
            String desc = txtNoteDescEditActivity.getText().toString();
            if(!title.isEmpty()) {
                Notes n = new Notes(title,desc);
                Intent data = new Intent();
                data.putExtra("NOTE_OBJECT", n);
                setResult(RESULT_OK, data);
                finish(); // This closes the current activity, returning us to the original activity
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setPositiveButton(getString(R.string.ok_confirm_button), (dialog, id) -> {
                    Intent intent = new Intent(EditActivity.this, MainActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                });
                builder.setNegativeButton(getString(R.string.cancel_confirm_button), (dialog, id) -> {
                    dialog.dismiss();
                });

                builder.setTitle(getString(R.string.empty_title_alert_title));
                builder.setMessage(getString(R.string.empty_title_alert_message));

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
        return true;
    }
}