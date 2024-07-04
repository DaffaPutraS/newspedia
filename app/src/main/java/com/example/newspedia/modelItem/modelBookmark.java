package com.example.newspedia.modelItem;

public class modelBookmark {
    private String newsId;
    private String userId;

    public modelBookmark() {
        // Default constructor required for calls to DataSnapshot.getValue(modelBookmark.class)
    }

    public modelBookmark(String newsId, String userId) {
        this.newsId = newsId;
        this.userId = userId;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
