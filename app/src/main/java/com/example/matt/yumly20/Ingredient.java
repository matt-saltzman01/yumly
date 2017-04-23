package com.example.matt.yumly20;

/**
 * Created by Isaac on 4/17/2017.
 */

public class Ingredient {

    public final String DIVIDER = "<|>";

    public String amount;
    public String name;

    public Ingredient(String a, String n) {
        amount = a;
        name = n;
    }

    public Ingredient(String full) throws StringFormatException {
        String[] items = full.split(DIVIDER);
        if (items.length != 2) {
            throw new StringFormatException(String.format(
                    "New ingredient has incorrect format: \"%s\" Should be: \"amount%sname\".",
                    full, DIVIDER));
        } else {
            amount = items[0];
            name = items[1];
        }
    }

    public String toString() {
        return String.format("%s%s%s", amount, DIVIDER, name);
    }

}
