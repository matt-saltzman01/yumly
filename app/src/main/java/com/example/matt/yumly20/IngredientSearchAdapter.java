package com.example.matt.yumly20;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Isaac on 4/20/2017.
 */
public class IngredientSearchAdapter extends ArrayAdapter<String[]> {

    private final Context context;
    private List foodItems;

    public IngredientSearchAdapter(Context c, int layoutId, List fItems) {
        super(c, layoutId, fItems);
        context = c;
        foodItems = fItems;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (foodItems.get(position) == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.empty_item, parent, false);
        }

        View ingredSearchView = convertView;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ingredSearchView = inflater.inflate(R.layout.ingredient_search_item, parent, false);

        LinearLayout ll = (LinearLayout) ingredSearchView.findViewById(R.id.isi_linear);
        ImageView pic = (ImageView) ingredSearchView.findViewById(R.id.ingredient_search_image);
        TextView name = (TextView) ingredSearchView.findViewById(R.id.ingredient_search_text);

        FoodItem item = (FoodItem) foodItems.get(position);
        name.setText(item.food);

        switch (item.food) {
            case "Eggs":
                pic.setImageResource(R.mipmap.egg);
                break;
            case "Ground Beef":
                pic.setImageResource(R.mipmap.groundbeef);
                break;
            case "Chicken Breast":
                pic.setImageResource(R.mipmap.chickenbreast);
                break;
            case "Salami":
                pic.setImageResource(R.mipmap.salami);
                break;
            case "Tomato":
                pic.setImageResource(R.mipmap.tomato);
                break;
            case "Spinach":
                pic.setImageResource(R.mipmap.spinach);
                break;
            case "Onion":
                pic.setImageResource(R.mipmap.onion);
                break;
            case "Lettuce":
                pic.setImageResource(R.mipmap.lettuce);
                break;
            case "Milk":
                pic.setImageResource(R.mipmap.milk);
                break;
            case "Parmesan":
                pic.setImageResource(R.mipmap.parmesan);
                break;
            case "Goat Cheese":
                pic.setImageResource(R.mipmap.goatcheese);
                break;
            case "Sourdough":
                pic.setImageResource(R.mipmap.sourdough);
                break;
            case "Corn Chips":
                pic.setImageResource(R.mipmap.cornchips);
                break;
            default:
                break;
        }

        if (position % 2 == 1) {
            ll.setBackgroundColor(ContextCompat.getColor(ingredSearchView.getContext(),
                    R.color.primaryBackgroundDark));
        } else {
            ll.setBackgroundColor(ContextCompat.getColor(ingredSearchView.getContext(),
                    R.color.primaryBackground));
        }

        return ingredSearchView;
    }

}