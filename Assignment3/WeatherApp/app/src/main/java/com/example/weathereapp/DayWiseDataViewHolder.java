package com.example.weathereapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class DayWiseDataViewHolder extends RecyclerView.ViewHolder {
    TextView txtDate,txtHighLow,txtUvindex,txtDescription,txtProbabilityofPrecipitation,txtMorning,txtAfternoon,txtEvening,txtNight;
    ImageView imgWeatherIcon;

    DayWiseDataViewHolder(View view) {
        super(view);
        txtDate = view.findViewById(R.id.txtDateDayWeatherRow);
        txtHighLow = view.findViewById(R.id.txtHighLowTempDayWeatherRow);
        txtUvindex = view.findViewById(R.id.txtUvindexDayWeatherRow);
        txtDescription = view.findViewById(R.id.txtDescriptionDayWeatherRow);
        txtProbabilityofPrecipitation = view.findViewById(R.id.txtProbabilityofPrecipitationDayWeatherRow);
        txtMorning = view.findViewById(R.id.txtMorningTempDayWeatherRow);
        txtAfternoon = view.findViewById(R.id.txtAfternoonTempDayWeatherRow);
        txtEvening = view.findViewById(R.id.txtEveningTempDayWeatherRow);
        txtNight = view.findViewById(R.id.txtNightTempDayWeatherRow);
        imgWeatherIcon = view.findViewById(R.id.imgWeatherIconDayWeatherRow);
    }
}
