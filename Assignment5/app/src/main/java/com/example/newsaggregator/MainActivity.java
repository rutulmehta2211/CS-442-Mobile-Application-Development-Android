package com.example.newsaggregator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
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
        drawerlayout.closeDrawer(drawermenu);
        NewsSources objNewsSource = lstNewsSources.get(position);
        getSupportActionBar().setTitle(objNewsSource.getNewsSources_Name());
        loadArticlesData(objNewsSource.getNewsSources_Id());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        opt_menu = menu;
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            Log.d(TAG, "onOptionsItemSelected: "+item);
            return true;
        }
        Log.d(TAG, "onOptionsItemSelected: "+item.getTitle());
        ArrayList<NewsSources> lstfilteredData = new ArrayList<>();
        CharSequence title = item.getTitle();
        if ("All".equals(title)) {
            ArrayList<String> lstall = new ArrayList<>();
            for (NewsSources objNewsSources : lstNewsSources) {
                lstall.add(objNewsSources.getNewsSources_Name());
            }
            getSupportActionBar().setTitle("News Gateway ("+lstall.size()+")");
            NewsSourcearrayAdapter = new ArrayAdapter<>(this, R.layout.drawer_items, lstall);
            drawermenu.setAdapter(NewsSourcearrayAdapter);
        } else if ("business".equals(title)) {
            lstfilteredData.clear();
            lstfilteredData = hmObjNewsSources.get("business");
            ArrayList<String> lstbusiness = new ArrayList<>();
            for (NewsSources objNewsSources : lstfilteredData) {
                lstbusiness.add(objNewsSources.getNewsSources_Name());
            }
            getSupportActionBar().setTitle("News Gateway ("+lstbusiness.size()+")");
            NewsSourcearrayAdapter = new ArrayAdapter<>(this, R.layout.drawer_items, lstbusiness);
            drawermenu.setAdapter(NewsSourcearrayAdapter);
        } else if ("entertainment".equals(title)) {
            lstfilteredData.clear();
            lstfilteredData = hmObjNewsSources.get("entertainment");
            ArrayList<String> lstentertainment = new ArrayList<>();
            for (NewsSources objNewsSources : lstfilteredData) {
                lstentertainment.add(objNewsSources.getNewsSources_Name());
            }
            getSupportActionBar().setTitle("News Gateway ("+lstentertainment.size()+")");
            NewsSourcearrayAdapter = new ArrayAdapter<>(this, R.layout.drawer_items, lstentertainment);
            drawermenu.setAdapter(NewsSourcearrayAdapter);
        } else if ("general".equals(title)) {
            lstfilteredData.clear();
            lstfilteredData = hmObjNewsSources.get("general");
            ArrayList<String> lstgeneral = new ArrayList<>();
            for (NewsSources objNewsSources : lstfilteredData) {
                lstgeneral.add(objNewsSources.getNewsSources_Name());
            }
            getSupportActionBar().setTitle("News Gateway ("+lstgeneral.size()+")");
            NewsSourcearrayAdapter = new ArrayAdapter<>(this, R.layout.drawer_items, lstgeneral);
            drawermenu.setAdapter(NewsSourcearrayAdapter);
        } else if ("health".equals(title)) {
            lstfilteredData.clear();
            lstfilteredData = hmObjNewsSources.get("health");
            ArrayList<String> lsthealth = new ArrayList<>();
            for (NewsSources objNewsSources : lstfilteredData) {
                lsthealth.add(objNewsSources.getNewsSources_Name());
            }
            getSupportActionBar().setTitle("News Gateway ("+lsthealth.size()+")");
            NewsSourcearrayAdapter = new ArrayAdapter<>(this, R.layout.drawer_items, lsthealth);
            drawermenu.setAdapter(NewsSourcearrayAdapter);
        } else if ("science".equals(title)) {
            lstfilteredData.clear();
            lstfilteredData = hmObjNewsSources.get("science");
            ArrayList<String> lstscience = new ArrayList<>();
            for (NewsSources objNewsSources : lstfilteredData) {
                lstscience.add(objNewsSources.getNewsSources_Name());
            }
            getSupportActionBar().setTitle("News Gateway ("+lstscience.size()+")");
            NewsSourcearrayAdapter = new ArrayAdapter<>(this, R.layout.drawer_items, lstscience);
            drawermenu.setAdapter(NewsSourcearrayAdapter);
        } else if ("sports".equals(title)) {
            lstfilteredData.clear();
            lstfilteredData = hmObjNewsSources.get("sports");
            ArrayList<String> lstsports = new ArrayList<>();
            for (NewsSources objNewsSources : lstfilteredData) {
                lstsports.add(objNewsSources.getNewsSources_Name());
            }
            getSupportActionBar().setTitle("News Gateway ("+lstsports.size()+")");
            NewsSourcearrayAdapter = new ArrayAdapter<>(this, R.layout.drawer_items, lstsports);
            drawermenu.setAdapter(NewsSourcearrayAdapter);
        } else if ("technology".equals(title)) {
            lstfilteredData.clear();
            lstfilteredData = hmObjNewsSources.get("technology");
            ArrayList<String> lsttechnology = new ArrayList<>();
            for (NewsSources objNewsSources : lstfilteredData) {
                lsttechnology.add(objNewsSources.getNewsSources_Name());
            }
            getSupportActionBar().setTitle("News Gateway ("+lsttechnology.size()+")");
            NewsSourcearrayAdapter = new ArrayAdapter<>(this, R.layout.drawer_items, lsttechnology);
            drawermenu.setAdapter(NewsSourcearrayAdapter);
        } else {
            ArrayList<String> lstdefault = new ArrayList<>();
            for (NewsSources objNewsSources : lstNewsSources) {
                lstdefault.add(objNewsSources.getNewsSources_Name());
            }
            getSupportActionBar().setTitle("News Gateway ("+lstdefault.size()+")");
            NewsSourcearrayAdapter = new ArrayAdapter<>(this, R.layout.drawer_items, lstdefault);
            drawermenu.setAdapter(NewsSourcearrayAdapter);
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateNewsSourcesData(ArrayList<NewsSources> newsSources) {
        Log.d(TAG, "updateNewsSourcesData: ");
        try {
            if(newsSources!=null && newsSources.size()>0){
                getSupportActionBar().setTitle("News Gateway ("+newsSources.size()+")");
                lstNewsSources = newsSources;
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
                opt_menu.add("All");
                for(String s:objCategory){
                    opt_menu.add(s);
                }
                NewsSourcearrayAdapter = new ArrayAdapter<>(this, R.layout.drawer_items, objNewsSourceName);
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