package com.example.newspedia.modelItem;

import java.util.ArrayList;

public class UserDomain {
    private String userId;
    private String email;
    private String username;
    private ArrayList<modelNews> bookmarks;

    public UserDomain() {
        // Empty constructor needed for Firebase
        bookmarks = new ArrayList<>();
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
}
