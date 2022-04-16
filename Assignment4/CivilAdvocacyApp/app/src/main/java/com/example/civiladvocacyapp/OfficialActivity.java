package com.example.civiladvocacyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class OfficialActivity extends AppCompatActivity {

    private static final String TAG = "OfficialActivity";
    private Picasso picasso;

    Officials officials;
    String location;
    String officeName;

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
        getSupportActionBar().setTitle(R.string.civil_advocacy_about);
        picasso = Picasso.get();
        picasso.setIndicatorsEnabled(true);
        picasso.setLoggingEnabled(true);
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

    @SuppressLint("SetTextI18n")
    private void LoadData() {
        officials = (Officials) getIntent().getSerializableExtra("officialData");
        officeName = getIntent().getStringExtra("officeName").toString();
        location = getIntent().getStringExtra("location");
        txtLocation_official.setText(location);
        txtoffice_official.setText(officeName);
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
            txtaddress_official.setText(officials.getOfficials_address() + " " + officials.getOfficials_city() + ", " + officials.getOfficials_state() + " " + officials.getOfficials_zip());
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
        }
    }

    private void loadRemoteImage(String imageURL) {
        try {
            Log.d(TAG, "loadRemoteImage: "+imageURL);
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
        if (officials != null && officials.getOfficials_photourl()!=null && !officials.getOfficials_photourl().isEmpty()) {
            Intent intent = new Intent(this, PhotoActivity.class);
            intent.putExtra("officeName",officeName);
            intent.putExtra("officialData", officials);
            intent.putExtra("location", location);
            startActivity(intent);
        } else {
            Toast.makeText(OfficialActivity.this, R.string.nophoto_officials, Toast.LENGTH_SHORT).show();
        }
    }

    public void OnPartyLogoClicked(View view) {
        if(officials!=null && !officials.getOfficials_party().isEmpty()){
            String partyUrl="";
            if (officials.getOfficials_party().toUpperCase().contains("DEMOCRATIC")) {
                partyUrl = "https://democrats.org/";
            }
            else if (officials.getOfficials_party().toUpperCase().contains("REPUBLICAN")) {
                partyUrl = "https://www.gop.com/";
            }
            openUrl(partyUrl);
        }
    }

    public void OnAddressClicked(View view) {
        try {
            if(officials!=null && !officials.getOfficials_address().isEmpty()){
                String address = officials.getOfficials_address() + " " + officials.getOfficials_city() + ", " + officials.getOfficials_state() + " " + officials.getOfficials_zip();
                Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
                Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(OfficialActivity.this, R.string.nomapapplication_official, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "OnAddressClicked: ",e );
        }
    }

    public void OnPhoneClicked(View view) {
        try {
            if(officials!=null && !officials.getOfficials_phone().isEmpty()){
                String number = officials.getOfficials_phone();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(OfficialActivity.this, R.string.nocallapplication_officials, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "OnPhoneClicked: ", e);
        }
    }

    public void OnEmailClicked(View view) {
        try {
            if (officials!=null && !officials.getOfficials_email().isEmpty()) {
                String[] addresses = new String[]{officials.getOfficials_email()};
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(OfficialActivity.this, R.string.noemailclient_officials, Toast.LENGTH_SHORT).show();
            Log.e(TAG, "OnEmailClicked: ", e);
        }
    }

    public void OnWebsiteClicked(View view) {
        if(officials!=null && !officials.getOfficials_url().isEmpty()){
            openUrl(officials.getOfficials_url());
        }
    }

    public void OnFacebookClicked(View view) {
        String FACEBOOK_URL = "";
        String urlToUse="";
        Channels channels = null;
        PackageManager packageManager = getPackageManager();
        try {
            if (officials != null && officials.getOfficials_channels().size() > 0) {
                for (int i = 0; i < officials.getOfficials_channels().size(); i++) {
                    channels = officials.getOfficials_channels().get(i);
                    if (channels.getChannels_id() != null && !channels.getChannels_id().isEmpty()) {
                        if (channels.getChannels_type().toUpperCase().contains("FACEBOOK")) {
                            FACEBOOK_URL = "https://www.facebook.com/" + channels.getChannels_id();
                        }
                    }
                }
            }
            if(!FACEBOOK_URL.isEmpty()) {
                int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                if (versionCode >= 3002850) { //newer versions of fb app
                    urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                } else { //older versions of fb app
                    urlToUse = "fb://page/" + channels.getChannels_id();
                }
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL; //normal web url
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);

    }

    public void OnTwitterClicked(View view) {
        Intent intent = null;
        String twitterID="";
        try {
            if (officials != null && officials.getOfficials_channels().size() > 0) {
                for (int i = 0; i < officials.getOfficials_channels().size(); i++) {
                    Channels channels = officials.getOfficials_channels().get(i);
                    if (channels.getChannels_id() != null && !channels.getChannels_id().isEmpty()) {
                        if (channels.getChannels_type().toUpperCase().contains("TWITTER")) {
                            twitterID = channels.getChannels_id();
                        }
                    }
                }
            }
            if(!twitterID.isEmpty()) {
                getPackageManager().getPackageInfo("com.twitter.android", 0);
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitterID));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
        }
        catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + twitterID));
        }
        startActivity(intent);
    }

    public void OnYoutubeClicked(View view) {
        Intent intent = null;
        String youtubeID="";
        try {
            if (officials != null && officials.getOfficials_channels().size() > 0) {
                for (int i = 0; i < officials.getOfficials_channels().size(); i++) {
                    Channels channels = officials.getOfficials_channels().get(i);
                    if (channels.getChannels_id() != null && !channels.getChannels_id().isEmpty()) {
                        if (channels.getChannels_type().toUpperCase().contains("YOUTUBE")) {
                            youtubeID = channels.getChannels_id();
                        }
                    }
                }
            }
            if(!youtubeID.isEmpty()) {
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setPackage("com.google.android.youtube");
                intent.setData(Uri.parse("https://www.youtube.com/" + youtubeID));
                startActivity(intent);
            }
        }
        catch (ActivityNotFoundException e) {
            // no Twitter app, revert to browser
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/" + youtubeID)));
        }
    }

    public void openUrl(String url) {
        if (url != null && !url.isEmpty()) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
        }
    }
}