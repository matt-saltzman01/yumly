package com.example.matt.yumly20;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Random;


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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setTitle("Home");
        buildImageScrolls();
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

    public void buildImageScrolls() {

        ArrayList<Integer> pics = new ArrayList<Integer>();
        pics.add(R.drawable.tacos);
        pics.add(R.drawable.ramen);
        pics.add(R.drawable.salmon);
        pics.add(R.drawable.crabcake);
        pics.add(R.drawable.brownies);

        ImageLoader imageLoader = ImageLoader.getInstance();

        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.week_recipes_linear);
        for (int i = 0; i < pics.size(); i++) {
            final ImageView imageView = new ImageView(getActivity());
            imageView.setId(i);
            imageView.setPadding(0, 0, 0, 0);

            imageLoader.loadImage("drawable://" + pics.get(i), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    imageView.setImageBitmap(loadedImage);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
            });

/*            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), pics.get(i)));
            imageView.setScaleType(ImageView.ScaleType.CENTER);*/

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) getActivity()).myWeekClick(view);
                }
            });

            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            layout.addView(imageView);
        }

        ArrayList<String> picUrls = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        String sql = String.format("SELECT * FROM Recipes");
        Cursor cursor = (new RecipeOpenHelper(getActivity())).getWritableDatabase()
                .rawQuery(sql, new String[] {});
        if (cursor.getCount() == 0) {

        } else {
            cursor.moveToFirst();
            picUrls.add(cursor.getString(3));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        ArrayList<String> finalUrls = new ArrayList<>();
        final ArrayList<String> finalNames = new ArrayList<>();
        while (finalUrls.size() < 5 && picUrls.size() > 0) {
            Random rand = new Random();
            int dex = rand.nextInt(picUrls.size());
            if (picUrls.size() > 0) {
                finalUrls.add(picUrls.get(dex));
                picUrls.remove(dex);
            }
            if (names.size() > 0) {
                finalNames.add(names.get(dex));
                names.remove(dex);
            }
        }


        layout = (LinearLayout) getActivity().findViewById(R.id.my_recipes_linear);
        for (int i = 0; i < finalUrls.size(); i++) {
            final ImageView imageView = new ImageView(getActivity());
            imageView.setId(i);
            imageView.setPadding(0, 0, 0, 0);
            final int index = i;

            imageLoader.loadImage(finalUrls.get(i), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    imageView.setImageBitmap(loadedImage);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
            });

/*            imageView.setImageBitmap(BitmapFactory.decodeResource(
                    getResources(), pics.get(i)));
            imageView.setScaleType(ImageView.ScaleType.CENTER);*/

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) getActivity()).myRecipeClick(finalNames.get(index));
                }
            });

            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            layout.addView(imageView);
        }

    }
}
