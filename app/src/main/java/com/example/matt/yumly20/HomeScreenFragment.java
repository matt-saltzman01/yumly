package com.example.matt.yumly20;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeScreenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeScreenFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeScreenFragment newInstance(String param1, String param2) {
        HomeScreenFragment fragment = new HomeScreenFragment();
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
        View view =  inflater.inflate(R.layout.fragment_home_screen, container, false);
        buildImageScrolls(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("Home");
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

    public void buildImageScrolls(View view) {

        ArrayList<Integer> pics = new ArrayList<Integer>();
        pics.add(R.drawable.tacos);
        pics.add(R.drawable.ramen);
        pics.add(R.drawable.salmon);
        pics.add(R.drawable.crabcake);
        pics.add(R.drawable.brownies);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.week_recipes_linear);
        for (int i = 0; i < pics.size(); i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setId(i);
            imageView.setPadding(0, 0, 0, 0);
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), pics.get(i)));
            imageView.setScaleType(ImageView.ScaleType.CENTER);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) getActivity()).myWeekClick(view);
                }
            });

            layout.addView(imageView);
        }

        pics = new ArrayList();
        pics.add(R.drawable.friedrice);
        pics.add(R.drawable.guacamole);
        pics.add(R.drawable.everydaybakedchicken);
        pics.add(R.drawable.burger);
        pics.add(R.drawable.pasta);

        layout = (LinearLayout) view.findViewById(R.id.my_recipes_linear);
        for (int i = 0; i < pics.size(); i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setId(i);
            imageView.setPadding(0, 0, 0, 0);
            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), pics.get(i)));
            imageView.setScaleType(ImageView.ScaleType.CENTER);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) getActivity()).myRecipeClick(view);
                }
            });

            layout.addView(imageView);
        }
    }
}
