package com.example.matt.yumly20;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

/**
 * Created by Isaac on 4/20/2017.
 */
public class IngredientSearchAdapter extends BaseAdapter {

    private final Context context;
    private List<String> urls;

    public IngredientSearchAdapter(Context c, int layoutId, List fItems) {
        context = c;
        urls = fItems;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (urls.get(position) == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(R.layout.empty_item, parent, false);
        }

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View ingredSearchView =
                inflater.inflate(R.layout.ingredient_search_item, parent, false);

        final LinearLayout ll = (LinearLayout) ingredSearchView.findViewById(R.id.isi_linear);
        final ImageView pic =
                (ImageView) ingredSearchView.findViewById(R.id.ingredient_search_image);

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.loadImage(urls.get(position), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                pic.setImageBitmap(loadedImage);
                pic.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        });

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll.setBackgroundColor(ContextCompat.getColor(ingredSearchView.getContext(),
                        R.color.primaryBackgroundDark));
                ((MainActivity) ingredSearchView.getContext()).newIngredientClick("Pepper",
                        urls.get(position));
            }
        });


        //TextView name = (TextView) ingredSearchView.findViewById(R.id.ingredient_search_text);

        //FoodItem item = (FoodItem) foodItems.get(position);
        //name.setText(item.food);

        /*switch (item.food) {
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
        }*/

        /*if (position % 2 == 1) {
            ll.setBackgroundColor(ContextCompat.getColor(ingredSearchView.getContext(),
                    R.color.primaryBackgroundDark));
        } else {
            ll.setBackgroundColor(ContextCompat.getColor(ingredSearchView.getContext(),
                    R.color.primaryBackground));
        }*/

        return ingredSearchView;
    }

}