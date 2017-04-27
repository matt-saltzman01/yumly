package com.example.matt.yumly20;

import java.util.Comparator;

/**
 * Created by Isaac on 4/27/2017.
 */

public class FoodItemComparator implements Comparator<FoodItem> {

    @Override
    public int compare(FoodItem i1, FoodItem i2) {
        return i1.food.compareTo(i2.food);
    }

}
