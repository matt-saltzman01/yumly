package com.example.matt.yumly20;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Isaac on 4/17/2017.
 */
public class DirectionAdapter extends ArrayAdapter<String[]> {

    private final Context context;
    private List directions;

    public DirectionAdapter(Context c, int layoutId, List directs) {
        super(c, layoutId, directs);
        context = c;
        directions = directs;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View directionsView = convertView;

        if (directionsView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            directionsView = inflater.inflate(R.layout.directions_item, parent, false);
        }

        LinearLayout layout = (LinearLayout) directionsView.findViewById(R.id.direction_layout);
        TextView name = (TextView) directionsView.findViewById(R.id.direction_text);

        String text = (String) directions.get(position);
        name.setText(text);

        if (position % 2 == 1) {
            layout.setBackgroundColor(ContextCompat.getColor(directionsView.getContext(),
                    R.color.primaryBackgroundDark));
        } else {
            layout.setBackgroundColor(ContextCompat.getColor(directionsView.getContext(),
                    R.color.primaryBackground));
        }

        return directionsView;
    }
}