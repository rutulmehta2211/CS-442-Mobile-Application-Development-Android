package com.example.newsaggregator;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsArticlesViewHolder extends RecyclerView.ViewHolder {

    LinearLayout linearLayout;
    TextView txtArticleHeading;
    TextView txtArticleDate;
    TextView txtArticleAuthor;
    ImageView imgArticlesImage;
    TextView txtArticlesDescription;
    TextView txtArticlesCounter;

    public NewsArticlesViewHolder(@NonNull View itemView) {
        super(itemView);
        linearLayout = itemView.findViewById(R.id.linearLayoutNA);
        txtArticleHeading = itemView.findViewById(R.id.txtArticleHeadingNA);
        txtArticleDate = itemView.findViewById(R.id.txtArticleDateNA);
        txtArticleAuthor = itemView.findViewById(R.id.txtArticleAuthorNA);
        imgArticlesImage = itemView.findViewById(R.id.imgArticlesImageNA);
        txtArticlesDescription = itemView.findViewById(R.id.txtArticlesDescriptionNA);
        txtArticlesCounter = itemView.findViewById(R.id.txtArticlesCounterNA);
    }
}
