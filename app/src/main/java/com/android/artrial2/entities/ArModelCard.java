package com.android.artrial2.entities;

public class ArModelCard {
    public String heritageName;
    public String heritageImage;
    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeritageName() {
        return heritageName;
    }

    public void setHeritageName(String heritageName) {
        this.heritageName = heritageName;
    }

    public String getHeritageImage() {
        return heritageImage;
    }

    public void setHeritageImage(String heritageImage) {
        this.heritageImage = heritageImage;
    }

    public ArModelCard(String heritageName, String heritageImage, int id) {
        this.heritageName = heritageName;
        this.heritageImage = heritageImage;
        this.id = id;
    }
}
