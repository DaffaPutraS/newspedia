package com.example.newspedia.modelItem;

import java.io.Serializable;

public class modelNews implements Serializable{
    private String newsId;
    private String category;
    private String datePublish;
    private String description;
    private String imageNews;
    private String nameNews;

    // Default constructor required for calls to DataSnapshot.getValue(modelNews.class)
    public modelNews() {}

    // Getters and setters
    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDatePublish() {
        return datePublish;
    }

    public void setDatePublish(String datePublish) {
        this.datePublish = datePublish;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageNews() {
        return imageNews;
    }

    public void setImageNews(String imageNews) {
        this.imageNews = imageNews;
    }

    public String getNameNews() {
        return nameNews;
    }

    public void setNameNews(String nameNews) {
        this.nameNews = nameNews;
    }
}
