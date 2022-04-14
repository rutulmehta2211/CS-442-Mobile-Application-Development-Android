package com.example.weathereapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DayWiseForecast extends AppCompatActivity {

    TextView txtLocation;
    RecyclerView recyclerdataView;
    String jsonData;
    boolean farenheit;
    ArrayList<DayWiseData> dayWiseData = new ArrayList<DayWiseData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_wise_forecast);

        //Initialize Controls
        txtLocation = findViewById(R.id.txtLocation);
        recyclerdataView = findViewById(R.id.recyclerViewDayWiseForecastData);
        loadData();
    }

    @SuppressLint("SetTextI18n")
    public void loadData(){
        jsonData = getIntent().getStringExtra("dailyData");
        farenheit = getIntent().getBooleanExtra("farenheit",true);
        txtLocation.setText(""+getIntent().getStringExtra("location"));

        try {
            JSONObject jObjMain = new JSONObject(jsonData);
            JSONArray daily = jObjMain.getJSONArray("daily");
            for(int i = 0 ; i<daily.length();i++){
                JSONObject dateObject = daily.getJSONObject(i);

                String iconCode = "_" + (dateObject.getJSONArray("weather").getJSONObject(0).getString("icon"));
                String description = (dateObject.getJSONArray("weather").getJSONObject(0).getString("description"));
                String precipitation = dateObject.getString("pop") + "% precip.";
                String uvi = dateObject.getString("uvi");
                String morning = dateObject.getJSONObject("temp").getString("morn");
                String afternoon = dateObject.getJSONObject("temp").getString("day");
                String evening = dateObject.getJSONObject("temp").getString("eve");
                String night = dateObject.getJSONObject("temp").getString("night");

                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                cal.setTimeInMillis(Long.parseLong(dateObject.getString("dt")) * 1000);
                @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("EEEE, MM/dd").format(cal.getTime());

                String highLowTemperature = String.format("%.0f° " + (farenheit ? "F" : "C"), Double.parseDouble(dateObject.getJSONObject("temp").getString("max"))) +" / " +String.format("%.0f° " + (farenheit ? "F" : "C"), Double.parseDouble(dateObject.getJSONObject("temp").getString("min")));

                DayWiseData dwd = new DayWiseData(date, highLowTemperature, description, precipitation, uvi, morning, afternoon, evening, night, iconCode, farenheit);
                dayWiseData.add(dwd);
            }
            DayWiseDataAdapter dailyAdapter = new DayWiseDataAdapter(dayWiseData,DayWiseForecast.this);
            recyclerdataView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerdataView.setAdapter(dailyAdapter);

        } catch (Exception e) {
            Log.d("Error",""+e.getMessage());
        }
    }
}