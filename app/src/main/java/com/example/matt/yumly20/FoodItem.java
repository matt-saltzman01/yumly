package com.example.matt.yumly20;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Isaac on 4/16/2017.
 */

public class FoodItem {

    public Context context;
    public String food;
    public String group;
    public String photoURL;
    public boolean checked;

    public FoodItem() { }

    public FoodItem(Context c, String f, String g, String url) {
        context = c;
        food = f;
        group = g;
        photoURL = url;
        checked = false;
        downloadPhoto();
    }

    public void downloadPhoto() {

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.loadImage(photoURL, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                ContextWrapper cw = new ContextWrapper(context);
                // path to /data/data/yourapp/app_data/imageDir
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                // Create imageDir
                File mypath = new File(directory, food.replace(' ', '+') + ".jpg");

                if (!mypath.exists()) {
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(mypath);
                        // Use the compress method on the BitMap object to write image to the OutputStream
                        loadedImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        photoURL = "file://" + mypath.toString();
                        System.out.println("~~~~~~~~~~~~~" + photoURL);
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
}
