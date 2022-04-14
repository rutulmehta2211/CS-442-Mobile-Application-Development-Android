package com.example.weathereapp;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class OpenWeatherDownloader implements Runnable{

    private static final String TAG = "OpenWeatherDownloader";
    private static final String weatherURL = "https://api.openweathermap.org/data/2.5/onecall";
    private static final String yourAPIKey = "ea852acfd5b467264f13ecfbfc677dcf";
    private final MainActivity mainActivity;
    private final double lat;
    private final double lng;
    private final boolean f;

    public OpenWeatherDownloader(MainActivity mainActivity, double lat,double lng, boolean fahrenheit) {
        this.mainActivity = mainActivity;
        this.lat = lat;
        this.lng = lng;
        this.f = fahrenheit;
    }

    @Override
    public void run() {
        Uri.Builder buildURL = Uri.parse(weatherURL).buildUpon();
        buildURL.appendQueryParameter("lat", String.valueOf(lat));
        buildURL.appendQueryParameter("lon", String.valueOf(lng));
        buildURL.appendQueryParameter("appid", yourAPIKey);
        buildURL.appendQueryParameter("units", (f ? "imperial" : "metric"));
        buildURL.appendQueryParameter("lang","en");
        buildURL.appendQueryParameter("exclude","minutely");

        String urlToUse = buildURL.build().toString();

        Response.Listener<JSONObject> listener =
                response -> parseJSON(response.toString());

        Response.ErrorListener error =
                error1 -> mainActivity.updateData(null,null);

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error);
        RequestQueue queue = Volley.newRequestQueue(mainActivity);
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private void parseJSON(String s) {

        try {
            JSONObject jObjMain = new JSONObject(s);

            String timeZone = jObjMain.getString("timezone");
            String timeZoneOffset = jObjMain.getString("timezone_offset");

            //Current Object
            JSONObject jMain = jObjMain.getJSONObject("current");
            String dt = jMain.getString("dt");
            String humidity = jMain.getString("humidity");
            String feelsLike = jMain.getString("feels_like");
            String visibility = jMain.getString("visibility");
            String uvi = jMain.getString("uvi");
            String sunrise = jMain.getString("sunrise");
            String sunset = jMain.getString("sunset");
            String windspeed = jMain.getString("wind_speed");
            String winddegree = jMain.getString("wind_deg");
            String windgust=null;
            if(!jMain.isNull("wind_gust")){
                windgust = jMain.getString("wind_gust");
            }
            String temp = jMain.getString("temp");
            JSONArray weather = jMain.getJSONArray("weather");

            //Weather object
            JSONObject jWeather = (JSONObject)weather.get(0);
            String icon = "_" + jWeather.getString("icon");
            String main = jWeather.getString("main");
            String description = jWeather.getString("description");

            String formattedTimeString = LocalDateTime.
                    ofEpochSecond(Long.parseLong(dt) + Long.parseLong(timeZoneOffset), 0, ZoneOffset.UTC)
                    .format(DateTimeFormatter.ofPattern("EEE MMM dd h:mm a, yyyy", Locale.getDefault()));

            //Sunrise & sunset
            sunrise = LocalDateTime
                    .ofEpochSecond(Long.parseLong(sunrise) + Long.parseLong(timeZoneOffset), 0, ZoneOffset.UTC)
                    .format(DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault()));
            sunset = LocalDateTime
                    .ofEpochSecond(Long.parseLong(sunset) + Long.parseLong(timeZoneOffset), 0, ZoneOffset.UTC)
                    .format(DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault())); //

            JSONArray daily = jObjMain.getJSONArray("daily");
            JSONObject jDaily = (JSONObject) daily.get(0);
            JSONObject jDaily1 = jDaily.getJSONObject("temp");
            String afternoon = jDaily1.getString("day");
            String evening = jDaily1.getString("eve");
            String day = jDaily1.getString("morn");
            String night = jDaily1.getString("night");

            Weather w;
            if(windgust == null){
                w = new Weather("", "", description, temp, humidity, windspeed, winddegree,
                        "", visibility, feelsLike, uvi, day, afternoon, sunrise, sunset, evening,
                        night, formattedTimeString, icon);
            }
            else{
                w = new Weather("", "", description, temp, humidity, windspeed, winddegree,
                        windgust, "", visibility, feelsLike, uvi, day, afternoon, sunrise, sunset, evening,
                        night, formattedTimeString, icon);
            }
            mainActivity.runOnUiThread(() -> mainActivity.updateData(w,s));

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "error: ", e);
        }
    }
}
