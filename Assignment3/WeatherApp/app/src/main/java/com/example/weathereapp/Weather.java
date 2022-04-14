package com.example.weathereapp;

import java.util.Optional;

public class Weather {

    public final String city;
    public final String country;
    public final String description;
    public final String temp;
    public final String humidity;
    public final String windspeed;
    public final String winddegree;
    public String windgust;
    public final String clouds;
    public final String visibility;
    public final String feelslike;
    public final String uvi;
    public final String morningTemp;
    public final String dayTemp;
    public final String sunrise;
    public final String sunset;
    public final String eveningTemp;
    public final String nightTemp;
    public final String timeZone;
    public final String iconName;

    public String getDescription() {
        return description;
    }

    public String getTemp() {
        return temp;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getWindspeed() {
        return windspeed;
    }

    public String getWinddegree() {
        return winddegree;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getFeelslike() {
        return feelslike;
    }

    public String getUvi() {
        return uvi;
    }

    public String getMorningTemp() {
        return morningTemp;
    }

    public String getDayTemp() {
        return dayTemp;
    }

    public String getEveningTemp() {
        return eveningTemp;
    }

    public String getNightTemp() {
        return nightTemp;
    }

    public Weather(String city, String country, String description, String temp, String humidity, String windspeed, String winddegree, String windgust, String clouds, String visibility, String feelslike, String uvi, String morningTemp, String dayTemp, String sunrise, String sunset, String eveningTemp, String nightTemp, String timeZone, String iconName) {
        this.city = city;
        this.country = country;
        this.description = description;
        this.temp = temp;
        this.humidity = humidity;
        this.windspeed = windspeed;
        this.winddegree = winddegree;
        this.windgust = windgust;
        this.clouds = clouds;
        this.visibility = visibility;
        this.feelslike = feelslike;
        this.uvi = uvi;
        this.morningTemp = morningTemp;
        this.dayTemp = dayTemp;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.eveningTemp = eveningTemp;
        this.nightTemp = nightTemp;
        this.timeZone = timeZone;
        this.iconName = iconName;
    }

    public Weather(String city, String country, String description, String temp, String humidity, String windspeed, String winddegree, String clouds, String visibility, String feelslike, String uvi, String morningTemp, String dayTemp, String sunrise, String sunset, String eveningTemp, String nightTemp, String timeZone, String iconName) {
        this.city = city;
        this.country = country;
        this.description = description;
        this.temp = temp;
        this.humidity = humidity;
        this.windspeed = windspeed;
        this.winddegree = winddegree;
        this.clouds = clouds;
        this.visibility = visibility;
        this.feelslike = feelslike;
        this.uvi = uvi;
        this.morningTemp = morningTemp;
        this.dayTemp = dayTemp;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.eveningTemp = eveningTemp;
        this.nightTemp = nightTemp;
        this.timeZone = timeZone;
        this.iconName = iconName;
    }
}
