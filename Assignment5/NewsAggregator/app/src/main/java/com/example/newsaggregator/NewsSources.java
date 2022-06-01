package com.example.newsaggregator;

import java.io.Serializable;
import java.util.HashMap;

public class NewsSources implements Serializable {
    private final String newsSources_Id;
    private final String newsSources_Name;
    private final String newsSources_Category;
    private final String color_Category;

    public NewsSources(String newsSources_id, String newsSources_name, String newsSources_category, String color_Category) {
        this.newsSources_Id = newsSources_id;
        this.newsSources_Name = newsSources_name;
        this.newsSources_Category = newsSources_category;
        this.color_Category = color_Category;
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

    public String getColor_Category() { return color_Category; }
}
