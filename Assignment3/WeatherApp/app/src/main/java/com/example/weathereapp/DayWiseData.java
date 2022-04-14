package com.example.weathereapp;

public class DayWiseData {
    public String date;
    public String temp;
    public String description;
    public String precipitation;
    public String uvindex;
    public String morning;
    public String day;
    public String evening;
    public String night;
    public boolean farenheit;
    public String iconcode;


    public DayWiseData(String date, String temp, String description, String precipitation, String uvindex, String morning, String day, String evening, String night, String iconcode,boolean farenheit) {
        this.date = date;
        this.temp = temp;
        this.description = description;
        this.precipitation = precipitation;
        this.uvindex = uvindex;
        this.morning = morning;
        this.day = day;
        this.evening = evening;
        this.night = night;
        this.iconcode = iconcode;
        this.farenheit = farenheit;
    }

    public String getMorning() {
        return morning;
    }

    public String getDay() {
        return day;
    }

    public String getEvening() {
        return evening;
    }

    public String getNight() {
        return night;
    }

}