package com.example.matt.yumly20;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Isaac on 4/23/2017.
 */
public class RecipeOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "cheflyRecipesDB.sql";
    private static final String TABLE_NAME = "Recipes";
    private static final String TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    "name TEXT, " +
                    "id TEXT PRIMARY KEY, " +
                    "ingredients TEXT, " +
                    "directions TEXT, " +
                    "nutrition TEXT, " +
                    "photourl TEXT);";

    RecipeOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //super.onUpgrade(db, oldVersion, newVersion);
    }

}
