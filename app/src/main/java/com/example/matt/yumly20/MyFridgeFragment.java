package com.example.matt.yumly20;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyFridgeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyFridgeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFridgeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FridgeAdapter fAdaptorL;
    private FridgeAdapter fAdaptorC;
    private FridgeAdapter fAdaptorR;

    private FridgeAdapter fAdapterGrid;
    private FridgeAdapter meatAdapter;
    private FridgeAdapter vegAdapter;
    private FridgeAdapter dryAdapter;
    private FridgeAdapter carbAdapter;


    public ArrayList<FoodItem> fridge = new ArrayList<>();
    public String group;

    private OnFragmentInteractionListener mListener;

    public MyFridgeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFridgeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFridgeFragment newInstance(String param1, String param2) {
        MyFridgeFragment fragment = new MyFridgeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateFridge();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_fridge, container, false);

        final Button meatButton = (Button) view.findViewById(R.id.meat_button);
        final Button vegButton = (Button) view.findViewById(R.id.veg_button);
        final Button dairyButton = (Button) view.findViewById(R.id.dairy_button);
        final Button carbsButton = (Button) view.findViewById(R.id.carbs_button);

        meatButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                R.color.lightGrey));
        vegButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                R.color.lightGrey));
        dairyButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                R.color.lightGrey));
        carbsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                R.color.lightGrey));

        if (group == null) {
            group = "M/P";
        }

        switch (group) {
            case "M/P":
                meatButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.primary));
                break;
            case "F/V":
                vegButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.primary));
                break;
            case "Dairy":
                dairyButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.primary));
                break;
            case "Carbs":
                carbsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.primary));
                break;
            default:
                break;
        }

        meatButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                meatButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.primary));
                vegButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));
                dairyButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));
                carbsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));

                group = "M/P";
                setAdapters();
            }
        });
        vegButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vegButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.primary));
                meatButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));
                dairyButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));
                carbsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));

                group = "F/V";
                setAdapters();
            }
        });
        dairyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dairyButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.primary));
                vegButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));
                meatButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));
                carbsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));

                group = "Dairy";
                setAdapters();
            }
        });
        carbsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                carbsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.primary));
                vegButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));
                dairyButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));
                meatButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                        R.color.lightGrey));

                group = "Carbs";
                setAdapters();
            }
        });

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
        ((MainActivity) getActivity()).setTitle("My Fridge");

        final Button findRecipesButton =
                (Button) getActivity().findViewById(R.id.find_recipes_button);

        findRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String checked = "";
                for (int a = 0; a < fridge.size(); a++) {
                    if (fridge.get(a).checked) {
                        String name = fridge.get(a).food.replace(' ', '+');
                        checked += "&allowedIngredient%5B%5D=" + name.toLowerCase();
                    }
                }
                ((MainActivity) getActivity()).findRecipesButtonClick(checked);
            }
        });

        if (fridge == null || fridge.size() == 0) {
            populateFridge();
        }

        setAdapters();

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

    public void setAdapters() {

        /*if (meatAdapter == null || vegAdapter == null || dryAdapter == null || carbAdapter == null
                || (meatAdapter.getCount() + vegAdapter.getCount() + dryAdapter.getCount()
                + carbAdapter.getCount()) != fridge.size()) {
            meatAdapter = new FridgeAdapter(getActivity(), R.layout.fridge_item, getItems("M/P"));
            vegAdapter = new FridgeAdapter(getActivity(), R.layout.fridge_item, getItems("F/V"));
            dryAdapter = new FridgeAdapter(getActivity(), R.layout.fridge_item, getItems("Dairy"));
            carbAdapter = new FridgeAdapter(getActivity(), R.layout.fridge_item, getItems("Carbs"));
        }*/

        GridView gridView = (GridView) getActivity().findViewById(R.id.item_grid);

        switch (group) {
            case "M/P":
                if (meatAdapter == null || meatAdapter.getCount() != getItems("M/P").size()) {
                    meatAdapter = new FridgeAdapter(getActivity(), R.layout.fridge_item,
                            getItems("M/P"));
                }
                gridView.setAdapter(meatAdapter);
                break;
            case "F/V":
                if (vegAdapter == null || vegAdapter.getCount() != getItems("F/V").size()) {
                    vegAdapter = new FridgeAdapter(getActivity(), R.layout.fridge_item,
                            getItems("F/V"));
                }
                gridView.setAdapter(vegAdapter);
                break;
            case "Dairy":
                if (dryAdapter == null || dryAdapter.getCount() != getItems("Dairy").size()) {
                    dryAdapter = new FridgeAdapter(getActivity(), R.layout.fridge_item,
                            getItems("Dairy"));
                }
                gridView.setAdapter(dryAdapter);
                break;
            case "Carbs":
                if (carbAdapter == null || carbAdapter.getCount() != getItems("Carbs").size()) {
                    carbAdapter = new FridgeAdapter(getActivity(), R.layout.fridge_item,
                            getItems("Carbs"));
                }
                gridView.setAdapter(carbAdapter);
                break;
            default:
                break;
        }

        /*ArrayList<FoodItem> items = getItems();

        fAdapterGrid = new FridgeAdapter(getActivity(), R.layout.fridge_item, items);
        GridView gridView = (GridView) getActivity().findViewById(R.id.item_grid);
        gridView.setAdapter(fAdapterGrid);*/

    }

    private ArrayList<FoodItem> getItems(String grp) {
        ArrayList<FoodItem> items = new ArrayList<>();
        for (int a = 0; a < fridge.size(); a++) {
            if (fridge.get(a) != null && fridge.get(a).group.equals(grp)) {
                items.add(fridge.get(a));
            }
        }

        Collections.sort(items, new Comparator<FoodItem>() {
            @Override
            public int compare(FoodItem i1, FoodItem i2) {
                return i1.food.compareTo(i2.food);
            }
        });
        return items;
    }

    private ArrayList<FoodItem> getItems() {
        ArrayList<FoodItem> items = new ArrayList<>();
        for (int a = 0; a < fridge.size(); a++) {
            if (fridge.get(a) != null && fridge.get(a).group.equals(group)) {
                items.add(fridge.get(a));
            }
        }

        Collections.sort(items, new Comparator<FoodItem>() {
            @Override
            public int compare(FoodItem i1, FoodItem i2) {
                return i1.food.compareTo(i2.food);
            }
        });
        return items;
    }

    private ArrayList<FoodItem> splitList(ArrayList<FoodItem> items, int mod) {
        ArrayList<FoodItem> split = new ArrayList<>();
        for (int a = mod; a < items.size(); a += 3) {
            split.add(items.get(a));
        }
        return split;
    }

    private void populateFridge() {

        fridge = new ArrayList<>();

        fridge.add(new FoodItem(getActivity(), "Eggs", "M/P",
                "https://tvaraj.files.wordpress.com/2012/10/eggs.jpg"));
        fridge.add(new FoodItem(getActivity(), "Tomato", "F/V",
                "http://www.fitnessvsweightloss.com/wp-content/uploads/2014/07/tomato.jpg"));
        fridge.add(new FoodItem(getActivity(), "Spinach", "F/V",
                "http://iwantmysexyback.files.wordpress.com/2011/01/iron-source-spinach-lg.jpg"));
        fridge.add(new FoodItem(getActivity(), "Bread Crumbs", "Carbs",
                "http://www.classicexhibits.com/tradeshow-blog/wp-content/uploads/2012/10/" +
                        "Bread-Crumbs.jpg"));
        fridge.add(new FoodItem(getActivity(), "Milk", "Dairy",
                "http://2.bp.blogspot.com/-M-HC4ThL9Hk/TgSuXPlvU4I/AAAAAAAAChE/YhLucY1d_no/s1600/" +
                        "milk-agriculture-commodities.jpg"));
        fridge.add(new FoodItem(getActivity(), "Ground Beef", "M/P",
                "http://3.bp.blogspot.com/-M-vLu0438Ic/UPHGE418XYI/AAAAAAAAAGM/l2T5oDNTDaU/s1600/" +
                        "Ground+Beef+Recipe.jpg"));
        fridge.add(new FoodItem(getActivity(), "Parmesan", "Dairy",
                "https://getbwoo.com/wp-content/uploads/2013/10/shutterstock_574773461.jpg"));
        fridge.add(new FoodItem(getActivity(), "Goat Cheese", "Dairy",
                "http://www.cheesemaking.com/images/recipes/33Chevre/Pics/pic02.jpg"));
        fridge.add(new FoodItem(getActivity(), "Chicken Breast", "M/P",
                "http://img2.timeinc.net/health/img/web/2014/04/slides/" +
                        "chicken-breast-raw-400x400.jpg"));
        fridge.add(new FoodItem(getActivity(), "Onion", "F/V",
                "http://ghk.h-cdn.co/assets/cm/15/11/54fe44fd11c59-ghk-stainbuster-onion-mdn.jpg"));
        fridge.add(new FoodItem(getActivity(), "Sourdough", "Carbs",
                "http://www.wildyeastblog.com/wp-content/uploads/2008/11/" +
                        "more-sourdough.jpg?441324"));
        fridge.add(new FoodItem(getActivity(), "Lettuce", "F/V",
                "http://2.bp.blogspot.com/-AOP_sPL_UoY/Td_WX4XVSkI/AAAAAAAABcU/w373jilbAy0/" +
                        "s1600/iStock_000007888237XSmall.jpg"));
        fridge.add(new FoodItem(getActivity(), "Salami", "M/P",
                "http://cdn1.theodysseyonline.com/files/2014/10/14/" +
                        "63548854034210072183215798_collagen-salami3.png"));

    }
}
