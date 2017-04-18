package com.example.matt.yumly20;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Isaac on 4/17/2017.
 */
public class IngredientAdapter extends ArrayAdapter<String[]> {

    private final Context context;
    private List ingredients;

    public IngredientAdapter(Context c, int layoutId, List ingreds) {
        super(c, layoutId, ingreds);
        context = c;
        ingredients = ingreds;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View ingredientsView = convertView;

        if (ingredientsView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ingredientsView = inflater.inflate(R.layout.ingredients_item, parent, false);
        }

        LinearLayout layout = (LinearLayout) ingredientsView.findViewById(R.id.ingredient_layout);
        TextView name = (TextView) ingredientsView.findViewById(R.id.ingredient_name);

        Ingredient item = (Ingredient) ingredients.get(position);
        name.setText(String.format("%s %s", item.amount, item.name));

        if (position % 2 == 1) {
            layout.setBackgroundColor(ContextCompat.getColor(ingredientsView.getContext(),
                    R.color.mediumGrey));
        } else {
            layout.setBackgroundColor(ContextCompat.getColor(ingredientsView.getContext(),
                    R.color.lightGrey));
        }

        return ingredientsView;
    }
}