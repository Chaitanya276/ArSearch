package com.android.artrial2.entities;

import java.util.ArrayList;

public class ObjectFile {
    public ArrayList<String> image_name;
    public ArrayList<String> model_name;

    public ArrayList<String> getImage_name() {
        return image_name;
    }

    public void setImage_name(ArrayList<String> image_name) {
        this.image_name = image_name;
    }

    public ArrayList<String> getModel_name() {
        return model_name;
    }

    public void setModel_name(ArrayList<String> model_name) {
        this.model_name = model_name;
    }

    public ObjectFile(ArrayList<String> image_name, ArrayList<String> model_name) {
        this.image_name = image_name;
        this.model_name = model_name;
    }
}
