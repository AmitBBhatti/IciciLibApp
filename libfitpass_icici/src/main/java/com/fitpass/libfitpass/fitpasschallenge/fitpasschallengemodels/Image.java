package com.fitpass.libfitpass.fitpasschallenge.fitpasschallengemodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Image implements Serializable
{

@SerializedName("image_url")
@Expose
private String imageUrl;
@SerializedName("image_name")
@Expose
private String imageName;

     private Boolean isSelected=false;
private final static long serialVersionUID = -2040512910908303605L;

public String getImageUrl() {
return imageUrl;
}

public void setImageUrl(String imageUrl) {
this.imageUrl = imageUrl;
}

public String getImageName() {
return imageName;
}

public void setImageName(String imageName) {
this.imageName = imageName;
}

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
