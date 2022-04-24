package com.example.newsaggregator;

import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewsArticlesDownloader implements Runnable{

    private static final String TAG = "NewsArticlesDownloader";
    //https://newsapi.org/v2/top-headlines?sources=bbc-news&apiKey=6b979057f3a5439f80e75ebf0bf8de4e
    private static final String URL = "https://newsapi.org/v2/top-headlines";
    private static final String APIKey = "6b979057f3a5439f80e75ebf0bf8de4e";
    //private final NewsArticlesActivity newsArticlesActivity;
    private final MainActivity mainActivity;
    private final String sources;

//    public NewsArticlesDownloader(NewsArticlesActivity newsArticlesActivity, String sources) {
//        this.newsArticlesActivity = newsArticlesActivity;
//        this.sources = sources;
//    }

    public NewsArticlesDownloader(MainActivity mainActivity, String sources) {
        this.mainActivity = mainActivity;
        this.sources = sources;
    }

    @Override
    public void run() {
        Log.d(TAG, "run: ");
        Uri.Builder buildURL = Uri.parse(URL).buildUpon();
        buildURL.appendQueryParameter("sources", sources);
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
                                ArrayList<NewsArticles> lstNewsArticles = parseJSON(response.toString());
                                mainActivity.runOnUiThread(() -> mainActivity.updateNewsArticlesData(lstNewsArticles));
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
    }

    private ArrayList<NewsArticles> parseJSON(String s) {
        Log.d(TAG, "parseJSON: ");
        try {
            JSONObject jobjMain = new JSONObject(s);
            JSONArray articles=jobjMain.getJSONArray("articles");
            ArrayList<NewsArticles> lstObjArticles = new ArrayList<>();
            for(int i=0;i<articles.length();i++){
                JSONObject jobjNewsArticles = (JSONObject) articles.get(i);
                JSONObject jobjNewsArticles_Sources = jobjNewsArticles.getJSONObject("source");
                HashMap<String, String> jobjNewsArticles_Id_Name = new HashMap<>();
                jobjNewsArticles_Id_Name.put(jobjNewsArticles_Sources.getString("id"),jobjNewsArticles_Sources.getString("name"));
                String jobjNewsArticles_Author = jobjNewsArticles.has("author")?jobjNewsArticles.getString("author"):"";
                String jobjNewsArticles_Title = jobjNewsArticles.has("title")?jobjNewsArticles.getString("title"):"";
                String jobjNewsArticles_Description = jobjNewsArticles.has("description")?jobjNewsArticles.getString("description"):"";
                String jobjNewsArticles_Url = jobjNewsArticles.has("url")?jobjNewsArticles.getString("url"):"";
                String jobjNewsArticles_UrlToImage = jobjNewsArticles.has("urlToImage")?jobjNewsArticles.getString("urlToImage"):"";
                String jobjNewsArticles_PublishedAt = jobjNewsArticles.has("publishedAt")?jobjNewsArticles.getString("publishedAt"):"";

                lstObjArticles.add(new NewsArticles(jobjNewsArticles_Id_Name,jobjNewsArticles_Author,jobjNewsArticles_Title,jobjNewsArticles_Description,jobjNewsArticles_Url,jobjNewsArticles_UrlToImage,jobjNewsArticles_PublishedAt));
            }
            return lstObjArticles;

        } catch (JSONException e) {
            Log.e(TAG, "parseJSON: ",e );
            e.printStackTrace();
        }
        return null;
    }
}
