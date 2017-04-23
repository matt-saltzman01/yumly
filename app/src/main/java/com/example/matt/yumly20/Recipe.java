package com.example.matt.yumly20;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Isaac on 4/23/2017.
 */

public class Recipe {

    public final String DIVIDER = "|+|";

    public String name;
    public ArrayList<Ingredient> ingredients;
    public ArrayList<String> directions;
    public String photoURL;

    public Recipe(String nm, ArrayList igs, ArrayList drs, String purl) {
        name = nm;
        ingredients = igs;
        directions = drs;
        photoURL = purl;
    }

    public String getIngredientsString() {
        String ret = "";
        for (int a = 0; a < ingredients.size(); a++) {
            if (a == ingredients.size() - 1) {
                ret += ingredients.get(a);
            } else {
                ret += ingredients.get(a) + DIVIDER;
            }
        }
        return ret;
    }

    public String getDirectionsString() {
        String ret = "";
        for (int a = 0; a < directions.size(); a++) {
            if (a == directions.size() - 1) {
                ret += directions.get(a);
            } else {
                ret += directions.get(a) + DIVIDER;
            }
        }
        return ret;
    }

    public byte[] getLogoImage(){
        try {
            URL imageUrl = new URL(photoURL);
            URLConnection conn = imageUrl.openConnection();

            InputStream istream = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(istream);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            //We create an array of bytes
            byte[] pic = new byte[1024];
            int current = 0;

            while((current = bis.read(pic, 0, pic.length)) != -1){
                buffer.write(pic, 0, current);
            }

            return buffer.toByteArray();
        } catch (Exception e) {
            System.err.println("Recipe image retrieval error: " + e.toString());
        }
        return new byte[0];
    }

}
