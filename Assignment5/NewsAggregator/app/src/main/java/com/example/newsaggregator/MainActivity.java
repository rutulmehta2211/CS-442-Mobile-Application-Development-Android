package com.example.newsaggregator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DrawerLayout drawerlayout;
    private ListView drawermenu;
    private ActionBarDrawerToggle toggle;
    private Menu opt_menu;
    private ViewPager2 pager;

    private ArrayAdapter<String> NewsSourcearrayAdapter;
    NewsArticlesAdapter newsArticlesAdapter;

    ArrayList<NewsSources> lstNewsSources;
    private final HashMap<String, ArrayList<NewsSources>> hmObjNewsSources = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = findViewById(R.id.pager);
        drawerlayout = findViewById(R.id.drawerlayout);

        drawermenu = findViewById(R.id.drawermenu);
        drawermenu.setAdapter(new ArrayAdapter<>(this,R.layout.drawer_items));
        drawermenu.setOnItemClickListener(
                (parent, view, position, id) -> selectItem(position)
        );

        toggle = new ActionBarDrawerToggle(this,drawerlayout,R.string.main_activity_open_drawer,R.string.main_activity_close_drawer);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        loadData();
    }

    public void loadData(){
        Log.d(TAG, "loadData: ");
        if (hasNetworkConnection()) {
            NewsSourcesDownloader newsSourcesDownloader = new NewsSourcesDownloader(MainActivity.this);
            new Thread(newsSourcesDownloader).start();
        } else {
            Toast.makeText(MainActivity.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
    }

    public void loadArticlesData(String SourceName){
        Log.d(TAG, "loadArticlesData: ");
        if (hasNetworkConnection()) {
            NewsArticlesDownloader newsArticlesDownloader = new NewsArticlesDownloader(MainActivity.this,SourceName);
            new Thread(newsArticlesDownloader).start();
        } else {
            Toast.makeText(MainActivity.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = null;
        connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        toggle.onConfigurationChanged(newConfig);
    }

    private void selectItem(int position) {
        Log.d(TAG, "selectItem: ");
        try {
            drawerlayout.closeDrawer(drawermenu);
            if(lstNewsSources!=null && lstNewsSources.size()>0) {
                NewsSources objNewsSource = lstNewsSources.get(position);
                getSupportActionBar().setTitle(objNewsSource.getNewsSources_Name());
                loadArticlesData(objNewsSource.getNewsSources_Id());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "selectItem: ", e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        opt_menu = menu;
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: ");
        try {
            if (toggle.onOptionsItemSelected(item)) {
                Log.d(TAG, "onOptionsItemSelected: "+item);
                return true;
            }
            if(hmObjNewsSources!=null) {
                lstNewsSources = hmObjNewsSources.get(item.getTitle().toString());
                ArrayList<String> lstfiltereStringData = new ArrayList<>();
                for (NewsSources objNewsSources : lstNewsSources) { //here
                    lstfiltereStringData.add(objNewsSources.getNewsSources_Name());
                }
                Collections.sort(lstfiltereStringData);
                getSupportActionBar().setTitle("News Gateway (" + lstfiltereStringData.size() + ")");
                NewsSourcearrayAdapter = new ArrayAdapter<String>(this, R.layout.drawer_items, lstfiltereStringData) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView text = (TextView) view.findViewById(R.id.txtDrawerListItem);
                        text.setTextColor(Color.parseColor(lstNewsSources.get(position).getColor_Category()));
                        return view;
                    }
                };
                drawermenu.setAdapter(NewsSourcearrayAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onOptionsItemSelected: ", e);
        }
        return super.onOptionsItemSelected(item);
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
                color_Category = "#000000";
                break;
        }
        return color_Category;
    }

    public void updateNewsSourcesData(ArrayList<NewsSources> newsSources) {
        Log.d(TAG, "updateNewsSourcesData: ");
        try {
            if(newsSources!=null && newsSources.size()>0){
                getSupportActionBar().setTitle("News Gateway ("+newsSources.size()+")");
                lstNewsSources = newsSources;
                hmObjNewsSources.put("All",newsSources);
                ArrayList<String> objNewsSourceName=new ArrayList<>();
                for(NewsSources objNewsSources:newsSources){
                    ArrayList<NewsSources> val = hmObjNewsSources.get(objNewsSources.getNewsSources_Category());
                    if(val == null)
                        val = new ArrayList<>();
                    val.add(objNewsSources);
                    hmObjNewsSources.put(objNewsSources.getNewsSources_Category(), val);
                    objNewsSourceName.add(objNewsSources.getNewsSources_Name());
                }
                ArrayList<String> objCategory = new ArrayList<>(hmObjNewsSources.keySet());
                Collections.sort(objCategory);
                for(int i=0;i<objCategory.size();i++){
                    String strCategory = objCategory.get(i);
                    opt_menu.add(strCategory);
                }
                for (int i = 0; i < opt_menu.size(); i++) {
                    String colorCategory = GetColorCodeForCategory(opt_menu.getItem(i).toString());
                    MenuItem item = opt_menu.getItem(i);
                    SpannableString spanString = new SpannableString(opt_menu.getItem(i).getTitle().toString());
                    spanString.setSpan(new ForegroundColorSpan(Color.parseColor(colorCategory)), 0,     spanString.length(), 0);
                    item.setTitle(spanString);
                }
                NewsSourcearrayAdapter = new ArrayAdapter<String>(this, R.layout.drawer_items, objNewsSourceName){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView text = (TextView) view.findViewById(R.id.txtDrawerListItem);
                        String color_Category = newsSources.get(position).getColor_Category();
                        text.setTextColor(Color.parseColor(color_Category));
                        return view;
                    }
                };
                drawermenu.setAdapter(NewsSourcearrayAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "updateNewsSourcesData: ", e);
        }
    }

    public void updateNewsArticlesData(ArrayList<NewsArticles> lstNewsArticles) {
        newsArticlesAdapter = new NewsArticlesAdapter(this,lstNewsArticles);
        pager.setAdapter(newsArticlesAdapter);
        pager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
    }
}