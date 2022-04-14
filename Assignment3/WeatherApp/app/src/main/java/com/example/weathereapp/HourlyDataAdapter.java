package com.example.weathereapp;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.List;

public class HourlyDataAdapter extends RecyclerView.Adapter<HourlyDataViewHolder> {
    public final List<HourlyData> hourlyData;
    public final MainActivity mainActivity;

    public HourlyDataAdapter(List<HourlyData> hourlyWeathers, MainActivity ma) {
        this.hourlyData = hourlyWeathers;
        mainActivity = ma;
    }

    @NonNull
    @Override
    public HourlyDataViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_weather_mainactivity_row, parent, false);

        return new HourlyDataViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HourlyDataViewHolder holder, int position) {

        HourlyData hourlyWeather = hourlyData.get(position);
        holder.txtDay.setText(hourlyWeather.day);
        holder.txtTime.setText(hourlyWeather.time);
        holder.txtTemperature.setText(hourlyWeather.temp);
        holder.txtDescription.setText(hourlyWeather.description);

        holder.imgWeather.setImageResource(mainActivity.getResources().getIdentifier(hourlyWeather.icon, "drawable", "com.example.weathereapp"));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                builder.appendPath("time");
                ContentUris.appendId(builder, Long.parseLong(hourlyWeather.timestamp)*1000+Long.parseLong(hourlyWeather.timezoneOffset));
                Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setData(builder.build());
                mainActivity.startActivity(intent);
                Log.d("TimeIssue", "onClick: "+getDate(Long.parseLong(hourlyWeather.timestamp)*1000+Long.parseLong(hourlyWeather.timezoneOffset), "dd/MM/yyyy hh:mm:ss.SSS"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return hourlyData.size();
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
