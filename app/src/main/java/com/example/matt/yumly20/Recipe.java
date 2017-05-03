package com.example.matt.yumly20;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Isaac on 4/23/2017.
 */

public class Recipe {

    public static final String DIVIDER = "~~~split~~~";
    public static final String NUTDIV = "~~nut~~";

    public Context context;

    public String name;
    public String id;
    public ArrayList<Ingredient> ingredients;
    public String directions;
    public HashMap<String, Float> nutrition;
    public String photoURL;
    public boolean saved = false;
    public byte[] photoData;

    public Recipe() {}

    public Recipe(Context c, String nm, String i, ArrayList igs, String drs, HashMap ns,
                  String purl) {
        context = c;
        name = nm;
        id = i;
        ingredients = igs;
        directions = drs;
        nutrition = ns;
        photoURL = purl;
    }

    public Recipe(Context c, SQLiteDatabase recipesDB, String nm, String i, ArrayList igs,
                  String drs, HashMap ns, String purl)
            throws StringFormatException {
        context = c;
        String sql = String.format("SELECT * FROM Recipes WHERE id='%s'", i);
        Cursor cursor = recipesDB.rawQuery(sql, new String[] {});
        if (cursor.getCount() == 0) {
            name = nm;
            id = i;
            ingredients = igs;
            directions = drs;
            nutrition = ns;
            photoURL = purl;
        } else {
            cursor.moveToFirst();
            name = cursor.getString(0);
            id = cursor.getString(1);
            parseIngredients(cursor.getString(2));
            directions = cursor.getString(3);
            parseNutrition(cursor.getString(4));
            photoURL = cursor.getString(5);
            saved = true;
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    public Recipe(Context c, SQLiteDatabase recipesDB, String keyId) throws StringFormatException {
        context = c;
        String sql = String.format("SELECT * FROM Recipes WHERE id='%s'", keyId);
        Cursor cursor = recipesDB.rawQuery(sql, new String[] {});
        if (cursor.getCount() == 0) {

        }
        if(cursor.moveToFirst()){
            name = cursor.getString(0);
            id = cursor.getString(1);
            parseIngredients(cursor.getString(2));
            directions = cursor.getString(3);
            parseNutrition(cursor.getString(4));
            photoURL = cursor.getString(5);
            saved = true;
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

    }

    public void saveToDB(SQLiteDatabase recipesDB) {

        String sql = "INSERT OR REPLACE INTO Recipes (name, id, ingredients, directions," +
                " nutrition, photourl) VALUES(?, ?, ?, ?, ?, ?)";

        SQLiteStatement insertStatement = recipesDB.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindString(1, name);
        insertStatement.bindString(2, id);
        insertStatement.bindString(3, getIngredientsString());
        insertStatement.bindString(4, directions);
        insertStatement.bindString(5, getNutritionString());
        insertStatement.bindString(6, photoURL);
        insertStatement.executeInsert();
        savePhotoToStorage(recipesDB);
        saved = true;
    }

    public void deleteFromDB(SQLiteDatabase recipesDB) {

        if (saved) {
            String sql = String.format("DELETE FROM Recipes WHERE id='%s'", id);
            SQLiteStatement insertStatement = recipesDB.compileStatement(sql);
            insertStatement.executeUpdateDelete();

            saved = false;
        }
    }

    private void savePhotoToStorage(final SQLiteDatabase recipesDB){

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.loadImage(photoURL, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                ContextWrapper cw = new ContextWrapper(context);
                // path to /data/data/yourapp/app_data/imageDir
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                // Create imageDir
                File mypath = new File(directory, name.replace(' ', '+') + ".jpg");

                if (!mypath.exists()) {
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(mypath);
                        // Use the compress method on the BitMap object to write image to the OutputStream
                        loadedImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        photoURL = "file://" + mypath.toString();
                        System.out.println("~~~~~~~~~~~~~" + photoURL);
                        String sql = "INSERT OR REPLACE INTO Recipes (name, id, ingredients, " +
                                "directions, nutrition, photourl) VALUES(?, ?, ?, ?, ?, ?)";
                        SQLiteStatement insertStatement = recipesDB.compileStatement(sql);
                        insertStatement.clearBindings();
                        insertStatement.bindString(1, name);
                        insertStatement.bindString(2, id);
                        insertStatement.bindString(3, getIngredientsString());
                        insertStatement.bindString(4, directions);
                        insertStatement.bindString(5, getNutritionString());
                        insertStatement.bindString(6, photoURL);
                        insertStatement.executeInsert();
                        saved = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
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

    public String getNutritionString() {

        String ret = "";
        Set<String> keys = nutrition.keySet();
        int curr = 0;

        for (String key : keys) {

            if (curr == keys.size() - 1) {
                ret += String.format("%s%s%f", key, NUTDIV, nutrition.get(key));
            } else {
                ret += String.format("%s%s%f%s", key, NUTDIV, nutrition.get(key), DIVIDER);
            }
            curr++;
        }

        return ret;
    }

    /*public String getDirectionsString() {
        String ret = "";
        for (int a = 0; a < directions.size(); a++) {
            if (a == directions.size() - 1) {
                ret += directions.get(a);
            } else {
                ret += directions.get(a) + DIVIDER;
            }
        }
        return ret;
    }*/

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

    public void parseIngredients(String text) throws StringFormatException{

        if (ingredients == null) {
            ingredients = new ArrayList();
        }

        String[] items = text.split(DIVIDER);
        for (int a = 0; a < items.length; a++) {
            ingredients.add(new Ingredient(items[a]));
        }
    }

    public String[] parseDirections() throws StringFormatException {

        String[] ret = directions.split(DIVIDER);

        if (ret.length != 2) {
            throw new StringFormatException("Directions had the incorrect number of parts.");
        } else {
            return directions.split(DIVIDER);
        }
    }

    public void parseNutrition(String text) throws StringFormatException {

        if (nutrition == null) {
            nutrition = new HashMap<>();
        }

        String[] items = text.split(DIVIDER);
        for (int a = 0; a < items.length; a++) {
            String[] parts = items[a].split(NUTDIV);
            nutrition.put(parts[0], Float.parseFloat(parts[1]));
        }
    }

    public void loadImage(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        if(cursor.getCount() == 0){
            //return null;
        } else {
           // return this;
        }
    }

}
