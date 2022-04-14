package com.example.civiladvocacyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextClock;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class OfficialActivity extends AppCompatActivity {

    private static final String TAG = "OfficialActivity";
    private Picasso picasso;

    Offices offices;
    Officials officials;
    String location;

    ScrollView scrollView;
    ConstraintLayout constraintLayout;

    TextView txtLocation_official;
    TextView txtoffice_official;
    TextView txtname_official;
    TextView txtparty_official;

    ImageView imgphoto_official;
    ImageView imgparty_official;

    TextView txtaddresslabel_official;
    TextView txtaddress_official;
    TextView txtphonelabel_official;
    TextView txtphone_official;
    TextView txtemaillabel_official;
    TextView txtemail_official;
    TextView txtwebsitelabel_official;
    TextView txtwebsite_official;

    ImageView imgfacebook_official;
    ImageView imgtwitter_official;
    ImageView imgyoutube_official;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        picasso = Picasso.get();
        InitializeControls();
        LoadData();
    }

    private void InitializeControls() {
        scrollView = findViewById(R.id.scrollView);
        constraintLayout = findViewById((R.id.constraintLayout));
        txtLocation_official = findViewById(R.id.txtLocation_official);
        txtoffice_official = findViewById(R.id.txtoffice_official);
        txtname_official = findViewById(R.id.txtname_official);
        txtparty_official = findViewById(R.id.txtparty_official);
        imgphoto_official = findViewById(R.id.imgphoto_official);
        imgparty_official = findViewById(R.id.imgparty_official);
        txtaddresslabel_official = findViewById(R.id.txtaddresslabel_official);
        txtaddress_official = findViewById(R.id.txtaddress_official);
        txtphonelabel_official = findViewById(R.id.txtphonelabel_official);
        txtphone_official = findViewById(R.id.txtphone_official);
        txtemaillabel_official = findViewById(R.id.txtemaillabel_official);
        txtemail_official = findViewById(R.id.txtemail_official);
        txtwebsitelabel_official = findViewById(R.id.txtwebsitelabel_official);
        txtwebsite_official = findViewById(R.id.txtwebsite_official);
        imgfacebook_official = findViewById(R.id.imgfacebook_official);
        imgtwitter_official = findViewById(R.id.imgtwitter_official);
        imgyoutube_official = findViewById(R.id.imgyoutube_official);
    }

    private void LoadData() {
        officials = (Officials) getIntent().getSerializableExtra("officialData");
        offices = (Offices) getIntent().getSerializableExtra("officeData");
        location = getIntent().getStringExtra("location");
        txtLocation_official.setText(location);
        txtoffice_official.setText(offices.getOffices_name());
        txtname_official.setText(officials.getOfficials_name());
        if(officials.getOfficials_party().toUpperCase().contains("DEMOCRATIC")){
            txtparty_official.setVisibility(View.VISIBLE);
            txtparty_official.setText(officials.getOfficials_party());
            scrollView.setBackgroundColor(Color.BLUE);
            imgparty_official.setVisibility(View.VISIBLE);
            imgparty_official.setImageResource(R.drawable.dem_logo);
        }
        else if(officials.getOfficials_party().toUpperCase().contains("REPUBLICAN")){
            txtparty_official.setVisibility(View.VISIBLE);
            txtparty_official.setText(officials.getOfficials_party());
            scrollView.setBackgroundColor(Color.RED);
            imgparty_official.setVisibility(View.VISIBLE);
            imgparty_official.setImageResource(R.drawable.rep_logo);
        }
        else{
            txtparty_official.setVisibility(View.INVISIBLE);
            scrollView.setBackgroundColor(Color.BLACK);
            imgparty_official.setVisibility(View.INVISIBLE);
        }
        if(officials.getOfficials_photourl()!=null && !officials.getOfficials_photourl().isEmpty()) {
            loadRemoteImage(officials.getOfficials_photourl());
        }
        if(officials.getOfficials_address()!=null && !officials.getOfficials_address().isEmpty()){
            txtaddresslabel_official.setVisibility(View.VISIBLE);
            txtaddress_official.setVisibility(View.VISIBLE);
            txtaddress_official.setText(officials.getOfficials_address());
        }
        else{
            txtaddresslabel_official.setVisibility(View.INVISIBLE);
            txtaddress_official.setVisibility(View.INVISIBLE);
        }
        if(officials.getOfficials_phone()!=null && !officials.getOfficials_phone().isEmpty()){
            txtphonelabel_official.setVisibility(View.VISIBLE);
            txtphone_official.setVisibility(View.VISIBLE);
            txtphone_official.setText(officials.getOfficials_phone());
        }
        else{
            txtphonelabel_official.setVisibility(View.INVISIBLE);
            txtphone_official.setVisibility(View.INVISIBLE);
        }
        if(officials.getOfficials_email()!=null && !officials.getOfficials_email().isEmpty()){
            txtemaillabel_official.setVisibility(View.VISIBLE);
            txtemail_official.setVisibility(View.VISIBLE);
            txtemail_official.setText(officials.getOfficials_email());
        }
        else{
            txtemaillabel_official.setVisibility(View.INVISIBLE);
            txtemail_official.setVisibility(View.INVISIBLE);
        }
        if(officials.getOfficials_url()!=null && !officials.getOfficials_url().isEmpty()){
            txtwebsitelabel_official.setVisibility(View.VISIBLE);
            txtwebsite_official.setVisibility(View.VISIBLE);
            txtwebsite_official.setText(officials.getOfficials_url());
        }
        else{
            txtwebsitelabel_official.setVisibility(View.INVISIBLE);
            txtwebsite_official.setVisibility(View.INVISIBLE);
        }
        imgfacebook_official.setVisibility(View.GONE);
        imgtwitter_official.setVisibility(View.GONE);
        imgyoutube_official.setVisibility(View.GONE);
        if (officials.getOfficials_channels()!=null && !officials.getOfficials_channels().isEmpty()) {
            if(officials.getOfficials_channels().size()>0){
                for(int i=0;i<officials.getOfficials_channels().size();i++){
                    Channels channels = officials.getOfficials_channels().get(i);
                    if(channels.getChannels_id()!=null && !channels.getChannels_id().isEmpty()){
                        if(channels.getChannels_type().toUpperCase().contains("FACEBOOK")){
                            imgfacebook_official.setVisibility(View.VISIBLE);
                        }
                        if(channels.getChannels_type().toUpperCase().contains("TWITTER")){
                            imgtwitter_official.setVisibility(View.VISIBLE);
                        }
                        if(channels.getChannels_type().toUpperCase().contains("YOUTUBE")){
                            imgyoutube_official.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        } else {
        }
    }

    private void loadRemoteImage(String imageURL) {
        try {
            String temp = imageURL;
            picasso.load(imageURL)
                    .error(R.drawable.missing)
                    .placeholder(R.drawable.placeholder)
                    .into(imgphoto_official);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "loadRemoteImage: ", e);
        }
    }

    public void OnImageClicked(View view) {
    }

    public void OnPartyLogoClicked(View view) {
    }

    public void OnAddressClicked(View view) {
    }

    public void OnPhoneClicked(View view) {
    }

    public void OnEmailClicked(View view) {
    }

    public void OnWebsiteClicked(View view) {
    }

    public void OnFacebookClicked(View view) {
    }

    public void OnTwitterClicked(View view) {
    }

    public void OnYoutubeClicked(View view) {
    }
}