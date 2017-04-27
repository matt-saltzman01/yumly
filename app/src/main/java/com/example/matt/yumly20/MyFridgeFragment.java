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

    public ArrayList<FoodItem> fridge = new ArrayList<>();
    private String group;

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
                R.color.primary));
        vegButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                R.color.lightGrey));
        dairyButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                R.color.lightGrey));
        carbsButton.setBackgroundColor(ContextCompat.getColor(getActivity(),
                R.color.lightGrey));

        group = "M/P";

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
                        StringBuilder name = new StringBuilder(fridge.get(a).food);
                        for (int b = 0; b < name.length(); b++) {
                            if (name.charAt(b) == ' ') {
                                name.setCharAt(b, '+');
                            }
                        }
                        checked += "&allowedIngredient%5B%5D=" + name.toString();
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

        ArrayList<FoodItem> items = getItems();

        /*Eventually this should be a GridView but I didn't know
        that existed when I wrote this code. This will work fine for the
        UI demo. -Isaac*/

 /*       if (items.size() == 0) {
            fAdaptorL = new FridgeAdapter(getActivity(), R.layout.fridge_item, items);
            fAdaptorC = new FridgeAdapter(getActivity(), R.layout.fridge_item, items);
            fAdaptorR = new FridgeAdapter(getActivity(), R.layout.fridge_item, items);
        } else if (items.size() == 1) {
            fAdaptorL = new FridgeAdapter(getActivity(), R.layout.fridge_item, items);
            fAdaptorC = new FridgeAdapter(getActivity(), R.layout.fridge_item, new ArrayList());
            fAdaptorR = new FridgeAdapter(getActivity(), R.layout.fridge_item, new ArrayList());
        } else if (items.size() == 2) {
            fAdaptorL = new FridgeAdapter(getActivity(), R.layout.fridge_item, items.subList(0, 1));
            fAdaptorC = new FridgeAdapter(getActivity(), R.layout.fridge_item, items.subList(1, 2));
            fAdaptorR = new FridgeAdapter(getActivity(), R.layout.fridge_item, new ArrayList());
        } else {
            fAdaptorL = new FridgeAdapter(getActivity(), R.layout.fridge_item,
                    splitList(items, 0));
            fAdaptorC = new FridgeAdapter(getActivity(), R.layout.fridge_item,
                    splitList(items, 1));
            fAdaptorR = new FridgeAdapter(getActivity(), R.layout.fridge_item,
                    splitList(items, 2));
        }


        ListView leftL = (ListView) getActivity().findViewById(R.id.left_list);
        ListView centerL = (ListView) getActivity().findViewById(R.id.center_list);
        ListView rightL = (ListView) getActivity().findViewById(R.id.right_list);

        leftL.setAdapter(fAdaptorL);
        centerL.setAdapter(fAdaptorC);
        rightL.setAdapter(fAdaptorR);*/

        fAdapterGrid = new FridgeAdapter(getActivity(), R.layout.fridge_item, items);
        GridView gridView = (GridView) getActivity().findViewById(R.id.item_grid);
        gridView.setAdapter(fAdapterGrid);

    }

    private ArrayList<FoodItem> getItems() {
        ArrayList<FoodItem> items = new ArrayList<>();
        for (int a = 0; a < fridge.size(); a++) {
            if (fridge.get(a) != null && fridge.get(a).group.equals(group)) {
                items.add(fridge.get(a));
            }
        }
        items.add(null);
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

        fridge.add(new FoodItem("Eggs", "M/P", "Egg.jpg"));
        fridge.add(new FoodItem("Tomato", "F/V", "Tomato.jpg"));
        fridge.add(new FoodItem("Spinach", "F/V", "Spinach.jpg"));
        fridge.add(new FoodItem("Corn Chips", "Carbs", "CornChips.jpg"));
        fridge.add(new FoodItem("Milk", "Dairy", "Milk.jpg"));
        fridge.add(new FoodItem("Ground Beef", "M/P", "GroundBeef.jpg"));
        fridge.add(new FoodItem("Parmesan", "Dairy", "Parmesan.jpg"));
        fridge.add(new FoodItem("Goat Cheese", "Dairy", "GoatCheese.jpg"));
        fridge.add(new FoodItem("Chicken Breast", "M/P", "ChickenBreast.jpg"));
        fridge.add(new FoodItem("Onion", "F/V", "Onion.jpg"));
        fridge.add(new FoodItem("Sourdough", "Carbs", "Sourdough.jpg"));
        fridge.add(new FoodItem("Lettuce", "F/V", "Lettuce.jpg"));
        fridge.add(new FoodItem("Salami", "M/P", "Salami.jpg"));

    }
}
