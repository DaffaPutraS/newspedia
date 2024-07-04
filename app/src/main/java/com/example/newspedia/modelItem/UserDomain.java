package com.example.newspedia.modelItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; // Corrected import

public class UserDomain {
    private String userId;
    private String email;
    private String username;
    private HashMap<String, modelNews> bookmarks;

    public UserDomain() {
        // Empty constructor needed for Firebase
    }

    public HashMap<String, modelNews> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(HashMap<String, modelNews> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Corrected method with proper import
    public List<modelNews> bookmarksToList() {
        return new ArrayList<>(bookmarks.values());
    }
}