package com.example.newsaggregator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewsArticlesAdapter extends RecyclerView.Adapter<NewsArticlesViewHolder> {

    private static final String TAG = "NewsArticlesAdapter";
    private final MainActivity mainActivity;
    private final ArrayList<NewsArticles> articlesList;

    public NewsArticlesAdapter(MainActivity mainActivity, ArrayList<NewsArticles> articlesList){
        this.mainActivity = mainActivity;
        this.articlesList = articlesList;
    }
    @NonNull
    @Override
    public NewsArticlesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsArticlesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_news_articles, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull NewsArticlesViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        try {
            NewsArticles newsArticles = articlesList.get(position);
            if(newsArticles!=null){
                holder.txtArticleHeading.setText(newsArticles.getNewsArticle_Title());

                if(newsArticles.getNewsArticle_PublishedAt()!=null && !newsArticles.getNewsArticle_PublishedAt().isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    Date d = sdf.parse(newsArticles.getNewsArticle_PublishedAt());
                    sdf.applyPattern("MMM d, yyyy HH:mm");
                    String finaloutputdate = sdf.format(d);
                    holder.txtArticleDate.setText(finaloutputdate);
                }
                holder.txtArticleAuthor.setText(newsArticles.getNewsArticle_Author());

                String urlToImage = newsArticles.getNewsArticle_UrlToImage();
                if(!TextUtils.isEmpty(urlToImage)) {
                    try {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);
                        InputStream in = new java.net.URL(urlToImage).openStream();
                        Bitmap mIcon11 = BitmapFactory.decodeStream(in);
                        holder.imgArticlesImage.setImageBitmap(mIcon11);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("Error",e.getMessage());
                        holder.imgArticlesImage.setImageResource(R.drawable.brokenimage);
                    }
                }
                else{
                    holder.imgArticlesImage.setImageResource(R.drawable.noimage);
                }
                holder.txtArticlesDescription.setText(newsArticles.getNewsArticle_Description());
                holder.txtArticlesCounter.setText((position+1) +" of "+(articlesList.size()));
            }

            holder.txtArticleHeading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = newsArticles.getNewsArticle_Url();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    mainActivity.startActivity(i);
                }
            });

            holder.imgArticlesImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = newsArticles.getNewsArticle_Url();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    mainActivity.startActivity(i);
                }
            });

            holder.txtArticlesDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = newsArticles.getNewsArticle_Url();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    mainActivity.startActivity(i);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onBindViewHolder: ", e);
        }
    }

    @Override
    public int getItemCount() {
        return articlesList.size();
    }
}
