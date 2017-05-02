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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private RadioGroup rg1;
    private RadioGroup rg2;
    private RadioButton lacto_veg;
    private RadioButton ovo_veg;
    private RadioButton pesc;
    private RadioButton vegan;
    private RadioButton vegetarian;
    private RadioButton male;
    private RadioButton female;
    private RadioButton nonbinary;

    private RadioGroup.OnCheckedChangeListener listener1 = null;
    private RadioGroup.OnCheckedChangeListener listener2 = null;


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

        //RadioButtons for diets
        lacto_veg = (RadioButton) rootView.findViewById(R.id.lacto_veg);
        ovo_veg = (RadioButton) rootView.findViewById(R.id.ovo_veg);
        pesc = (RadioButton) rootView.findViewById(R.id.pescetarian);
        vegan = (RadioButton) rootView.findViewById(R.id.vegan);
        vegetarian = (RadioButton) rootView.findViewById(R.id.vegetarian);

        //RadioButtons for gender
        male = (RadioButton) rootView.findViewById(R.id.male);
        female = (RadioButton) rootView.findViewById(R.id.female);
        nonbinary = (RadioButton) rootView.findViewById(R.id.nonbinary);

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

        //Gender RadioButtons
        boolean checkedMale = myPrefs.getBoolean("male", false);
        male.setChecked(checkedMale);

        boolean checkedFemale = myPrefs.getBoolean("female", false);
        female.setChecked(checkedFemale);

        boolean checkedNonBinary = myPrefs.getBoolean("nonbinary", false);
        nonbinary.setChecked(checkedNonBinary);

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

        listener1 = new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                    rg2.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
                    rg2.clearCheck(); // clear the second RadioGroup!
                    rg2.setOnCheckedChangeListener(listener2);
            }
        };

        listener2 = new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                    rg1.setOnCheckedChangeListener(null);
                    rg1.clearCheck();
                    rg1.setOnCheckedChangeListener(listener1);
            }
        };

        rg1 = (RadioGroup) rootView.findViewById(R.id.rg1);
        rg2 = (RadioGroup) rootView.findViewById(R.id.rg2);
        rg1.setOnCheckedChangeListener(listener1);
        rg2.setOnCheckedChangeListener(listener2);




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

        if(american.isChecked()) {
            peditor.putBoolean("american", true);
        } else {
            peditor.putBoolean("american", false);
        }

        if(chinese.isChecked()) {
            peditor.putBoolean("chinese", true);
        } else {
            peditor.putBoolean("chinese", false);
        }

        if(english.isChecked()) {
            peditor.putBoolean("english", true);
        } else {
            peditor.putBoolean("english", false);
        }

        if(french.isChecked()) {
            peditor.putBoolean("french", true);
        } else {
            peditor.putBoolean("french", false);
        }

        if(greek.isChecked()) {
            peditor.putBoolean("greek", true);
        } else {
            peditor.putBoolean("greek", false);
        }

        if(hawaiin.isChecked()) {
            peditor.putBoolean("hawaiin", true);
        } else {
            peditor.putBoolean("hawaiin", false);
        }

        if(indian.isChecked()) {
            peditor.putBoolean("indian", true);
        } else {
            peditor.putBoolean("indian", false);
        }

        if(italian.isChecked()) {
            peditor.putBoolean("italian", true);
        } else {
            peditor.putBoolean("italian", false);
        }

        if(japanese.isChecked()) {
            peditor.putBoolean("japanese", true);
        } else {
            peditor.putBoolean("japanese", false);
        }

        if(mediterrenean.isChecked()) {
            peditor.putBoolean("mediterrenean", true);
        } else {
            peditor.putBoolean("mediterrenean", false);
        }

        if(mexican.isChecked()) {
            peditor.putBoolean("mexican", true);
        } else {
            peditor.putBoolean("mexican", false);
        }

        if(thai.isChecked()) {
            peditor.putBoolean("thai", true);
        } else {
            peditor.putBoolean("thai", false);
        }

        if(lacto_veg.isChecked()) {
            peditor.putBoolean("lacto_veg", true);
        } else {
            peditor.putBoolean("lacto_veg", false);
        }

        if(ovo_veg.isChecked()) {
            peditor.putBoolean("ovo_veg", true);
        } else {
            peditor.putBoolean("ovo_veg", false);
        }

        if(pesc.isChecked()) {
            peditor.putBoolean("pesc", true);
        } else {
            peditor.putBoolean("pesc", false);
        }

        if(vegan.isChecked()) {
            peditor.putBoolean("vegan", true);
        } else {
            peditor.putBoolean("vegan", false);
        }

        if(vegetarian.isChecked()) {
            peditor.putBoolean("vegetarian", true);
        } else {
            peditor.putBoolean("vegetarian", false);
        }

        if(dairy.isChecked()) {
            peditor.putBoolean("dairy", true);
        } else {
            peditor.putBoolean("dairy", false);
        }

        if(egg.isChecked()) {
            peditor.putBoolean("egg", true);
        } else {
            peditor.putBoolean("egg", false);
        }

        if(gluten.isChecked()) {
            peditor.putBoolean("gluten", true);
        } else {
            peditor.putBoolean("gluten", false);
        }

        if(peanut.isChecked()) {
            peditor.putBoolean("peanut", true);
        } else {
            peditor.putBoolean("peanut", false);
        }

        if(seafood.isChecked()) {
            peditor.putBoolean("seafood", true);
        } else {
            peditor.putBoolean("seafood", false);
        }

        if(sesame.isChecked()) {
            peditor.putBoolean("sesame", true);
        } else {
            peditor.putBoolean("sesame", false);
        }

        if(soy.isChecked()) {
            peditor.putBoolean("soy", true);
        } else {
            peditor.putBoolean("soy", false);
        }

        if(sulfite.isChecked()) {
            peditor.putBoolean("sulfite", true);
        } else {
            peditor.putBoolean("sulfite", false);
        }

        if(treenut.isChecked()) {
            peditor.putBoolean("treenut", true);
        } else {
            peditor.putBoolean("treenut", false);
        }

        if(wheat.isChecked()) {
            peditor.putBoolean("wheat", true);
        } else {
            peditor.putBoolean("wheat", false);
        }

        if(male.isChecked()) {
            peditor.putBoolean("male", true);
        } else {
            peditor.putBoolean("male", false);
        }

        if(female.isChecked()) {
            peditor.putBoolean("female", true);
        } else {
            peditor.putBoolean("female", false);
        }
        if(nonbinary.isChecked()) {
            peditor.putBoolean("nonbinary", true);
        } else {
            peditor.putBoolean("nonbinary", false);
        }
        peditor.apply();

        Context context = getActivity().getApplicationContext();
        CharSequence text = "Settings Successfully Updated!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
