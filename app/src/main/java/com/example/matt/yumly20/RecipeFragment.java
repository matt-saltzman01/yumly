package com.example.matt.yumly20;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    private static final String APP_ID = "c99817f9";
    private static final String APP_KEY = "e7f3fbaa149d8beef86f2affedf35244";
    private static final String API_PREFIX = "http://api.yummly.com/v1/api/recipe/";
    private static final String API_SUFFIX = String.format("?_app_id=%s&_app_key=%s",
            APP_ID, APP_KEY);

    private static final String ARG_PARAM1 = "source";
    private static final String ARG_PARAM2 = "id";

    private String source;
    private String id;

    protected SQLiteDatabase recipesDB;

    private ListView iView;
    private ListView dView;
    private LinearLayout nView;

    private String component = "Ingredients";
    private IngredientAdapter iAdapter;
    private DirectionAdapter dAdapter;
    protected ImageLoader imageLoader;
    protected Bitmap picMap;

    private Recipe recipe;
    private List ingredients = new ArrayList();
    private List directions = new ArrayList();

    private SharedPreferences myPrefs;
    private float caloriesDaily;
    private float cholesterolDaily;
    private float fatDaily;
    private float proteinDaily;
    private float sodiumDaily;
    private TextView calProgressText;
    private TextView cholProgressText;
    private TextView fatProgressText;
    private TextView proteinProgressText;
    private TextView sodiumProgressText;
    private ProgressBar calProgressWheel;
    private ProgressBar cholProgressWheel;
    private ProgressBar fatProgressWheel;
    private ProgressBar proteinProgressWheel;
    private ProgressBar sodiumProgressWheel;


    private OnFragmentInteractionListener mListener;

    public RecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Created by Isaac on 4/25/2017.
     */
    public class QueryYummlyTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            try {
                URL url = params[0];
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new
                            InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    System.out.println(stringBuilder.toString());
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {

            if (!result.equals("")) {
                try {

                    JSONObject json = new JSONObject(result);

                    ArrayList<Ingredient> igs = new ArrayList<>();
                    JSONArray igJson = json.getJSONArray("ingredientLines");
                    for (int a = 0; a < igJson.length(); a++) {
                        igs.add(new Ingredient(igJson.getString(a), " "));
                    }

                    ArrayList<String> dirs = new ArrayList<>();
                    dirs.add("There are no listed directions.");

                    try {
                        recipe = new Recipe(
                                recipesDB,
                                json.getString("name"),
                                json.getString("id"),
                                igs,
                                dirs,
                                json.getJSONArray("images").getJSONObject(0)
                                        .getString("hostedLargeUrl")
                        );
                    } catch (StringFormatException sfe) {
                        sfe.printStackTrace();
                    }

                    ingredients = recipe.ingredients;
                    directions = recipe.directions;

                    imageLoader.loadImage(recipe.photoURL, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            picMap = loadedImage;
                            onResume();
                        }
                    });

                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }
        }

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param source Parameter 1.
     * @param idname Parameter 2.
     * @return A new instance of fragment RecipeFragment.
     */
    public static RecipeFragment newInstance(String source, String idname) {
        RecipeFragment fragment = new RecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, source);
        if (source.equals("search") || source.equals("saved")) {
            args.putString(ARG_PARAM2, idname);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            source = getArguments().getString(ARG_PARAM1);
            if (source.equals("search") || source.equals("saved")) {
                id = getArguments().getString(ARG_PARAM2);
            } else {
                id = "";
            }
        } else {
            source = "";
            id = "";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_recipe, container, false);

        recipesDB = (new RecipeOpenHelper(getActivity())).getWritableDatabase();

        imageLoader = ImageLoader.getInstance();

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

        if (source.equals("saved")) {
            try {
                recipe = new Recipe(recipesDB, id);

                ingredients = recipe.ingredients;
                directions = recipe.directions;

                imageLoader.loadImage(recipe.photoURL, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        picMap = loadedImage;
                        onResume();
                    }
                });

            } catch (StringFormatException sfe) {
                sfe.printStackTrace();
            }
        } else if (source.equals("search")) {
            try {
                new QueryYummlyTask().execute(new URL(API_PREFIX + id + API_SUFFIX));
            } catch (MalformedURLException mue) {
                mue.printStackTrace();
            }
        }

        Context context = getActivity().getApplicationContext(); // app level storage
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        caloriesDaily = myPrefs.getFloat("calories", (float) 2000);
        cholesterolDaily = myPrefs.getFloat("cholesterol", (float) 3000);
        fatDaily = myPrefs.getFloat("fat", (float) 65);
        proteinDaily = myPrefs.getFloat("protein", (float) 50);
        sodiumDaily = myPrefs.getFloat("sodium", (float) 2400);


        calProgressText = (TextView) view.findViewById(R.id.calProgressText);
        cholProgressText = (TextView) view.findViewById(R.id.cholProgressText);
        fatProgressText = (TextView) view.findViewById(R.id.fatProgressText);
        proteinProgressText = (TextView) view.findViewById(R.id.proteinProgressText);
        sodiumProgressText = (TextView) view.findViewById(R.id.sodiumProgressText);
        calProgressWheel = (ProgressBar) view.findViewById(R.id.calProgressWheel);
        cholProgressWheel = (ProgressBar) view.findViewById(R.id.cholProgressWheel);
        fatProgressWheel = (ProgressBar) view.findViewById(R.id.fatProgressWheel);
        proteinProgressWheel = (ProgressBar) view.findViewById(R.id.proteinProgressWheel);
        sodiumProgressWheel = (ProgressBar) view.findViewById(R.id.sodiumProgressWheel);


        float calPercent = (140/caloriesDaily)*100;
        float cholPercent = (400/cholesterolDaily)*100;
        float fatPercent = (20/fatDaily)*100;
        float sodiumPercent = (654/sodiumDaily)*100;
        float proteinPercent = (27/proteinDaily)*100;

        calPercent = round(calPercent, 1);
        cholPercent = round(cholPercent, 1);
        fatPercent = round(fatPercent, 1);
        sodiumPercent = round(sodiumPercent, 1);
        proteinPercent = round(proteinPercent, 1);

        calProgressText.setText(Float.toString(calPercent) + "%");
        cholProgressText.setText(Float.toString(cholPercent) + "%");
        fatProgressText.setText(Float.toString(fatPercent) + "%");
        sodiumProgressText.setText(Float.toString(sodiumPercent) + "%");
        proteinProgressText.setText(Float.toString(proteinPercent) + "%");

        calProgressWheel.setProgress((int) (calPercent));
        cholProgressWheel.setProgress((int) (cholPercent));
        fatProgressWheel.setProgress((int) (fatPercent));
        sodiumProgressWheel.setProgress((int) (sodiumPercent));
        proteinProgressWheel.setProgress((int) (proteinPercent));




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

        recipesDB = (new RecipeOpenHelper(getActivity())).getWritableDatabase();

        final ImageView image = (ImageView) getActivity().findViewById(R.id.recipe_image);
        final TextView rTitle = (TextView) getActivity().findViewById(R.id.recipe_title);

        if (picMap != null){
            image.setImageBitmap(picMap);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        if (recipe != null) {
            rTitle.setText(recipe.name);
            if (recipe.saved) {
                getActivity().findViewById(R.id.star_button_off).setVisibility(View.GONE);
                getActivity().findViewById(R.id.star_button_on).setVisibility(View.VISIBLE);
            } else {
                getActivity().findViewById(R.id.star_button_off).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.star_button_on).setVisibility(View.GONE);
            }
        }

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

    @Override
    public void onPause() {
        recipesDB.close();
        super.onPause();
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

    private static float round (float value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
    }


}
