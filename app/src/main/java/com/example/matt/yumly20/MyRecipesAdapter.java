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
public class MyRecipesAdapter extends ArrayAdapter<String[]> {

    private final Context context;
    private List recipes;

    public MyRecipesAdapter(Context c, int layoutId, List rItems) {
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

        Recipe rec = (Recipe) recipes.get(position);

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.loadImage(rec.photoURL, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                pic.setImageBitmap(loadedImage);
                pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        });

        name.setText(rec.name);

        return recipesView;
    }
}