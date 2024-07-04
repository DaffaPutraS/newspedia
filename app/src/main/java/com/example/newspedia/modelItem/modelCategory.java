package com.example.newspedia.modelItem;

import java.io.Serializable;

public class modelCategory  implements Serializable {
  private String nameCategory;

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }
    public modelCategory(){

    }
    public modelCategory(String nameCategory){
        this.nameCategory = nameCategory;
    }
}
