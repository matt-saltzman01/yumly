package com.example.matt.yumly20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Isaac on 4/16/2017.
 */
public class FridgeAdapter extends BaseAdapter {

    private final Context context;
    private List foodItems;

    public FridgeAdapter(Context c, int layoutId, List fItems) {
        //super(c, layoutId, fItems);
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