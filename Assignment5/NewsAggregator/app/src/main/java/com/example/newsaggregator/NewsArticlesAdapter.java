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
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
            if (articlesList!=null && articlesList.size()>0) {
                NewsArticles newsArticles = articlesList.get(position);
                if(newsArticles!=null){
                    holder.txtArticleHeading.setText(newsArticles.getNewsArticle_Title());
                    //holder.txtArticleDate.setText(newsArticles.getNewsArticle_PublishedAt());

                    String publishedDate = newsArticles.getNewsArticle_PublishedAt();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
                    String publishedDateChangeFormat="";
                    try {
                        Date dt = format.parse(publishedDate);
                        SimpleDateFormat changedDateFmt = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.ENGLISH);
                        if (dt != null) {
                            publishedDateChangeFormat = changedDateFmt.format(dt);
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    holder.txtArticleDate.setText(publishedDateChangeFormat);

                    String img = newsArticles.getNewsArticle_UrlToImage();
                    if(img == null || img.isEmpty()) {
                        holder.imgArticlesImage.setImageResource(R.drawable.noimage);
                    }
                    else {
                        Picasso.get()
                                .load(img)
                                .fit()
                                .error(R.drawable.brokenimage)
                                .placeholder(R.drawable.loading)
                                .into(holder.imgArticlesImage);
                        }

                    holder.txtArticlesDescription.setText(newsArticles.getNewsArticle_Description());
                    holder.txtArticlesCounter.setText((position+1) +" of "+(articlesList.size()));
                }

                holder.txtArticleHeading.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = newsArticles.getNewsArticle_Url();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        mainActivity.startActivity(intent);
                    }
                });

                holder.imgArticlesImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = newsArticles.getNewsArticle_Url();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        mainActivity.startActivity(intent);
                    }
                });

                holder.txtArticlesDescription.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = newsArticles.getNewsArticle_Url();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        mainActivity.startActivity(intent);
                    }
                });
            }
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
