package com.example.weathereapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LinearLayout linearLayout1,linearLayout2,linearLayout3,linearLayout4,linearLayout5,linearLayout6;
    TextView txtLocation, txtCurrentDateTime,
            txtTemperature,txtTemperatureFeelsLike,
            txtDescriptionOfWeather, txtWindInfo,
            txtHumidity,txtUvindexDayWeatherRow, txtVisibility,
            txtMorningTemperature, txtAfternoonTemperature, txtEveningTemperature, txtNightTemperature,
            txtSunrise,txtSunset;
    RecyclerView recyclerViewData;
    ImageView imgIconForWeather;
    SwipeRefreshLayout swiperLayout;
    Menu menu;

    //Global Variable
    boolean isF=true;
    double lat=0.0;
    double lng=0.0;
    String location=null;
    String jsonData;
    private static final int LOCATION_REQUEST = 111;
    String default_locationName="Chicago , Illinois";
    String default_lat="41.8675766";
    String default_lng="-87.616232";

    //Save data
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Initialize Controls
        //Linear Layouts
        linearLayout1  = findViewById(R.id.linearLayout1);
        linearLayout2  = findViewById(R.id.linearLayout2);
        linearLayout3 = findViewById(R.id.linearLayout3);
        linearLayout4 = findViewById(R.id.linearLayout4);
        linearLayout5 = findViewById(R.id.linearLayout5);
        linearLayout6 = findViewById(R.id.linearLayout6);

        //Swiper Layout
        swiperLayout = findViewById(R.id.swiperLayout);

        //TextView Controls
        txtLocation  = findViewById(R.id.txtLocation);
        txtCurrentDateTime  = findViewById(R.id.txtCurrentDateTime);
        txtTemperature  = findViewById(R.id.txtTemperature);
        txtTemperatureFeelsLike  = findViewById(R.id.txtTemperatureFeelsLike);
        txtDescriptionOfWeather  = findViewById(R.id.txtDescriptionOfWeather);
        txtWindInfo  = findViewById(R.id.txtWindInfo);
        txtHumidity  = findViewById(R.id.txtHumidity);
        txtUvindexDayWeatherRow  = findViewById(R.id.txtUvindexDayWeatherRow);
        txtVisibility  = findViewById(R.id.txtVisibility);
        txtMorningTemperature  = findViewById(R.id.txtMorningTemperature);
        txtAfternoonTemperature  = findViewById(R.id.txtAfternoonTemperature);
        txtEveningTemperature  = findViewById(R.id.txtEveningTemperature);
        txtNightTemperature  = findViewById(R.id.txtNightTemperature);
        txtSunrise  = findViewById(R.id.txtSunrise);
        txtSunset  = findViewById(R.id.txtSunset);
        recyclerViewData  = findViewById(R.id.recyclerViewData);

        //ImageView Control
        imgIconForWeather  = findViewById(R.id.imgIconForWeather);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ChangeVisibility();
        onClicks();
        loadData();
    }

    //Function to Check Network Connection
    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = null;
        connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    //Change Layout Visibility if in case of no internet
    public void ChangeVisibility(){
        if(hasNetworkConnection()){
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.GONE);
        }
        else{
            linearLayout1.setVisibility(View.GONE);
            linearLayout2.setVisibility(View.VISIBLE);
        }
    }

    public void onClicks(){
        swiperLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(hasNetworkConnection()) {
                    OpenWeatherDownloader openWeatherDownloader = new OpenWeatherDownloader(MainActivity.this, lat, lng, isF);
                    new Thread(openWeatherDownloader).start();
                    swiperLayout.setRefreshing(false);
                    linearLayout1.setVisibility(View.VISIBLE);
                    linearLayout2.setVisibility(View.GONE);
                }
                else{
                    Toast.makeText(getApplicationContext(),R.string.no_internet_msg,Toast.LENGTH_LONG).show();
                    linearLayout1.setVisibility(View.GONE);
                    linearLayout2.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void loadData() {
        txtLocation.setText(prefs.getString("locationname", default_locationName));
        lat = Double.parseDouble(prefs.getString("lat", default_lat));
        lng = Double.parseDouble(prefs.getString("long", default_lng));
        isF = prefs.getBoolean("degreeselected", true);

        if (hasNetworkConnection()) {
            OpenWeatherDownloader openWeatherDownloader = new OpenWeatherDownloader(MainActivity.this, lat, lng, isF);
            new Thread(openWeatherDownloader).start();
        } else {
            Toast.makeText(getApplicationContext(), R.string.no_internet_msg, Toast.LENGTH_LONG).show();
        }
    }

    private String getLocationName(String userProvidedLocation) {
        String x1 = "";
        String x2 = "";
        Geocoder geocoder = new Geocoder(this); // Here, “this” is an Activity
        try {
            List<Address> address =
                    geocoder.getFromLocationName(userProvidedLocation, 1);
            if (address != null && address.size()!=0) {
                String country = address.get(0).getCountryCode();
                if (country.equals("US")) {
                    x1 = address.get(0).getLocality();
                    x2 = address.get(0).getAdminArea();
                } else {
                    x1 = address.get(0).getLocality();
                    if (x1 == null)
                        x1 = address.get(0).getSubAdminArea();
                    x2 = address.get(0).getCountryName();
                }
                location = x1 + ", " + x2;
                return location;
            }
            Toast.makeText(this,R.string.invalid_city_name,Toast.LENGTH_LONG).show();
            return null;
        }
        catch (IOException e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_icons, menu);
        this.menu = menu;
        MenuItem item = menu.getItem(0);
        if(isF){
            item.setIcon(ContextCompat.getDrawable(this, R.drawable.units_c));
        }
        else{
            item.setIcon(ContextCompat.getDrawable(this, R.drawable.units_f));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ChangeVisibility();
        if (item.getItemId() == R.id.menuDayWiseForecast) {
            if(hasNetworkConnection()) {
                Intent intent = new Intent(this, DayWiseForecast.class);
                intent.putExtra("dailyData", jsonData);
                intent.putExtra("farenheit", isF);
                intent.putExtra("location", txtLocation.getText().toString());
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(),R.string.no_internet_msg,Toast.LENGTH_LONG).show();
            }
            return true;

        }
        else if (item.getItemId() == R.id.menuUnitChange) {
            if(hasNetworkConnection()) {
                if (isF) {
                    isF = false;
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.units_f));
                    OpenWeatherDownloader openWeatherDownloader = new OpenWeatherDownloader(MainActivity.this, lat,lng, isF);
                    new Thread(openWeatherDownloader).start();
                } else {
                    isF = true;
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.units_c));
                    OpenWeatherDownloader openWeatherDownloader = new OpenWeatherDownloader(MainActivity.this, lat,lng, isF);
                    new Thread(openWeatherDownloader).start();
                }
            }
            else{
                Toast.makeText(getApplicationContext(),R.string.no_internet_msg,Toast.LENGTH_LONG).show();
            }
            return true;

        }
        else if (item.getItemId() == R.id.menuLocationChange) {
            if(hasNetworkConnection()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final EditText et = new EditText(this);
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                et.setGravity(Gravity.CENTER_HORIZONTAL);
                builder.setView(et);
                builder.setTitle(R.string.dialogue_title);
                builder.setMessage(R.string.dialogue_message);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        boolean isSuccess = getLatLon(et.getText().toString().trim());
                        if(isSuccess) {
                            txtLocation.setText(getLocationName(et.getText().toString().trim()));
                            prefs.edit().putString("locationname", txtLocation.getText().toString()).apply();
                            prefs.edit().putString("lat", "" + lat).apply();
                            prefs.edit().putString("long", "" + lng).apply();
                            prefs.edit().putBoolean("degreeselected", isF).apply();
                            OpenWeatherDownloader openWeatherDownloader = new OpenWeatherDownloader(MainActivity.this, lat, lng, isF);
                            new Thread(openWeatherDownloader).start();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),R.string.invalid_city_name,Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else{
                Toast.makeText(getApplicationContext(),R.string.no_internet_msg,Toast.LENGTH_LONG).show();
            }
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateData(Weather weather, String jsonData){
        if (weather == null) {
            Toast.makeText(this, R.string.invalid_city_name, Toast.LENGTH_SHORT).show();
            return;
        }
        this.jsonData = jsonData;
        txtTemperature.setText(String.format("%.0f° " + (isF ? "F" : "C"), Double.parseDouble(weather.getTemp())));
        txtHumidity.setText(String.format(Locale.getDefault(), "Humidity: %.0f%%", Double.parseDouble(weather.getHumidity())));
        imgIconForWeather.setImageResource(getResources().getIdentifier(
                weather.iconName, "drawable", "com.example.weathereapp"));
        txtWindInfo.setText(String.format("Winds: "+getDirection(Double.parseDouble(weather.getWinddegree()))+" at %.0f " + (isF ? "mph" : "mps"), Double.parseDouble(weather.getWindspeed())));
        txtDescriptionOfWeather.setText(String.format("%s", weather.getDescription()));
        txtAfternoonTemperature.setText(String.format("%.0f° " + (isF ? "F" : "C"), Double.parseDouble(weather.getDayTemp())));
        txtMorningTemperature.setText(String.format("%.0f° " + (isF ? "F" : "C"), Double.parseDouble(weather.getMorningTemp())));
        txtEveningTemperature.setText(String.format("%.0f° " + (isF ? "F" : "C"), Double.parseDouble(weather.getEveningTemp())));
        txtNightTemperature.setText(String.format("%.0f° " + (isF ? "F" : "C"), Double.parseDouble(weather.getNightTemp())));
        txtSunrise.setText("Sunrise : " + weather.sunrise);
        txtSunset.setText("Sunset : "+ weather.sunset);
        txtUvindexDayWeatherRow.setText("UV Index : "+String.format("%s", weather.getUvi()));
        txtTemperatureFeelsLike.setText("Feels Like "+String.format("%.0f° " + (isF ? "F" : "C"), Double.parseDouble(weather.getFeelslike())));
        txtVisibility.setText("Visibility : "+String.format("%s", (Double.parseDouble(weather.getVisibility())/1000) +" miles"));
        txtCurrentDateTime.setText(weather.timeZone);

        //Set hourly weather data in horizontal Recycler View
        recyclerViewData.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.HORIZONTAL,false));
        ArrayList<HourlyData> hourlyWeathers = new ArrayList<HourlyData>();
        try {
            JSONObject jObjMain = new JSONObject(jsonData);
            JSONArray hourly = jObjMain.getJSONArray("hourly");
            for (int i = 0; i < hourly.length(); i++) {
                JSONObject dateObject = hourly.getJSONObject(i);

                String icon = "_" + (dateObject.getJSONArray("weather").getJSONObject(0).getString("icon"));
                String description = (dateObject.getJSONArray("weather").getJSONObject(0).getString("description"));
                String temp = String.format("%.0f° " + (isF ? "F" : "C"), Double.parseDouble(dateObject.getString("temp")));
                String timeZoneOffset = jObjMain.getString("timezone_offset");

                String day = LocalDateTime
                        .ofEpochSecond(Long.parseLong(dateObject.getString("dt")) + Long.parseLong(timeZoneOffset), 0, ZoneOffset.UTC)
                        .format(DateTimeFormatter.ofPattern("EEEE", Locale.getDefault())); // Thu Sep 30 10:06 PM, 2021

                String today = LocalDateTime
                        .ofEpochSecond(Long.parseLong(jObjMain.getJSONObject("current").getString("dt")) + Long.parseLong(timeZoneOffset), 0, ZoneOffset.UTC)
                        .format(DateTimeFormatter.ofPattern("EEEE", Locale.getDefault()));

                String time = LocalDateTime
                        .ofEpochSecond(Long.parseLong(dateObject.getString("dt")) + Long.parseLong(timeZoneOffset), 0, ZoneOffset.UTC)
                        .format(DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault()));

                if(day.equalsIgnoreCase(today)){
                    day = "Today";
                }

                HourlyData hd= new HourlyData(day, time, icon, temp, description, dateObject.getString("dt"),timeZoneOffset);
                hourlyWeathers.add(hd);

            }

            HourlyDataAdapter hourlyAdapter = new HourlyDataAdapter(hourlyWeathers, MainActivity.this);
            recyclerViewData.setAdapter(hourlyAdapter);

        }
        catch (Exception e){
            Log.e("Error", e.getMessage());
        }
    }

    private boolean getLatLon(String userProvidedLocation) {
        Geocoder geocoder = new Geocoder(this); // Here, “this” is an Activity
        try {
            List<Address> address =
                    geocoder.getFromLocationName(userProvidedLocation, 1);
            if (address != null && address.size()!=0) {
                lat = address.get(0).getLatitude();
                lng = address.get(0).getLongitude();
                return true;
            }
        } catch (IOException e) {
            Log.e("Error", e.getMessage());
        }
        return false;
    }

    private String getDirection(double degrees) {
        if (degrees >= 337.5 || degrees < 22.5)
            return "N";
        if (degrees >= 22.5 && degrees < 67.5)
            return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
            return "E";
        if (degrees >= 112.5 && degrees < 157.5)
            return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
            return "S";
        if (degrees >= 202.5 && degrees < 247.5)
            return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
            return "W";
        if (degrees >= 292.5 && degrees < 337.5)
            return "NW";
        return "X"; // We'll use 'X' as the default if we get a bad value
    }
}