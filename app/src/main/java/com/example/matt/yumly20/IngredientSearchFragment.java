package com.example.matt.yumly20;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

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
public class IngredientSearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ListView lv;
    SearchView sv;
    IngredientSearchAdapter iSAdapter;
    List ingredients = new ArrayList();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public IngredientSearchFragment() {
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
        View view = inflater.inflate(R.layout.fragment_ingredient_search, container, false);
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

        if (ingredients == null || ingredients.size() == 0) {
            populateRecipes();
        }

        iSAdapter = new IngredientSearchAdapter(getActivity(), R.layout.ingredient_search_item,
                ingredients);
        lv = (ListView) getActivity().findViewById(R.id.ingredient_search_list);
        lv.setAdapter(iSAdapter);

        sv = (SearchView) getActivity().findViewById(R.id.is_search_view);
        sv.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        iSAdapter.getFilter().filter(newText);
                        return false;
                    }
                }
        );

        lv.setOnItemClickListener(new android.widget.ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ((MainActivity) getActivity()).newIngredientClick(view);
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

    private void populateRecipes() {

        //if (fridge == null) {
        ingredients = new ArrayList();
        //}

        ingredients.add(new FoodItem("Eggs", "M/P", "Egg.jpg"));
        ingredients.add(new FoodItem("Tomato", "F/V", "Tomato.jpg"));
        ingredients.add(new FoodItem("Spinach", "F/V", "Spinach.jpg"));
        ingredients.add(new FoodItem("Corn Chips", "Carbs", "CornChips.jpg"));
        ingredients.add(new FoodItem("Milk", "Dairy", "Milk.jpg"));
        ingredients.add(new FoodItem("Ground Beef", "M/P", "GroundBeef.jpg"));
        ingredients.add(new FoodItem("Parmesan", "Dairy", "Parmesan.jpg"));
        ingredients.add(new FoodItem("Goat Cheese", "Dairy", "GoatCheese.jpg"));
        ingredients.add(new FoodItem("Chicken Breast", "M/P", "ChickenBreast.jpg"));
        ingredients.add(new FoodItem("Onion", "F/V", "Onion.jpg"));
        ingredients.add(new FoodItem("Sourdough", "Carbs", "Sourdough.jpg"));
        ingredients.add(new FoodItem("Lettuce", "F/V", "Lettuce.jpg"));
        ingredients.add(new FoodItem("Salami", "M/P", "Salami.jpg"));

    }

}
