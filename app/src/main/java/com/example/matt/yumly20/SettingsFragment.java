package com.example.matt.yumly20;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private SharedPreferences myPrefs;
    private EditText height;
    private EditText weight;
    private EditText calories;
    private EditText cholesteral;
    private EditText fat;
    private EditText protein;
    private EditText sodium;
    private CheckBox american;
    private CheckBox chinese;
    private CheckBox english;
    private CheckBox french;
    private CheckBox greek;
    private CheckBox hawaiin;
    private CheckBox indian;
    private CheckBox italian;
    private CheckBox japanese;
    private CheckBox mediterrenean;
    private CheckBox mexican;
    private CheckBox thai;
    private CheckBox lacto_veg;
    private CheckBox ovo_veg;
    private CheckBox pesc;
    private CheckBox vegan;
    private CheckBox vegetarian;
    private CheckBox dairy;
    private CheckBox egg;
    private CheckBox gluten;
    private CheckBox peanut;
    private CheckBox seafood;
    private CheckBox sesame;
    private CheckBox soy;
    private CheckBox sulfite;
    private CheckBox treenut;
    private CheckBox wheat;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        Spinner hspinner = (Spinner) rootView.findViewById(R.id.height_spinner);
        Spinner wspinner = (Spinner) rootView.findViewById(R.id.weight_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> hadapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.height_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> wadapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.weight_array, android.R.layout.simple_spinner_item);

        //Specify the layout to use when the list of choices appears
        hadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Apply the adapter to the spinner
        hspinner.setAdapter(hadapter);
        wspinner.setAdapter(wadapter);

        Context context = getActivity().getApplicationContext(); // app level storage
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        //Checkboxes for favorite cuisines
        american = (CheckBox) rootView.findViewById(R.id.american);
        chinese = (CheckBox) rootView.findViewById(R.id.chinese);
        english = (CheckBox) rootView.findViewById(R.id.english);
        french = (CheckBox) rootView.findViewById(R.id.french);
        greek = (CheckBox) rootView.findViewById(R.id.greek);
        hawaiin = (CheckBox) rootView.findViewById(R.id.hawaiin);
        indian = (CheckBox) rootView.findViewById(R.id.indian);
        italian = (CheckBox) rootView.findViewById(R.id.italian);
        japanese = (CheckBox) rootView.findViewById(R.id.japanese);
        mediterrenean = (CheckBox) rootView.findViewById(R.id.mediterrenean);
        mexican = (CheckBox) rootView.findViewById(R.id.mexican);
        thai = (CheckBox) rootView.findViewById(R.id.thai);

        //Checkboxes for diets
        lacto_veg = (CheckBox) rootView.findViewById(R.id.lacto_veg);
        ovo_veg = (CheckBox) rootView.findViewById(R.id.ovo_veg);
        pesc = (CheckBox) rootView.findViewById(R.id.pescetarian);
        vegan = (CheckBox) rootView.findViewById(R.id.vegan);
        vegetarian = (CheckBox) rootView.findViewById(R.id.vegetarian);

        //Checkboxes for allergies
        dairy = (CheckBox) rootView.findViewById(R.id.dairy);
        egg = (CheckBox) rootView.findViewById(R.id.egg);
        gluten = (CheckBox) rootView.findViewById(R.id.gluten);
        peanut = (CheckBox) rootView.findViewById(R.id.peanut);
        seafood = (CheckBox) rootView.findViewById(R.id.seafood);
        sesame = (CheckBox) rootView.findViewById(R.id.sesame);
        soy = (CheckBox) rootView.findViewById(R.id.soy);
        sulfite = (CheckBox) rootView.findViewById(R.id.sulfite);
        treenut = (CheckBox) rootView.findViewById(R.id.treenut);
        wheat = (CheckBox) rootView.findViewById(R.id.wheat);

        //get and set cuisine checkbox according to sharedprefs
        boolean checkedam = myPrefs.getBoolean("american", false);
        american.setChecked(checkedam);

        boolean checkedchi = myPrefs.getBoolean("chinese", false);
        chinese.setChecked(checkedchi);

        boolean checkedeng = myPrefs.getBoolean("english", false);
        english.setChecked(checkedeng);

        boolean checkedfr = myPrefs.getBoolean("french", false);
        french.setChecked(checkedfr);

        boolean checkedgr = myPrefs.getBoolean("greek", false);
        greek.setChecked(checkedgr);

        boolean checkedhaw = myPrefs.getBoolean("hawaiin", false);
        hawaiin.setChecked(checkedhaw);

        boolean checkedind = myPrefs.getBoolean("indian", false);
        indian.setChecked(checkedind);

        boolean checkedita = myPrefs.getBoolean("italian", false);
        italian.setChecked(checkedita);

        boolean checkedjap = myPrefs.getBoolean("japanese", false);
        japanese.setChecked(checkedjap);

        boolean checkedmed = myPrefs.getBoolean("mediterrenean", false);
        mediterrenean.setChecked(checkedmed);

        boolean checkedmex = myPrefs.getBoolean("mexican", false);
        mexican.setChecked(checkedmex);

        boolean checkedthai = myPrefs.getBoolean("thai", false);
        thai.setChecked(checkedthai);

        //get and set diet checkbox according to sharedprefs
        boolean checkedlac = myPrefs.getBoolean("lacto_veg", false);
        lacto_veg.setChecked(checkedlac);

        boolean checkedovo = myPrefs.getBoolean("ovo_veg", false);
        ovo_veg.setChecked(checkedovo);

        boolean checkedpesc = myPrefs.getBoolean("pesc", false);
        pesc.setChecked(checkedpesc);

        boolean checkedvegan = myPrefs.getBoolean("vegan", false);
        vegan.setChecked(checkedvegan);

        boolean checkedveg = myPrefs.getBoolean("vegetarian", false);
        vegetarian.setChecked(checkedveg);

        //get and set allergies checkbox according to sharedprefs
        boolean checkeddairy = myPrefs.getBoolean("dairy", false);
        dairy.setChecked(checkeddairy);

        boolean checkedegg = myPrefs.getBoolean("egg", false);
        egg.setChecked(checkedegg);

        boolean checkedglu = myPrefs.getBoolean("gluten", false);
        gluten.setChecked(checkedglu);

        boolean checkedpea = myPrefs.getBoolean("peanut", false);
        peanut.setChecked(checkedpea);

        boolean checkedsea = myPrefs.getBoolean("seafood", false);
        seafood.setChecked(checkedsea);

        boolean checkedses = myPrefs.getBoolean("sesame", false);
        sesame.setChecked(checkedses);

        boolean checkedsoy = myPrefs.getBoolean("soy", false);
        soy.setChecked(checkedsoy);

        boolean checkedsul = myPrefs.getBoolean("sulfite", false);
        sulfite.setChecked(checkedsul);

        boolean checkedtree = myPrefs.getBoolean("treenut", false);
        treenut.setChecked(checkedtree);

        boolean checkedwhe = myPrefs.getBoolean("wheat", false);
        wheat.setChecked(checkedwhe);

        //Edit Text Fields for user inputs
        height = (EditText) rootView.findViewById(R.id.height);
        weight = (EditText) rootView.findViewById(R.id.weight);
        calories = (EditText) rootView.findViewById(R.id.calories);
        cholesteral = (EditText) rootView.findViewById(R.id.cholesteral);
        fat = (EditText) rootView.findViewById(R.id.fat);
        protein = (EditText) rootView.findViewById(R.id.protein);
        sodium = (EditText) rootView.findViewById(R.id.sodium);

        //set EditText fields to user settings
        height.setText(String.format("%.0f", myPrefs.getFloat("height", (float) 150)));
        weight.setText(String.format("%.0f", myPrefs.getFloat("weight", (float) 68)));
        calories.setText(String.format("%.0f", myPrefs.getFloat("calories", (float) 2000)));
        cholesteral.setText(String.format("%.0f", myPrefs.getFloat("cholesterol", (float) 3000)));
        fat.setText(String.format("%.0f", myPrefs.getFloat("fat", (float) 65)));
        protein.setText(String.format("%.0f", myPrefs.getFloat("protein", (float) 50)));
        sodium.setText(String.format("%.0f", myPrefs.getFloat("sodium", (float) 2400)));

        Button saveButton = (Button) rootView.findViewById(R.id.save_preferences_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave(v);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("Settings");
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

    public void onSave(View v) {

        SharedPreferences.Editor peditor = myPrefs.edit();

        peditor.putFloat("height", Float.parseFloat(height.getText().toString()));
        peditor.putFloat("weight", Float.parseFloat(weight.getText().toString()));
        peditor.putFloat("calories", Float.parseFloat(calories.getText().toString()));
        peditor.putFloat("cholesterol", Float.parseFloat(cholesteral.getText().toString()));
        peditor.putFloat("fat", Float.parseFloat(fat.getText().toString()));
        peditor.putFloat("protein", Float.parseFloat(protein.getText().toString()));
        peditor.putFloat("sodium", Float.parseFloat(sodium.getText().toString()));

        switch(getView().getId()) {
            case R.id.american:
                peditor.putBoolean("american", true);
                break;

            case R.id.chinese:
                peditor.putBoolean("chinese", true);
                break;

            case R.id.english:
                peditor.putBoolean("english", true);
                break;

            case R.id.french:
                peditor.putBoolean("french", true);
                break;

            case R.id.greek:
                peditor.putBoolean("greek", true);
                break;

            case R.id.hawaiin:
                peditor.putBoolean("hawaiin", true);
                break;

            case R.id.indian:
                peditor.putBoolean("indian", true);
                break;

            case R.id.italian:
                peditor.putBoolean("italian", true);
                break;

            case R.id.japanese:
                peditor.putBoolean("japanese", true);
                break;

            case R.id.mediterrenean:
                peditor.putBoolean("mediterrenean", true);
                break;

            case R.id.mexican:
                peditor.putBoolean("mexican", true);
                break;

            case R.id.thai:
                peditor.putBoolean("thai", true);
                break;

            case R.id.lacto_veg:
                peditor.putBoolean("lacto_veg", true);
                break;

            case R.id.ovo_veg:
                peditor.putBoolean("ovo_veg", true);
                break;

            case R.id.pescetarian:
                peditor.putBoolean("pesc", true);
                break;

            case R.id.vegan:
                peditor.putBoolean("vegan", true);
                break;

            case R.id.vegetarian:
                peditor.putBoolean("vegetarian", true);
                break;

            case R.id.dairy:
                peditor.putBoolean("dairy", true);
                break;

            case R.id.egg:
                peditor.putBoolean("egg", true);
                break;

            case R.id.gluten:
                peditor.putBoolean("gluten", true);
                break;

            case R.id.peanut:
                peditor.putBoolean("peanut", true);
                break;

            case R.id.seafood:
                peditor.putBoolean("seafood", true);
                break;

            case R.id.sesame:
                peditor.putBoolean("sesame", true);
                break;

            case R.id.soy:
                peditor.putBoolean("soy", true);
                break;

            case R.id.sulfite:
                peditor.putBoolean("sulfite", true);
                break;

            case R.id.treenut:
                peditor.putBoolean("treenut", true);
                break;

            case R.id.wheat:
                peditor.putBoolean("wheat", true);
                break;
        }

        peditor.commit();

        Context context = getActivity().getApplicationContext();
        CharSequence text = "Settings Successfully Updated!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
