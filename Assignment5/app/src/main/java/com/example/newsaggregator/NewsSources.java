package com.example.newsaggregator;

import java.io.Serializable;
import java.util.HashMap;

public class NewsSources implements Serializable {
    private final String newsSources_Id;
    private final String newsSources_Name;
    private final String newsSources_Category;

    public NewsSources(String newsSources_id, String newsSources_name, String newsSources_category) {
        this.newsSources_Id = newsSources_id;
        this.newsSources_Name = newsSources_name;
        this.newsSources_Category = newsSources_category;
    }

    public String getNewsSources_Id() {
        return newsSources_Id;
    }

    public String getNewsSources_Name() {
        return newsSources_Name;
    }

    public String getNewsSources_Category() {
        return newsSources_Category;
    }
}
