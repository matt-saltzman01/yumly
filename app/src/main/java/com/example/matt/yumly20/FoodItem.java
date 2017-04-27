package com.example.matt.yumly20;

/**
 * Created by Isaac on 4/16/2017.
 */

public class FoodItem {

    public String food;
    public String group;
    public String photoURL;
    public boolean checked;

    public FoodItem(String f, String g, String url) {
        food = f;
        group = g;
        photoURL = url;
        checked = false;
    }
}
