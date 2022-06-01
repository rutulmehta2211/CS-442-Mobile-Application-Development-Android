package com.example.newsaggregator;

import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewsSourcesDownloader implements Runnable{
    private static final String TAG = "NewsSourcesDownloader";
    //https://newsapi.org/v2/sources?apiKey=6b979057f3a5439f80e75ebf0bf8de4e
    private static final String URL = "https://newsapi.org/v2/sources";
    private static final String APIKey = "6b979057f3a5439f80e75ebf0bf8de4e";
    private final MainActivity mainActivity;

    public NewsSourcesDownloader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        Log.d(TAG, "run: ");
        try {
            Uri.Builder buildURL = Uri.parse(URL).buildUpon();
            buildURL.appendQueryParameter("apikey", APIKey);

            String urlToUse = buildURL.build().toString();
            JsonObjectRequest jsonObjectRequest =
                    new JsonObjectRequest(
                            Request.Method.GET,
                            urlToUse,
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    ArrayList<NewsSources> lstNewsSources = parseJSON(response.toString());
                                    mainActivity.runOnUiThread(() -> mainActivity.updateNewsSourcesData(lstNewsSources));
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e(TAG, "onErrorResponse: ", error);
                                }
                            }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>();
                            headers.put("User-Agent", "");
                            return headers;
                        };
                    };
            RequestQueue queue = Volley.newRequestQueue(mainActivity);
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "run: ", e);
        }
    }

    private ArrayList<NewsSources> parseJSON(String s) {
        Log.d(TAG, "parseJSON: ");
        try {
            JSONObject jobjMain = new JSONObject(s);
            JSONArray sources=jobjMain.getJSONArray("sources");
            ArrayList<NewsSources> lstObjSources = new ArrayList<>();
            for(int i=0;i<sources.length();i++){
                JSONObject jobjNewsSources = (JSONObject) sources.get(i);
                String jobjNewsSources_Id = jobjNewsSources.has("id")?jobjNewsSources.getString("id"):"";
                String jobjNewsSources_Name = jobjNewsSources.has("name")?jobjNewsSources.getString("name"):"";
                String jobjNewsSources_Category = jobjNewsSources.has("category")?jobjNewsSources.getString("category"):"";
                String color_Category = jobjNewsSources.has("category")?GetColorCodeForCategory(jobjNewsSources_Category):"";
                lstObjSources.add(new NewsSources(jobjNewsSources_Id,jobjNewsSources_Name,jobjNewsSources_Category,color_Category));
            }
            return lstObjSources;

        } catch (JSONException e) {
            Log.e(TAG, "parseJSON: ",e );
            e.printStackTrace();
        }
        return null;
    }

    private String GetColorCodeForCategory(String jobjNewsSources_Category){
        String color_Category="";
        switch (jobjNewsSources_Category) {
            case "general":
                color_Category = "#deae64";
                break;
            case "sports":
                color_Category = "#b9b3ec";
                break;
            case "business":
                color_Category = "#45804e";
                break;
            case "entertainment":
                color_Category = "#c67763";
                break;
            case "science":
                color_Category = "#6c98d3";
                break;
            case "health":
                color_Category = "#7a9da3";
                break;
            case "technology":
                color_Category = "#ef64ee";
                break;
            default:
                color_Category = "#FFFFFF";
                break;
        }
        return color_Category;
    }
}
