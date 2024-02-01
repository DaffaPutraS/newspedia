package com.example.newspedia.modelItem;

import java.io.Serializable;

public class modelNews implements Serializable {


    private String name,category,detail,date,poster;


    public modelNews(String name, String category , String detail , String date, String poster){
        this.name = name;
        this.category = category;
        this.detail = detail;
        this.date = date;
        this.poster = poster;
    }
    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDetail() {
        return detail;
    }

    public String getDate() {
        return date;
    }

    public String getPoster() {
        return poster;
    }
}
