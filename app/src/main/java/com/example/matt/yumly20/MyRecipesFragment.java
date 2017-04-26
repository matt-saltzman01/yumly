package com.example.matt.yumly20;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

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
 * {@link MyRecipesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyRecipesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    protected ListView lv;
    protected SearchView sv;
    protected MyRecipesAdapter mrAdapter;
    protected ArrayList<Recipe> recipes = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyRecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyRecipesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyRecipesFragment newInstance(String param1, String param2) {
        MyRecipesFragment fragment = new MyRecipesFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_recipes, container, false);
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
        ((MainActivity) getActivity()).setTitle("Favorite Recipes");

        getMyRecipes();

        if (recipes == null || recipes.size() == 0) {
            getActivity().findViewById(R.id.yummly_layout).setVisibility(View.GONE);
            getActivity().findViewById(R.id.recipes_list).setVisibility(View.GONE);
            getActivity().findViewById(R.id.no_recpies_text).setVisibility(View.VISIBLE);
            return;
        } else {
            getActivity().findViewById(R.id.yummly_layout).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.recipes_list).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.no_recpies_text).setVisibility(View.GONE);
        }

        mrAdapter = new MyRecipesAdapter(getActivity(), R.layout.my_recipes_item, recipes);
        lv = (ListView) getActivity().findViewById(R.id.recipes_list);
        lv.setAdapter(mrAdapter);

        sv = (SearchView) getActivity().findViewById(R.id.mr_search_view);
        sv.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        mrAdapter.getFilter().filter(newText);
                        return false;
                    }
                }
        );

        lv.setOnItemClickListener(new android.widget.ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ((MainActivity) getActivity()).myRecipeClick(recipes.get(position).id);
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

    private void getMyRecipes() {

        recipes = new ArrayList<>();

        SQLiteDatabase recipesDB = (new RecipeOpenHelper(getActivity())).getWritableDatabase();

        String sql = String.format("SELECT * FROM Recipes");
        Cursor cursor = (new RecipeOpenHelper(getActivity())).getWritableDatabase()
                .rawQuery(sql, new String[] {});
        if (cursor.getCount() == 0) {

        } else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast() && !cursor.isClosed()) {

                try {
                    recipes.add(new Recipe(recipesDB, cursor.getString(1)));
                } catch (StringFormatException sfe) {
                    sfe.printStackTrace();
                }
                cursor.moveToNext();
            }
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }


    }


    private void populateRecipes() {
        /*recipes = new ArrayList();
        recipes.add("Fried Rice");
        recipes.add("Everyday Baked Chicken");
        recipes.add("Burger");
        recipes.add("Crab Cake");
        recipes.add("Guacamole");
        recipes.add("Pasta");
        recipes.add("Ramen");
        recipes.add("Salmon");
        recipes.add("Tacos");
        recipes.add("Brownies");*/
    }

}
