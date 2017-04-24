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

        height = (EditText) rootView.findViewById(R.id.height);
        weight = (EditText) rootView.findViewById(R.id.weight);
        calories = (EditText) rootView.findViewById(R.id.calories);
        cholesteral = (EditText) rootView.findViewById(R.id.cholesteral);
        fat = (EditText) rootView.findViewById(R.id.fat);
        protein = (EditText) rootView.findViewById(R.id.protein);
        sodium = (EditText) rootView.findViewById(R.id.sodium);

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

        peditor.commit();

        Context context = getActivity().getApplicationContext();
        CharSequence text = "Settings Successfully Updated!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
