package com.example.matt.yumly20;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

/**
 * Created by Isaac on 4/16/2017.
 */
public class FridgeAdapter extends BaseAdapter {

    private final Context context;
    private List foodItems;

    public FridgeAdapter(Context c, int layoutId, List fItems) {
        context = c;
        foodItems = fItems;
    }

    @Override
    public int getCount() {
        return foodItems.size();
    }

    @Override
    public Object getItem(int position) {
        return foodItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (foodItems.get(position) == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.empty_item, parent, false);
        }

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View fridgeView = inflater.inflate(R.layout.fridge_item, parent, false);

        final ImageView pic = (ImageView) fridgeView.findViewById(R.id.food_picture);
        final TextView name = (TextView) fridgeView.findViewById(R.id.food_name);
        final CheckBox box = (CheckBox) fridgeView.findViewById(R.id.food_check);

        final FoodItem item = (FoodItem) foodItems.get(position);
        final int index = position;

        name.setText(item.food);

        ImageLoader imageLoader = ImageLoader.getInstance();

        System.out.println("Loading image for " + item.food);

        imageLoader.loadImage(item.photoURL, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                pic.setImageBitmap(loadedImage);
                pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                fridgeView.findViewById(R.id.progress_load).setVisibility(View.GONE);
                pic.setVisibility(View.VISIBLE);
            }
        });

        box.setChecked(item.checked);

        fridgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                box.setChecked(!item.checked);
                ((MainActivity) fridgeView.getContext()).selectFromFridge(item);
            }
        });

        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) fridgeView.getContext()).selectFromFridge(item);
            }
        });

        return fridgeView;
    }
}