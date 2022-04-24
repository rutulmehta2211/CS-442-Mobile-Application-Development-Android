package com.example.newsaggregator;

import java.util.HashMap;

public class NewsArticles {
    private final HashMap<String,String> newsArticles_Id_Name;
    private final String newsArticle_Author;
    private final String newsArticle_Title;
    private final String newsArticle_Description;
    private final String newsArticle_Url;
    private final String newsArticle_UrlToImage;
    private final String newsArticle_PublishedAt;

    public NewsArticles(HashMap<String, String> newsArticles_id_name, String newsArticle_author, String newsArticle_title, String newsArticle_description, String newsArticle_url, String newsArticle_urlToImage, String newsArticle_publishedAt) {
        newsArticles_Id_Name = newsArticles_id_name;
        newsArticle_Author = newsArticle_author;
        newsArticle_Title = newsArticle_title;
        newsArticle_Description = newsArticle_description;
        newsArticle_Url = newsArticle_url;
        newsArticle_UrlToImage = newsArticle_urlToImage;
        newsArticle_PublishedAt = newsArticle_publishedAt;
    }

    public HashMap<String, String> getNewsArticles_Id_Name() {
        return newsArticles_Id_Name;
    }

    public String getNewsArticle_Author() {
        return newsArticle_Author;
    }

    public String getNewsArticle_Title() {
        return newsArticle_Title;
    }

    public String getNewsArticle_Description() {
        return newsArticle_Description;
    }

    public String getNewsArticle_Url() {
        return newsArticle_Url;
    }

    public String getNewsArticle_UrlToImage() {
        return newsArticle_UrlToImage;
    }

    public String getNewsArticle_PublishedAt() {
        return newsArticle_PublishedAt;
    }
}
