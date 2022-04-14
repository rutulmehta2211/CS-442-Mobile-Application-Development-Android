package com.example.weathereapp;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DayWiseDataAdapter extends RecyclerView.Adapter<DayWiseDataViewHolder> {
    public final List<DayWiseData> lstDayWiseData;
    public final DayWiseForecast mainActivity;

    public DayWiseDataAdapter(List<DayWiseData> lstDayWiseData, DayWiseForecast ma) {
        this.lstDayWiseData = lstDayWiseData;
        mainActivity = ma;
    }

    @NonNull
    @Override
    public DayWiseDataViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.day_weather_daywiseforecast_row, parent, false);

        return new DayWiseDataViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DayWiseDataViewHolder holder, int position) {

        DayWiseData dayWiseData = lstDayWiseData.get(position);

        holder.txtDate.setText(dayWiseData.date);
        holder.txtHighLow.setText(dayWiseData.temp);
        holder.txtUvindex.setText("UV Index : "+dayWiseData.uvindex);
        holder.txtDescription.setText(dayWiseData.description);
        holder.txtProbabilityofPrecipitation.setText("( "+dayWiseData.precipitation+" )");
        holder.txtMorning.setText(String.format("%.0f° " + (dayWiseData.farenheit ? "F" : "C"), Double.parseDouble(dayWiseData.getMorning())));
        holder.txtAfternoon.setText(String.format("%.0f° " + (dayWiseData.farenheit ? "F" : "C"), Double.parseDouble(dayWiseData.getDay())));
        holder.txtEvening.setText(String.format("%.0f° " + (dayWiseData.farenheit ? "F" : "C"), Double.parseDouble(dayWiseData.getEvening())));
        holder.txtNight.setText(String.format("%.0f° " + (dayWiseData.farenheit ? "F" : "C"), Double.parseDouble(dayWiseData.getNight())));

        holder.txtDate.setText(dayWiseData.date);
        holder.imgWeatherIcon.setImageResource(mainActivity.getResources().getIdentifier(dayWiseData.iconcode, "drawable", "com.app.weatherapp"));
    }

    @Override
    public int getItemCount() {
        return lstDayWiseData.size();
    }
}
