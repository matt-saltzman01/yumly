package com.example.matt.yumly20;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Isaac on 4/16/2017.
 */
public class RecipesAdapter extends ArrayAdapter<String[]> {

    private final Context context;
    private List recipes;

    public RecipesAdapter(Context c, int layoutId, List rItems) {
        super(c, layoutId, rItems);
        context = c;
        recipes = rItems;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View recipesView = convertView;

        if (recipesView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            recipesView = inflater.inflate(R.layout.my_recipes_item, parent, false);
        }

        final ImageView pic = (ImageView) recipesView.findViewById(R.id.recipe_item_image);
        final TextView name = (TextView) recipesView.findViewById(R.id.recipe_item_title);

        RecipePreview rPreview = (RecipePreview) recipes.get(position);

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.loadImage(rPreview.miniPhotoURL, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                pic.setImageBitmap(loadedImage);
                pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        });

        name.setText(rPreview.name);

        /*int image;
        switch (item) {
            case "Tacos":
                image = R.drawable.tacos;
                break;
            case "Pasta":
                image = R.drawable.pasta;
                break;
            case "Everyday Baked Chicken":
                image = R.drawable.everydaybakedchicken;
                break;
            case "Crab Cake":
                image = R.drawable.crabcake;
                break;
            case "Fried Rice":
                image = R.drawable.friedrice;
                break;
            case "Guacamole":
                image = R.drawable.guacamole;
                break;
            case "Ramen":
                image = R.drawable.ramen;
                break;
            case "Salmon":
                image = R.drawable.salmon;
                break;
            case "Brownies":
                image = R.drawable.brownies;
                break;
            case "Burger":
                image = R.drawable.burger;
                break;
            default:
                image = -1;
                break;
        }

        if (image != -1) {
            pic.setImageBitmap(BitmapFactory.decodeResource(
                    recipesView.getContext().getResources(), image));
            pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }*/

        return recipesView;
    }



}