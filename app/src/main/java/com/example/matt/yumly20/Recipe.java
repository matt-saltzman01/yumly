package com.example.matt.yumly20;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

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

    public static final String DIVIDER = "~~~split~~~";

    public String name;
    public String id;
    public ArrayList<Ingredient> ingredients;
    public ArrayList<String> directions;
    public String photoURL;
    public boolean saved = false;
    public byte[] photoData;

    public Recipe() {}

    public Recipe(String nm, String i, ArrayList igs, ArrayList drs, String purl) {
        name = nm;
        id = i;
        ingredients = igs;
        directions = drs;
        photoURL = purl;
    }

    public Recipe(SQLiteDatabase recipesDB, String nm, String i, ArrayList igs, ArrayList drs,
                  String purl)
            throws StringFormatException{
        String sql = String.format("SELECT * FROM Recipes WHERE id='%s'", i);
        Cursor cursor = recipesDB.rawQuery(sql, new String[] {});
        if (cursor.getCount() == 0) {
            name = nm;
            id = i;
            ingredients = igs;
            directions = drs;
            photoURL = purl;
        } else {
            cursor.moveToFirst();
            name = cursor.getString(0);
            id = cursor.getString(1);
            parseIngredients(cursor.getString(2));
            parseDirections(cursor.getString(3));
            photoURL = cursor.getString(4);
            saved = true;
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    public Recipe(SQLiteDatabase recipesDB, String keyId) throws StringFormatException {
        String sql = String.format("SELECT * FROM Recipes WHERE id='%s'", keyId);
        Cursor cursor = recipesDB.rawQuery(sql, new String[] {});
        if (cursor.getCount() == 0) {

        }
        if(cursor.moveToFirst()){
            name = cursor.getString(0);
            id = cursor.getString(1);
            parseIngredients(cursor.getString(2));
            parseDirections(cursor.getString(3));
            photoURL = cursor.getString(4);
            saved = true;
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

    }

    public void saveToDB(SQLiteDatabase recipesDB) {

        String sql = "INSERT OR REPLACE INTO Recipes (name, id, ingredients, directions, photourl)"
                + " VALUES(?, ?, ?, ?, ?)";
        SQLiteStatement insertStatement = recipesDB.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindString(1, name);
        insertStatement.bindString(2, id);
        insertStatement.bindString(3, getIngredientsString());
        insertStatement.bindString(4, getDirectionsString());
        insertStatement.bindString(5, photoURL);
        insertStatement.executeInsert();
        recipesDB.close();
        saved = true;
    }

    public void deleteFromDB(SQLiteDatabase recipesDB) {

        if (saved) {
            String sql = String.format("DELETE FROM Recipes WHERE id='%s'", id);
            SQLiteStatement insertStatement = recipesDB.compileStatement(sql);
            insertStatement.executeUpdateDelete();
            recipesDB.close();
            saved = false;
        }
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

    public void parseIngredients(String text) throws StringFormatException{

        if (ingredients == null) {
            ingredients = new ArrayList();
        }

        String[] items = text.split(DIVIDER);
        for (int a = 0; a < items.length; a++) {
            ingredients.add(new Ingredient(items[a]));
        }
    }

    public void parseDirections(String text) throws StringFormatException{

        if (directions == null) {
            directions = new ArrayList();
        }

        String[] items = text.split(DIVIDER);
        for (int a = 0; a < items.length; a++) {
            directions.add(items[a]);
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
