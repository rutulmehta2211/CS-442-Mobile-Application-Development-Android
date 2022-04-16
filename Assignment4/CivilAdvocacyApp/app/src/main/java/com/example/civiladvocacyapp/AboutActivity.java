package com.example.civiladvocacyapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AboutActivity extends AppCompatActivity {

    private String googleCivicInfoUrl = "https://developers.google.com/civic-information";

    TextView txtCopyright;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setTitle(R.string.civil_advocacy_about);

        txtCopyright = findViewById(R.id.txtCopyright);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateformat = new SimpleDateFormat("yyyy");
        String year = dateformat.format(new Date());
        txtCopyright.setText("\u00a9 "+year+", Rutul Mehta");
    }

    public void OnGoogleCivicInfoClicked(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(googleCivicInfoUrl));
        startActivity(i);
    }
}