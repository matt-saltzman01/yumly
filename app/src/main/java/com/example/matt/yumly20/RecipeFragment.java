package com.example.matt.yumly20;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView iView;
    private ListView dView;
    private LinearLayout nView;

    private String component = "Ingredients";
    private IngredientAdapter iAdapter;
    private DirectionAdapter dAdapter;

    private Recipe recipe;
    private List ingredients = new ArrayList();
    private List directions = new ArrayList();

    private OnFragmentInteractionListener mListener;

    public RecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeFragment newInstance(String param1, String param2) {
        RecipeFragment fragment = new RecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_recipe, container, false);

        final ImageView image = (ImageView) view.findViewById(R.id.recipe_image);

        final Button ingredientsButton = (Button) view.findViewById(R.id.ingredients_button);
        final Button directionsButton = (Button) view.findViewById(R.id.directions_button);
        final Button nutritionButton = (Button) view.findViewById(R.id.nutrition_button);

        final ImageButton starOff = (ImageButton) view.findViewById(R.id.star_button_off);
        final ImageButton starOn = (ImageButton) view.findViewById(R.id.star_button_on);

        ingredientsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                R.color.primary));
        directionsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                R.color.lightGrey));
        nutritionButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                R.color.lightGrey));

        final SQLiteDatabase recipesDB = (new RecipeOpenHelper(getActivity())).getWritableDatabase();
        try {
            recipe = new Recipe(recipesDB, "Everyday Baked Chicken");

            ingredients = recipe.ingredients;
            directions = recipe.directions;
            ImageLoader imageLoader = ImageLoader.getInstance();

            imageLoader.loadImage(recipe.photoURL, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    image.setImageBitmap(loadedImage);
                    image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
            });

        } catch (StringFormatException sfe) {
            sfe.printStackTrace();
        }

        ingredientsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ingredientsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.primary));
                directionsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));
                nutritionButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));

                iView.setVisibility(View.VISIBLE);
                dView.setVisibility(View.GONE);
                nView.setVisibility(View.GONE);

                component = "Ingredients";
            }
        });

        directionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                directionsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.primary));
                ingredientsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));
                nutritionButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));

                iView.setVisibility(View.GONE);
                dView.setVisibility(View.VISIBLE);
                nView.setVisibility(View.GONE);

                component = "Directions";
            }
        });

        nutritionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nutritionButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.primary));
                directionsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));
                ingredientsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));

                iView.setVisibility(View.GONE);
                dView.setVisibility(View.GONE);
                nView.setVisibility(View.VISIBLE);

                component = "Nutrition";
            }
        });

        if (recipe != null) {
            if (recipe.saved) {
                starOff.setVisibility(View.GONE);
                starOn.setVisibility(View.VISIBLE);
            }
        }

        starOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starOff.setVisibility(View.GONE);
                starOn.setVisibility(View.VISIBLE);
                recipe.saveToDB(recipesDB);
            }
        });

        starOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starOn.setVisibility(View.GONE);
                starOff.setVisibility(View.VISIBLE);
                recipe.deleteFromDB(recipesDB);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("Recipe Details");

        iView = (ListView) getActivity().findViewById(R.id.ingredients_component);
        dView = (ListView) getActivity().findViewById(R.id.directions_component);
        nView = (LinearLayout) getActivity().findViewById(R.id.nutrition_component);

        if (component == null) {
            component = "Ingredients";
        }

        iView.setVisibility(View.GONE);
        dView.setVisibility(View.GONE);
        nView.setVisibility(View.GONE);

        if (component.equals("Ingredients")) {
            iView.setVisibility(View.VISIBLE);
        } else if (component.equals("Directions")) {
            dView.setVisibility(View.VISIBLE);
        } else if (component.equals("Nutrition")) {
            nView.setVisibility(View.VISIBLE);
        }

        setAdapters();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setAdapters() {

        if (ingredients == null || ingredients.size() == 0) {
            populateIngredients();
        }
        iAdapter = new IngredientAdapter(getActivity(), R.layout.ingredients_item, ingredients);
        iView.setAdapter(iAdapter);


        if (directions == null || directions.size() == 0) {
            populateDirections();
        }
        dAdapter = new DirectionAdapter(getActivity(), R.layout.directions_item, directions);
        dView.setAdapter(dAdapter);

    }

    private void populateIngredients() {
        ingredients = new ArrayList();
        ingredients.add(new Ingredient("No ingredients", "for non existing recipes"));

    }

    private void populateDirections() {
        directions = new ArrayList();
        directions.add("This recipe no longer exists.");

    }


}
