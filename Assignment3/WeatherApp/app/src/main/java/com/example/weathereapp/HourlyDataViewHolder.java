package com.example.weathereapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class HourlyDataViewHolder extends RecyclerView.ViewHolder {
    TextView txtDay,txtTime,txtTemperature,txtDescription;
    ImageView imgWeather;
    LinearLayout linearLayout;

    HourlyDataViewHolder(View view) {
        super(view);
        txtDay = view.findViewById(R.id.txtDay);
        txtTime = view.findViewById(R.id.txtTime);
        txtTemperature = view.findViewById(R.id.txtTemperature);
        txtDescription = view.findViewById(R.id.txtDescriptionDayWeatherRow);
        imgWeather = view.findViewById(R.id.imgWeather);
        linearLayout = view.findViewById(R.id.linearLayout);
    }
}
