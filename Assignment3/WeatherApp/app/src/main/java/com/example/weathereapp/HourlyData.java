package com.example.weathereapp;

public class HourlyData {
    public String day;
    public String time;
    public String icon;
    public String temp;
    public String description;
    public String timestamp;
    public String timezoneOffset;

    public HourlyData(String day, String time, String icon, String temp, String description,String timestamp, String timezoneOffset) {
        this.day = day;
        this.time = time;
        this.icon = icon;
        this.temp = temp;
        this.description = description;
        this.timestamp = timestamp;
        this.timezoneOffset = timezoneOffset;
    }
}
