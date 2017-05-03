package com.example.matt.yumly20;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.health.SystemHealthManager;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

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
 * {@link SearchRecipesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchRecipesFragment extends Fragment {

    private static final String APP_ID = "c99817f9";
    private static final String APP_KEY = "e7f3fbaa149d8beef86f2affedf35244";
    private static final String API_PREFIX = String.format("http://api.yummly.com/v1" +
            "/api/recipes?_app_id=%s&_app_key=%s", APP_ID, APP_KEY);

    private static final String ARG_PARAM1 = "ingredients";
    private static final String ARG_PARAM2 = "query";

    private String ingredients;
    private String query;
    private String alleriesDiets;
    private String cuisines;

    protected ListView lv;
    protected SearchView sv;
    protected SearchRecipesAdapter srAdapter;
    protected ArrayList<RecipePreview> recipes = new ArrayList<>();
    protected int current = 0;
    protected int possible = -1;

    private OnFragmentInteractionListener mListener;

    public SearchRecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param ingredients Parameter 1.
     * @return A new instance of fragment SearchRecipesFragment.
     */
    public static SearchRecipesFragment newInstance(String ingredients, String query) {
        SearchRecipesFragment fragment = new SearchRecipesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, ingredients);
        args.putString(ARG_PARAM2, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            ingredients = getArguments().getString(ARG_PARAM1);
            query = getArguments().getString(ARG_PARAM2);
            cuisines = "";
        } else {
            ingredients = "";
            query = "";
            cuisines = "";
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_recipes, container, false);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("Search Recipes");

        searchForRecipes();

        sv = (SearchView) getActivity().findViewById(R.id.mr_search_view);
        sv.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String q) {

                        query = String.format("&q=%s", q.toLowerCase().replace(' ', '+'));
                        searchForRecipes();
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                }
        );

        ListView lv = (ListView) getActivity().findViewById(R.id.recipes_list);

        lv.setOnItemClickListener(new android.widget.ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ((MainActivity) getActivity()).searchRecipeClick(recipes.get(position).id);
            }
        });

        ImageView yummlyLogo = (ImageView) getActivity().findViewById(R.id.yummly_image);
        yummlyLogo.setImageBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.yummly_logo));
        yummlyLogo.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        LinearLayout yummlyLinear = (LinearLayout) getActivity().findViewById(R.id.yummly_linear);
        yummlyLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserYummly = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.yummly.com/"));
                startActivity(browserYummly);
            }
        });

        final CheckBox cuisineBox = (CheckBox) getActivity().findViewById(R.id.cuisine_checkbox);
        cuisineBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchForRecipes();
            }
        });

    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

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

    private void searchForRecipes() {

        buildAllergiesAndDietsString();
        buildCuisinesString();
        //String[] api = {"ingredient?", "allergy?", "diet?", "cuisine?"};

        try {
            if (possible == -1 || current < possible) {
                //for (int a = 0; a < api.length; a++) {
                String urlString = String.format("%s%s%s%s%s%s%s", API_PREFIX, "&maxResult=10&start=0",
                        "&requirePictures=true", query, alleriesDiets, ingredients, cuisines);
                    /*String urlString = String.format("%s_app_id=%s&_app_key=%s",
                            "http://api.yummly.com/v1/api/metadata/" + api[a], APP_ID, APP_KEY);*/
                System.out.println(urlString);
                URL url = new URL(urlString);
                new QueryYummlyTask().execute(url);
                //}
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        }

    }

    private void buildAllergiesAndDietsString() {

        Context context = getActivity().getApplicationContext();
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        alleriesDiets = "";

        //get diets booleans
        if (myPrefs.getBoolean("lacto_veg", false)) {
            alleriesDiets += "&allowedDiet%5B%5D=" + "388^Lacto+vegetarian";
        }
        if (myPrefs.getBoolean("ovo_veg", false)) {
            alleriesDiets += "&allowedDiet%5B%5D=" + "389^Ovo+vegetarian";
        }
        if (myPrefs.getBoolean("pesc", false)) {
            alleriesDiets += "&allowedDiet%5B%5D=" + "390^Pescetarian";
        }
        if (myPrefs.getBoolean("vegan", false)) {
            alleriesDiets += "&allowedDiet%5B%5D=" + "386^Vegan";
        }
        if (myPrefs.getBoolean("vegetarian", false)) {
            alleriesDiets += "&allowedDiet%5B%5D=" + "387^Lacto-ovo+vegetarian";
        }
        if (myPrefs.getBoolean("paleo", false)) {
            alleriesDiets += "&allowedDiet%5B%5D=" + "403^Paleo";
        }

        //get and set allergies checkbox according to sharedprefs
        if (myPrefs.getBoolean("dairy", false)) {
            alleriesDiets += "&allowedAllergy%5B%5D=" + "396^Dairy-Free";
        }
        if (myPrefs.getBoolean("egg", false)) {
            alleriesDiets += "&allowedAllergy%5B%5D=" + "397^Egg-Free";
        }
        if (myPrefs.getBoolean("gluten", false)) {
            alleriesDiets += "&allowedAllergy%5B%5D=" + "393^Gluten-Free";
        }
        if (myPrefs.getBoolean("peanut", false)) {
            alleriesDiets += "&allowedAllergy%5B%5D=" + "394^Peanut-Free";
        }
        if (myPrefs.getBoolean("seafood", false)) {
            alleriesDiets += "&allowedAllergy%5B%5D=" + "398^Seafood-Free";
        }
        if (myPrefs.getBoolean("sesame", false)) {
            alleriesDiets += "&allowedAllergy%5B%5D=" + "399^Sesame-Free";
        }
        if (myPrefs.getBoolean("soy", false)) {
            alleriesDiets += "&allowedAllergy%5B%5D=" + "400^Soy-Free";
        }
        if (myPrefs.getBoolean("sulfite", false)) {
            alleriesDiets += "&allowedAllergy%5B%5D=" + "401^Sulfite-Free";
        }
        if (myPrefs.getBoolean("treenut", false)) {
            alleriesDiets += "&allowedAllergy%5B%5D=" + "395^Tree+Nut-Free";
        }
        if (myPrefs.getBoolean("wheat", false)) {
            alleriesDiets += "&allowedAllergy%5B%5D=" + "392^Wheat-Free";
        }

        System.out.println("~~~~~~~*" + alleriesDiets + "*~~~~~~");

    }

    private void buildCuisinesString() {

        cuisines = "";
        Context context = getActivity().getApplicationContext();
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        if (((CheckBox) getActivity().findViewById(R.id.cuisine_checkbox)).isChecked()) {

            if (myPrefs.getBoolean("american", false)) {
                cuisines += "&allowedCuisine%5B%5D=" + "cuisine^cuisine-american";
            }
            if (myPrefs.getBoolean("chinese", false)) {
                cuisines += "&allowedCuisine%5B%5D=" + "cuisine^cuisine-chinese";
            }
            if (myPrefs.getBoolean("english", false)) {
                cuisines += "&allowedCuisine%5B%5D=" + "cuisine^cuisine-english";
            }
            if (myPrefs.getBoolean("french", false)) {
                cuisines += "&allowedCuisine%5B%5D=" + "cuisine^cuisine-french";
            }
            if (myPrefs.getBoolean("greek", false)) {
                cuisines += "&allowedCuisine%5B%5D=" + "cuisine^cuisine-greek";
            }
            if (myPrefs.getBoolean("hawaiin", false)) {
                cuisines += "&allowedCuisine%5B%5D=" + "cuisine^cuisine-hawaiian";
            }
            if (myPrefs.getBoolean("indian", false)) {
                cuisines += "&allowedCuisine%5B%5D=" + "cuisine^cuisine-indian";
            }
            if (myPrefs.getBoolean("italian", false)) {
                cuisines += "&allowedCuisine%5B%5D=" + "cuisine^cuisine-italian";
            }
            if (myPrefs.getBoolean("japanese", false)) {
                cuisines += "&allowedCuisine%5B%5D=" + "cuisine^cuisine-japanese";
            }
            if (myPrefs.getBoolean("mediterrenean", false)) {
                cuisines += "&allowedCuisine%5B%5D=" + "cuisine^cuisine-mediterranean";
            }
            if (myPrefs.getBoolean("mexican", false)) {
                cuisines += "&allowedCuisine%5B%5D=" + "cuisine^cuisine-mexican";
            }
            if (myPrefs.getBoolean("thai", false)) {
                cuisines += "&allowedCuisine%5B%5D=" + "cuisine^cuisine-thai";
            }
        }

        System.out.println("~~~~~~~*" + cuisines + "*~~~~~~");

    }

    /**
     * Created by Isaac on 4/25/2017.
     */
    public class QueryYummlyTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            getActivity().findViewById(R.id.progress_load).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.recipes_list).setVisibility(View.GONE);
            getActivity().findViewById(R.id.yummly_layout).setVisibility(View.GONE);
            getActivity().findViewById(R.id.cuisine_checkbox).setVisibility(View.GONE);

            recipes = new ArrayList<>();
        }

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
                    JSONArray recipesJson = json.getJSONArray("matches");

                    for (int a = 0; a < recipesJson.length(); a++) {

                        JSONObject rpJson = recipesJson.getJSONObject(a);

                        recipes.add(new RecipePreview(
                                        rpJson.getString("recipeName"),
                                        rpJson.getString("id"),
                                        rpJson.getJSONArray("smallImageUrls").getString(0)
                                )
                        );
                    }


                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }

            getActivity().findViewById(R.id.progress_load).setVisibility(View.GONE);

            if (recipes == null || recipes.size() == 0) {

                getActivity().findViewById(R.id.yummly_layout).setVisibility(View.GONE);
                getActivity().findViewById(R.id.recipes_list).setVisibility(View.GONE);
                getActivity().findViewById(R.id.cuisine_checkbox).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.no_search_text).setVisibility(View.VISIBLE);

            } else {

                getActivity().findViewById(R.id.yummly_layout).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.recipes_list).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.cuisine_checkbox).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.no_search_text).setVisibility(View.GONE);

                srAdapter = new SearchRecipesAdapter(getActivity(), R.layout.search_recipes_item,
                        recipes);
                ((ListView) getActivity().findViewById(R.id.recipes_list)).setAdapter(srAdapter);
            }
        }

    }

}
