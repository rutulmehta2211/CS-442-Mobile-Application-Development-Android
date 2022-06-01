package com.example.civiladvocacyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotoActivity extends AppCompatActivity {

    private static final String TAG = "PhotoActivity";
    private Picasso picasso;

    Officials officials;
    String location;
    String officeName;

    ConstraintLayout constraintLayout;
    TextView txtlocation_photo;
    TextView txtoffice_photo;
    TextView txtname_photo;
    ImageView imgphoto_photo;
    ImageView imgparty_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        getSupportActionBar().setTitle(R.string.civil_advocacy_about);
        picasso = Picasso.get();
        //picasso.setIndicatorsEnabled(true);
        //picasso.setLoggingEnabled(true);
        InitializeControls();
        LoadData();
    }

    private void InitializeControls() {
        constraintLayout = findViewById((R.id.constraintLayout));
        txtlocation_photo = findViewById(R.id.txtlocation_photo);
        txtoffice_photo = findViewById(R.id.txtoffice_photo);
        txtname_photo = findViewById(R.id.txtname_photo);
        imgphoto_photo = findViewById(R.id.imgphoto_photo);
        imgparty_photo = findViewById(R.id.imgparty_photo);
    }

    private void LoadData(){
        officials = (Officials) getIntent().getSerializableExtra("officialData");
        officeName = getIntent().getStringExtra("officeName").toString();
        location = getIntent().getStringExtra("location");

        txtlocation_photo.setText(location);
        txtoffice_photo.setText(officeName);
        txtname_photo.setText(officials.getOfficials_name());
        if(officials.getOfficials_party().toUpperCase().contains("DEMOCRATIC")){
            constraintLayout.setBackgroundColor(Color.BLUE);
            imgparty_photo.setVisibility(View.VISIBLE);
            imgparty_photo.setImageResource(R.drawable.dem_logo);
        }
        else if(officials.getOfficials_party().toUpperCase().contains("REPUBLICAN")){
            constraintLayout.setBackgroundColor(Color.RED);
            imgparty_photo.setVisibility(View.VISIBLE);
            imgparty_photo.setImageResource(R.drawable.rep_logo);
        }
        else{
            constraintLayout.setVisibility(View.INVISIBLE);
            imgparty_photo.setBackgroundColor(Color.BLACK);
            imgparty_photo.setVisibility(View.INVISIBLE);
        }
        if(officials.getOfficials_photourl()!=null && !officials.getOfficials_photourl().isEmpty()) {
            loadRemoteImage(officials.getOfficials_photourl());
        }
    }

    private void loadRemoteImage(String imageURL) {
        try {
            Log.d(TAG, "loadRemoteImage: ");
            picasso.load(imageURL)
                    .error(R.drawable.missing)
                    .placeholder(R.drawable.placeholder)
                    .into(imgphoto_photo);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "loadRemoteImage: ", e);
        }
    }
}