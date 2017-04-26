package com.example.matt.yumly20;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

/**
 * Created by Isaac on 4/16/2017.
 */
public class SearchRecipesAdapter extends ArrayAdapter<String[]> {

    private final Context context;
    private List recipes;

    public SearchRecipesAdapter(Context c, int layoutId, List rItems) {
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

        return recipesView;
    }



}