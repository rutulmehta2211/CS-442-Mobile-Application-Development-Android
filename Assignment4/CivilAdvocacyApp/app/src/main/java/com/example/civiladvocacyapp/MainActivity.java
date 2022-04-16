package com.example.civiladvocacyapp;

import static android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST = 111;
    //private String locationString = ""; //Chicago, Illinois
    private CivicInformation civicInformation;

    TextView txtLocation;
    RecyclerView recyclerView;
    ConstraintLayout constraintLayout;

    MainActivityAdapter mainActivityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtLocation = findViewById(R.id.txtLocation);
        recyclerView=findViewById(R.id.recyclerView);
        constraintLayout=findViewById(R.id.constraintLayout);

        //mainActivityAdapter = new MainActivityAdapter(MainActivity.this,civicInformation);
        //recyclerView.setAdapter(mainActivityAdapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        determineLocation();
    }

    //Function to Check Network Connection
    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = null;
        connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    private void determineLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        String locationString = getPlace(location);
                        loadData(locationString);
                    }
                })
                .addOnFailureListener(this, e -> {
                    Toast.makeText(MainActivity.this,
                            e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    Log.e(TAG, "determineLocation: ", e);
                });

    }

    public void loadData(String locationString) {
        if(locationString!=null && !locationString.isEmpty()) {
            txtLocation.setText(locationString);
            if(hasNetworkConnection()) {
                CivicInformationDownloader civicInformationDownloader = new CivicInformationDownloader(MainActivity.this,locationString);
                new Thread(civicInformationDownloader).start();
            }
            else{
                errorDialog(getString(R.string.no_internet),getString(R.string.no_internet_dialog_msg));
            }
        }
        else{
            errorDialog(getString(R.string.no_location),getString(R.string.no_location_permission));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST) {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                } else {
                    errorDialog(getString(R.string.no_location), getString(R.string.no_location_permission));
                }
            }
        }
    }

    private String getPlace(Location loc) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            if(addresses.size()==0){
                errorDialog(getString(R.string.no_location), getString(R.string.main_invalid_location));
                return null;
            }
            Address ad = addresses.get(0);
            return ad.getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "getPlace: ", e);
        }
        return null;
    }

    private String getPlace(String loc) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(loc,1);
            if(addresses.size()==0){
                errorDialog(getString(R.string.no_location), getString(R.string.main_invalid_location));
                return null;
            }
            Address ad = addresses.get(0);
            return ad.getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "getPlace: ", e);
        }
        return null;
    }

    private void errorDialog(String title, String msg) {
        Log.d(TAG, "errorDialog: ");
        txtLocation.setText(title);
        constraintLayout.setBackgroundColor(Color.WHITE);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(msg);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        getMenuInflater().inflate(R.menu.opt_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        try {
            Log.d(TAG, "onOptionsItemSelected: ");
            if(item.getItemId()==R.id.icon_about_info){
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
            else {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                final EditText et = new EditText(this);
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                et.setGravity(Gravity.CENTER_HORIZONTAL);
                builder.setView(et);
                builder.setTitle(R.string.dialog_title);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (et.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity.this, R.string.main_invalid_location, Toast.LENGTH_SHORT).show();
                        } else {
                            if(hasNetworkConnection()) {
                                String locationString = getPlace(et.getText().toString());
                                loadData(locationString);
                            }
                            else {
                                Toast.makeText(MainActivity.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                android.app.AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
        catch (Exception e){
            Log.e(TAG, "onOptionsItemSelected: ",e);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        try {
            int position = recyclerView.getChildLayoutPosition(view);
            String officeName="";
            Log.d(TAG, "onClick: "+position);
            Officials officials = civicInformation.getOfficials().get(position);
            Offices offices;
            for(int i=0;i<civicInformation.getOffices().size();i++) {
                offices = civicInformation.getOffices().get(i);
                if(offices.getOffices_officialIndices().contains(position)){
                    officeName = offices.getOffices_name();
                    break;
                }
            }
            if(hasNetworkConnection()) {
                if (officials != null) {
                    Intent intent = new Intent(this, OfficialActivity.class);
                    intent.putExtra("officeName",officeName);
                    intent.putExtra("officialData", officials);
                    intent.putExtra("location", txtLocation.getText());
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, R.string.nodata_officials, Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(MainActivity.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onClick: ", e);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(CivicInformation objCivicInformation) {
        if(objCivicInformation!=null){
            civicInformation = objCivicInformation;
            mainActivityAdapter = new MainActivityAdapter(MainActivity.this,objCivicInformation);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(mainActivityAdapter);
            //mainActivityAdapter.notifyDataSetChanged();
        }
    }
}