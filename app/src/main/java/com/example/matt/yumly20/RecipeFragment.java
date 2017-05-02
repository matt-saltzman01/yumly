package com.example.matt.yumly20;

import android.content.Context;
import android.content.Intent;
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
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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
    private LinearLayout dView;
    private LinearLayout nView;

    private String component = "Ingredients";
    private IngredientAdapter iAdapter;
    private DirectionAdapter dAdapter;
    protected ImageLoader imageLoader;
    protected Bitmap picMap;

    private Recipe recipe;
    private List ingredients = new ArrayList();
    private HashMap<String, Float> nutrition = new HashMap();
    private String directionstext;
    private String directionsurl;

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

                    HashMap<String, Float> nuts = new HashMap<>();
                    JSONArray nutsJson = json.getJSONArray("nutritionEstimates");
                    for (int a = 0; a < nutsJson.length(); a++) {
                        JSONObject nutJ = nutsJson.getJSONObject(a);
                        String attr = nutJ.getString("attribute");

                        if (attr.equals("ENERC_KCAL") || attr.equals("CHOLE") ||
                                attr.equals("FAT") || attr.equals("NA") || attr.equals("PROCNT")) {
                            nuts.put(attr, (float) nutJ.getDouble("value"));
                        }
                    }

                    JSONObject source = json.getJSONObject("source");
                    String dirs =  source.getString("sourceDisplayName") + Recipe.DIVIDER +
                            source.getString("sourceRecipeUrl");

                    try {
                        recipe = new Recipe(
                                getActivity(),
                                recipesDB,
                                json.getString("name"),
                                json.getString("id"),
                                igs,
                                dirs,
                                nuts,
                                json.getJSONArray("images").getJSONObject(0)
                                        .getString("hostedLargeUrl")
                        );
                    } catch (StringFormatException sfe) {
                        sfe.printStackTrace();
                    }

                    ingredients = recipe.ingredients;
                    nutrition = recipe.nutrition;

                    try {
                        directionstext = recipe.parseDirections()[0];
                        directionsurl = recipe.parseDirections()[1];
                    } catch (StringFormatException sfe) {
                        sfe.printStackTrace();
                    }

                    imageLoader.loadImage(recipe.photoURL, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            picMap = loadedImage;
                            onResume();
                        }
                    });

                    setNutrition();

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
                recipe = new Recipe(getActivity(), recipesDB, id);

                ingredients = recipe.ingredients;
                directionstext = recipe.parseDirections()[0];
                directionsurl = recipe.parseDirections()[1];
                nutrition = recipe.nutrition;

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
        dView = (LinearLayout) getActivity().findViewById(R.id.directions_component);
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

        setNutrition();
        setAdapters();

        TextView dlText = (TextView) getActivity().findViewById(R.id.directions_link_text);
        dlText.setText(String.format("Steps to prepare the dish are supplied by %s at the " +
                "link below.", directionstext));

        dView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserDir = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(directionsurl));
                startActivity(browserDir);
            }
        });
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

    }

    private void setNutrition() {

        Context context = getActivity().getApplicationContext(); // app level storage
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        caloriesDaily = myPrefs.getFloat("calories", (float) 2000);
        cholesterolDaily = myPrefs.getFloat("cholesterol", (float) 300);
        fatDaily = myPrefs.getFloat("fat", (float) 65);
        proteinDaily = myPrefs.getFloat("protein", (float) 50);
        sodiumDaily = myPrefs.getFloat("sodium", (float) 1500);


        calProgressText = (TextView) getActivity().findViewById(R.id.calProgressText);
        cholProgressText = (TextView) getActivity().findViewById(R.id.cholProgressText);
        fatProgressText = (TextView) getActivity().findViewById(R.id.fatProgressText);
        proteinProgressText = (TextView) getActivity().findViewById(R.id.proteinProgressText);
        sodiumProgressText = (TextView) getActivity().findViewById(R.id.sodiumProgressText);

        calProgressWheel = (ProgressBar) getActivity().findViewById(R.id.calProgressWheel);
        cholProgressWheel = (ProgressBar) getActivity().findViewById(R.id.cholProgressWheel);
        fatProgressWheel = (ProgressBar) getActivity().findViewById(R.id.fatProgressWheel);
        proteinProgressWheel = (ProgressBar) getActivity().findViewById(R.id.proteinProgressWheel);
        sodiumProgressWheel = (ProgressBar) getActivity().findViewById(R.id.sodiumProgressWheel);


        if (nutrition.containsKey("ENERC_KCAL")) {
            float calPercent = (nutrition.get("ENERC_KCAL") / caloriesDaily) * (float) 100;
            calPercent = round(calPercent, 1);
            calProgressText.setText(Float.toString(calPercent) + "%");
            calProgressWheel.setProgress((int) calPercent);
        } else {
            calProgressText.setText("N/A");
            calProgressWheel.setProgress(0);
        }

        if (nutrition.containsKey("CHOLE")) {
            float cholPercent = ((nutrition.get("CHOLE") * (float) 1000) / cholesterolDaily)
                    * (float) 100;
            cholPercent = round(cholPercent, 1);
            cholProgressText.setText(Float.toString(cholPercent) + "%");
            cholProgressWheel.setProgress((int) cholPercent);
        } else {
            cholProgressText.setText("N/A");
            cholProgressWheel.setProgress(0);
        }

        if (nutrition.containsKey("FAT")) {
            float fatPercent = (nutrition.get("FAT") / fatDaily) * (float) 100;
            fatPercent = round(fatPercent, 1);
            fatProgressText.setText(Float.toString(fatPercent) + "%");
            fatProgressWheel.setProgress((int) fatPercent);
        } else {
            fatProgressText.setText("N/A");
            fatProgressWheel.setProgress(0);
        }

        if (nutrition.containsKey("NA")) {
            float sodiumPercent = ((nutrition.get("NA") * (float) 1000) / sodiumDaily)
                    * (float) 100;
            sodiumPercent = round(sodiumPercent, 1);
            sodiumProgressText.setText(Float.toString(sodiumPercent) + "%");
            sodiumProgressWheel.setProgress((int) sodiumPercent);
        } else {
            sodiumProgressText.setText("N/A");
            sodiumProgressWheel.setProgress(0);
        }

        if (nutrition.containsKey("PROCNT")) {
            float proteinPercent = (nutrition.get("PROCNT") / proteinDaily) * (float) 100;
            proteinPercent = round(proteinPercent, 1);
            proteinProgressText.setText(Float.toString(proteinPercent) + "%");
            proteinProgressWheel.setProgress((int) proteinPercent);
        } else {
            proteinProgressText.setText("N/A");
            proteinProgressWheel.setProgress(0);
        }

    }

    private void populateIngredients() {
        ingredients = new ArrayList();
        ingredients.add(new Ingredient("No ingredients", "for non existing recipes"));

    }

    private static float round (float value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
    }


}
