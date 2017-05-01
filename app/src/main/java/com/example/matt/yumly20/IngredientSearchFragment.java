package com.example.matt.yumly20;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.HttpGet;


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

    private static final String API_PREFIX = "http://api.yummly.com/v1";

    /*

    The allowedIngredient[] parameter must be set equal to a searchValue found in the Yummly
    Search Metadata Dictionaries. An example of an allowedIngredient[] searchValue is "garlic"
    to indicate you want to recipes which include garlic. To access the metadata dictionary for
    allowedIngredient[] searchValues, use the following end point:
    http://api.yummly.com/v1/api/metadata/ingredient?_app_id=YOUR_ID&_app_key=YOUR_APP_KEY

     */

    protected GridView gv;
    protected SearchView sv;
    protected IngredientSearchAdapter iSAdapter;
    protected List ingredients = new ArrayList<String>();

    protected ImageLoader imageLoader;
    protected Bitmap picMap;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public IngredientSearchFragment() {
        // Required empty public constructor
    }


    public class QueryBingImagesTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {

            //Below is temporary until search is implemented
            //This simply reads a sample JSON response for the Bing API for "bell pepper"
            return getSampleResponse();
        }

        @Override
        protected void onPostExecute(String result) {

            if (!result.equals("")) {
                try {
                    ingredients.clear();
                    JSONObject json = new JSONObject(result);
                    JSONArray images = json.getJSONArray("value");

                    for (int a = 0; a < images.length(); a++) {
                        JSONObject image = images.getJSONObject(a);
                        ingredients.add(image.getString("thumbnailUrl"));
                    }

                    ((TextView) getActivity().findViewById(R.id.search_instructions))
                            .setText("Select the image which best fits the item.");
                    gv.setAdapter(iSAdapter);

                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }
        }

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

        //populateRecipes();

        iSAdapter = new IngredientSearchAdapter(getActivity(), R.layout.ingredient_search_item,
                ingredients);
        gv = (GridView) getActivity().findViewById(R.id.ingredient_image_list);
        gv.setAdapter(iSAdapter);

        sv = (SearchView) getActivity().findViewById(R.id.is_search_view);
        sv.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        try {
                            new QueryBingImagesTask().execute(new URL("http://something.com"));
                        } catch (MalformedURLException mue) {
                            mue.printStackTrace();
                        }
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                }
        );

        /*lv.setOnItemClickListener(new android.widget.ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ((MainActivity) getActivity()).newIngredientClick(view);
            }
        });*/

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
        ingredients = new ArrayList<String>();
        //}

        ingredients.add("https://tvaraj.files.wordpress.com/2012/10/eggs.jpg");
        ingredients.add("http://www.fitnessvsweightloss.com/wp-content/uploads/2014/07/tomato.jpg");
        ingredients.add("http://iwantmysexyback.files.wordpress.com/2011/01/iron-source-spinach-lg.jpg");
        ingredients.add("http://www.classicexhibits.com/tradeshow-blog/wp-content/uploads/2012/10/" +
                        "Bread-Crumbs.jpg");
        ingredients.add("http://2.bp.blogspot.com/-M-HC4ThL9Hk/TgSuXPlvU4I/AAAAAAAAChE/YhLucY1d_no/s1600/" +
                        "milk-agriculture-commodities.jpg");
        ingredients.add("http://3.bp.blogspot.com/-M-vLu0438Ic/UPHGE418XYI/AAAAAAAAAGM/l2T5oDNTDaU/s1600/" +
                        "Ground+Beef+Recipe.jpg");
        ingredients.add("https://getbwoo.com/wp-content/uploads/2013/10/shutterstock_574773461.jpg");
        ingredients.add("http://www.cheesemaking.com/images/recipes/33Chevre/Pics/pic02.jpg");
        ingredients.add("http://img2.timeinc.net/health/img/web/2014/04/slides/" +
                        "chicken-breast-raw-400x400.jpg");

    }

    protected String getSampleResponse() {
        return "{\n" +
                "  \"_type\": \"Images\",\n" +
                "  \"instrumentation\": {\n" +
                "    \"pageLoadPingUrl\": \"https://www.bingapis.com/api/ping/pageload?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&Type=Event.CPT&DATA=0\"\n" +
                "  },\n" +
                "  \"webSearchUrl\": \"https://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=z87g6Bf75w3zgihOEfrF94sDU7ddUX_PI1Kciu38Okk&v=1&r=https%3a%2f%2fwww.bing.com%2fimages%2fsearch%3fq%3dbell%2520pepper%26FORM%3dOIIARP&p=DevEx,5105.1\",\n" +
                "  \"totalEstimatedMatches\": 140,\n" +
                "  \"value\": [\n" +
                "    {\n" +
                "      \"name\": \"Bell+pepper.jpg\",\n" +
                "      \"webSearchUrl\": \"https://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=IJUJjYn82kNHzy6Ts-EhlPxT1LsLBiTovb4YwhGz6_Y&v=1&r=https%3a%2f%2fwww.bing.com%2fimages%2fsearch%3fview%3ddetailv2%26FORM%3dOIIRPO%26q%3dbell%2bpepper%26id%3d6D403D38177D78469F35C90117DBDE8485F11EA8%26simid%3d608040819372919344&p=DevEx,5006.1\",\n" +
                "      \"thumbnailUrl\": \"https://tse3.mm.bing.net/th?id=OIP.1J-NdPsy2yJKdg_axHu6fgD6D5&pid=Api\",\n" +
                "      \"datePublished\": \"2013-05-07T12:00:00\",\n" +
                "      \"contentUrl\": \"http://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=4nvRafsVjtDUXSIH8URvvted4znq2qS2YEgAp1NyBJE&v=1&r=http%3a%2f%2f2.bp.blogspot.com%2f-CnsRJ1ENCNw%2fUYnH95Bw7rI%2fAAAAAAAANV0%2f206ihqs0yCM%2fs1600%2fBell%2bpepper.jpg&p=DevEx,5008.1\",\n" +
                "      \"hostPageUrl\": \"http://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=f1NcKNI5Ty8xpoKUE2tL4aSyacuu52dZbtoApfz71D0&v=1&r=http%3a%2f%2fneetustastytreats.blogspot.com%2f2013%2f05%2fvegetarian-stuffed-bell-peppers.html&p=DevEx,5007.1\",\n" +
                "      \"contentSize\": \"66941 B\",\n" +
                "      \"encodingFormat\": \"jpeg\",\n" +
                "      \"hostPageDisplayUrl\": \"neetustastytreats.blogspot.com/2013/05/vegetarian-stuffed-bell...\",\n" +
                "      \"width\": 866,\n" +
                "      \"height\": 864,\n" +
                "      \"thumbnail\": {\n" +
                "        \"width\": 250,\n" +
                "        \"height\": 249\n" +
                "      },\n" +
                "      \"imageInsightsToken\": \"ccid_1J+NdPsy*mid_6D403D38177D78469F35C90117DBDE8485F11EA8*simid_608040819372919344\",\n" +
                "      \"insightsSourcesSummary\": {\n" +
                "        \"shoppingSourcesCount\": 8,\n" +
                "        \"recipeSourcesCount\": 0\n" +
                "      },\n" +
                "      \"imageId\": \"6D403D38177D78469F35C90117DBDE8485F11EA8\",\n" +
                "      \"accentColor\": \"C39D08\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Description Green-Bell-Pepper.jpg\",\n" +
                "      \"webSearchUrl\": \"https://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=PeKdUQBOnxpWOgtOHvas2x8TNxd-nxT1fz8R3oUeVeQ&v=1&r=https%3a%2f%2fwww.bing.com%2fimages%2fsearch%3fview%3ddetailv2%26FORM%3dOIIRPO%26q%3dbell%2bpepper%26id%3d571DF9AC0E00D8CD73E0BE75730FFE9B4023868E%26simid%3d608014297937741215&p=DevEx,5012.1\",\n" +
                "      \"thumbnailUrl\": \"https://tse1.mm.bing.net/th?id=OIP.7MHakyGZF9QyziMG93etigDWEs&pid=Api\",\n" +
                "      \"datePublished\": \"2015-06-10T12:00:00\",\n" +
                "      \"contentUrl\": \"http://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=ENKVYSLvxeftJ0MEhjYyvQzKoRJh8EuCtUqWD704uMQ&v=1&r=http%3a%2f%2fupload.wikimedia.org%2fwikipedia%2fcommons%2fb%2fb7%2fGreen-Bell-Pepper.jpg&p=DevEx,5014.1\",\n" +
                "      \"hostPageUrl\": \"http://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=xVrrpu8zBLoe1owVOspC-YVAsto4PQ89mEQquY3DQlE&v=1&r=http%3a%2f%2fcommons.wikimedia.org%2fwiki%2fFile%3aGreen-Bell-Pepper.jpg&p=DevEx,5013.1\",\n" +
                "      \"contentSize\": \"2496152 B\",\n" +
                "      \"encodingFormat\": \"jpeg\",\n" +
                "      \"hostPageDisplayUrl\": \"commons.wikimedia.org/wiki/File:Green-Bell-Pepper.jpg\",\n" +
                "      \"width\": 1900,\n" +
                "      \"height\": 2660,\n" +
                "      \"thumbnail\": {\n" +
                "        \"width\": 214,\n" +
                "        \"height\": 300\n" +
                "      },\n" +
                "      \"imageInsightsToken\": \"ccid_7MHakyGZ*mid_571DF9AC0E00D8CD73E0BE75730FFE9B4023868E*simid_608014297937741215\",\n" +
                "      \"insightsSourcesSummary\": {\n" +
                "        \"shoppingSourcesCount\": 2,\n" +
                "        \"recipeSourcesCount\": 0\n" +
                "      },\n" +
                "      \"imageId\": \"571DF9AC0E00D8CD73E0BE75730FFE9B4023868E\",\n" +
                "      \"accentColor\": \"48630A\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"free Bell Pepper clipart pictures\",\n" +
                "      \"webSearchUrl\": \"https://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=Je4m2REyPSAvj6nGozHoamUsKtQp6T5I2UuvnPV5HJg&v=1&r=https%3a%2f%2fwww.bing.com%2fimages%2fsearch%3fview%3ddetailv2%26FORM%3dOIIRPO%26q%3dbell%2bpepper%26id%3d5B766EBC1064CD1C5C53F230E893AE355C1065B3%26simid%3d608012159037014665&p=DevEx,5018.1\",\n" +
                "      \"thumbnailUrl\": \"https://tse3.mm.bing.net/th?id=OIP.uZIwL_m293K3HoSgFBWSDAEgDY&pid=Api\",\n" +
                "      \"datePublished\": \"2010-02-06T21:47:00\",\n" +
                "      \"contentUrl\": \"http://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=175PJgSxUTGkuyx3vbbI_HsuDkHISYfURW7MFuqBJbM&v=1&r=http%3a%2f%2fwww.freeclipartpictures.com%2fclipart%2fclip-art%2fpictures%2fbellpepper.jpg&p=DevEx,5020.1\",\n" +
                "      \"hostPageUrl\": \"http://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=r9rGNYHZxXQCye7Op_dBe1OAAgImhaHuh60_z6qh9-0&v=1&r=http%3a%2f%2fwww.freeclipartpictures.com%2fclipart%2ffood73.htm&p=DevEx,5019.1\",\n" +
                "      \"contentSize\": \"52926 B\",\n" +
                "      \"encodingFormat\": \"jpeg\",\n" +
                "      \"hostPageDisplayUrl\": \"www.freeclipartpictures.com/clipart/food73.htm\",\n" +
                "      \"width\": 1024,\n" +
                "      \"height\": 768,\n" +
                "      \"thumbnail\": {\n" +
                "        \"width\": 288,\n" +
                "        \"height\": 216\n" +
                "      },\n" +
                "      \"imageInsightsToken\": \"ccid_uZIwL/m2*mid_5B766EBC1064CD1C5C53F230E893AE355C1065B3*simid_608012159037014665\",\n" +
                "      \"insightsSourcesSummary\": {\n" +
                "        \"shoppingSourcesCount\": 2,\n" +
                "        \"recipeSourcesCount\": 0\n" +
                "      },\n" +
                "      \"imageId\": \"5B766EBC1064CD1C5C53F230E893AE355C1065B3\",\n" +
                "      \"accentColor\": \"3D7101\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Yankee Bell Pepper_DenverFarmer.com\",\n" +
                "      \"webSearchUrl\": \"https://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=xQuB0WUsII3Na-sxJH5ZpDswU6xi225Rrz568mBzm00&v=1&r=https%3a%2f%2fwww.bing.com%2fimages%2fsearch%3fview%3ddetailv2%26FORM%3dOIIRPO%26q%3dbell%2bpepper%26id%3dB62A94BFAA47E5781F09A14B952BD1542B754817%26simid%3d608039079911686676&p=DevEx,5024.1\",\n" +
                "      \"thumbnailUrl\": \"https://tse2.mm.bing.net/th?id=OIP.xQiH01tboa-by2bE99Z8sAD9Es&pid=Api\",\n" +
                "      \"datePublished\": \"2015-02-17T00:44:00\",\n" +
                "      \"contentUrl\": \"http://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=xQSbSzcrzajsCm3go9px3pjUT1taOaprRhehuws2PrY&v=1&r=http%3a%2f%2fdenverfarmer.com%2fimages%2fstories%2fvirtuemart%2fproduct%2fYankee%2520Bell%2520Pepper_DenverFarmer.com.jpg&p=DevEx,5026.1\",\n" +
                "      \"hostPageUrl\": \"http://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=KAyfpqqIMSE6rqg5YepRZaKUt_FiyowuPgp9ESb_TKQ&v=1&r=http%3a%2f%2fcoloradoorganicfarmer.com%2f&p=DevEx,5025.1\",\n" +
                "      \"contentSize\": \"59315 B\",\n" +
                "      \"encodingFormat\": \"jpeg\",\n" +
                "      \"hostPageDisplayUrl\": \"coloradoorganicfarmer.com\",\n" +
                "      \"width\": 900,\n" +
                "      \"height\": 1066,\n" +
                "      \"thumbnail\": {\n" +
                "        \"width\": 253,\n" +
                "        \"height\": 300\n" +
                "      },\n" +
                "      \"imageInsightsToken\": \"ccid_xQiH01tb*mid_B62A94BFAA47E5781F09A14B952BD1542B754817*simid_608039079911686676\",\n" +
                "      \"insightsSourcesSummary\": {\n" +
                "        \"shoppingSourcesCount\": 0,\n" +
                "        \"recipeSourcesCount\": 0\n" +
                "      },\n" +
                "      \"imageId\": \"B62A94BFAA47E5781F09A14B952BD1542B754817\",\n" +
                "      \"accentColor\": \"154900\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"bell peppers bell peppers are a member of the capsicum annuum species ...\",\n" +
                "      \"webSearchUrl\": \"https://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=g7DhU_VHi8sGLDr1w8-aG2-wCvWSXDUn6vtY2mD8jZs&v=1&r=https%3a%2f%2fwww.bing.com%2fimages%2fsearch%3fview%3ddetailv2%26FORM%3dOIIRPO%26q%3dbell%2bpepper%26id%3d0D5E238793782132ABD6FBC4CCD0408765F5845E%26simid%3d608054554660241997&p=DevEx,5030.1\",\n" +
                "      \"thumbnailUrl\": \"https://tse3.mm.bing.net/th?id=OIP.HRc4FE1McbQ0elATAo3eegEsDP&pid=Api\",\n" +
                "      \"datePublished\": \"2014-10-25T21:45:00\",\n" +
                "      \"contentUrl\": \"http://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=mYy1MJ3bf1fUQ1SD3EfwtufWNqxnwCkVq3btGi83D7A&v=1&r=http%3a%2f%2fwww.picpedia.org%2fimages%2fphotos%2fb%2fbell-peppers08.jpg&p=DevEx,5032.1\",\n" +
                "      \"hostPageUrl\": \"http://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=hbniM088qj2bQyeACMGCOaewDvzf1WWnEs_PDbaQmQc&v=1&r=http%3a%2f%2fwww.picpedia.org%2fb%2fbell-peppers.html&p=DevEx,5031.1\",\n" +
                "      \"contentSize\": \"43364 B\",\n" +
                "      \"encodingFormat\": \"jpeg\",\n" +
                "      \"hostPageDisplayUrl\": \"www.picpedia.org/b/bell-peppers.html\",\n" +
                "      \"width\": 800,\n" +
                "      \"height\": 553,\n" +
                "      \"thumbnail\": {\n" +
                "        \"width\": 300,\n" +
                "        \"height\": 207\n" +
                "      },\n" +
                "      \"imageInsightsToken\": \"ccid_HRc4FE1M*mid_0D5E238793782132ABD6FBC4CCD0408765F5845E*simid_608054554660241997\",\n" +
                "      \"insightsSourcesSummary\": {\n" +
                "        \"shoppingSourcesCount\": 0,\n" +
                "        \"recipeSourcesCount\": 0\n" +
                "      },\n" +
                "      \"imageId\": \"0D5E238793782132ABD6FBC4CCD0408765F5845E\",\n" +
                "      \"accentColor\": \"231800\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Sweet bell pepper: 0 Scovilles. The typical green bell pepper, about ...\",\n" +
                "      \"webSearchUrl\": \"https://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=bgT8zpXxRdBsUg75vgslG3_9oUw3NO5CclAlFP_UeMU&v=1&r=https%3a%2f%2fwww.bing.com%2fimages%2fsearch%3fview%3ddetailv2%26FORM%3dOIIRPO%26q%3dbell%2bpepper%26id%3dF93507B8F40A6721B4CADF4920C4A5FA0F5A060A%26simid%3d608015002297042904&p=DevEx,5036.1\",\n" +
                "      \"thumbnailUrl\": \"https://tse2.mm.bing.net/th?id=OIP.VhyFY0JN3GEMqHQzeP0xkQEgEs&pid=Api\",\n" +
                "      \"datePublished\": \"2017-03-29T02:41:00\",\n" +
                "      \"contentUrl\": \"http://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=KiR6Ea7H0XicysiXvZCbIATKLPVWzXFIjgsPmDQ4SiM&v=1&r=http%3a%2f%2fwww.bibsnbobs.co.uk%2ftriviafile%2fPclips%2fBell.jpg&p=DevEx,5038.1\",\n" +
                "      \"hostPageUrl\": \"http://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=cXR23N7kVSk1k1oWGSxEpz7osrxBGQAaeDGd0TablRw&v=1&r=http%3a%2f%2fwww.bibsnbobs.co.uk%2ftriviafile%2fChiliPeppers.html&p=DevEx,5037.1\",\n" +
                "      \"contentSize\": \"14100 B\",\n" +
                "      \"encodingFormat\": \"jpeg\",\n" +
                "      \"hostPageDisplayUrl\": \"www.bibsnbobs.co.uk/triviafile/ChiliPeppers.html\",\n" +
                "      \"width\": 512,\n" +
                "      \"height\": 533,\n" +
                "      \"thumbnail\": {\n" +
                "        \"width\": 288,\n" +
                "        \"height\": 300\n" +
                "      },\n" +
                "      \"imageInsightsToken\": \"ccid_VhyFY0JN*mid_F93507B8F40A6721B4CADF4920C4A5FA0F5A060A*simid_608015002297042904\",\n" +
                "      \"insightsSourcesSummary\": {\n" +
                "        \"shoppingSourcesCount\": 1,\n" +
                "        \"recipeSourcesCount\": 0\n" +
                "      },\n" +
                "      \"imageId\": \"F93507B8F40A6721B4CADF4920C4A5FA0F5A060A\",\n" +
                "      \"accentColor\": \"344F11\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Before you bite, do you know how hot those peppers are?\",\n" +
                "      \"webSearchUrl\": \"https://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=Nq8hSHDtGFNlgGlQzNvGMZng0qfi3whBBXsGINffz_Y&v=1&r=https%3a%2f%2fwww.bing.com%2fimages%2fsearch%3fview%3ddetailv2%26FORM%3dOIIRPO%26q%3dbell%2bpepper%26id%3d791CFCE01E5D24383888308C249AC53246C01606%26simid%3d608051917542263260&p=DevEx,5042.1\",\n" +
                "      \"thumbnailUrl\": \"https://tse1.mm.bing.net/th?id=OIP.49zMsGtN0hoFw8BMQzjb8QEyDM&pid=Api\",\n" +
                "      \"datePublished\": \"2016-02-21T22:27:00\",\n" +
                "      \"contentUrl\": \"http://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=dz0n0Zv72wBOtCCDDKxqFtVSTDbn3OyWbV0R4MshQT4&v=1&r=http%3a%2f%2fcdn.sheknows.com%2farticles%2f2014%2f10%2fKMiner%2fpeppers%2f478991905.jpg&p=DevEx,5044.1\",\n" +
                "      \"hostPageUrl\": \"http://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=yAF-swWPOXZalu-FWqBcC5-4OaoxDMRxsQaavHizYXY&v=1&r=http%3a%2f%2fwww.sheknows.com%2ffood-and-recipes%2farticles%2f805359%2ftypes-of-hot-peppers&p=DevEx,5043.1\",\n" +
                "      \"contentSize\": \"4371006 B\",\n" +
                "      \"encodingFormat\": \"jpeg\",\n" +
                "      \"hostPageDisplayUrl\": \"www.sheknows.com/food-and-recipes/articles/805359/types-of-hot-peppers\",\n" +
                "      \"width\": 5616,\n" +
                "      \"height\": 3744,\n" +
                "      \"thumbnail\": {\n" +
                "        \"width\": 306,\n" +
                "        \"height\": 204\n" +
                "      },\n" +
                "      \"imageInsightsToken\": \"ccid_49zMsGtN*mid_791CFCE01E5D24383888308C249AC53246C01606*simid_608051917542263260\",\n" +
                "      \"insightsSourcesSummary\": {\n" +
                "        \"shoppingSourcesCount\": 1,\n" +
                "        \"recipeSourcesCount\": 0\n" +
                "      },\n" +
                "      \"imageId\": \"791CFCE01E5D24383888308C249AC53246C01606\",\n" +
                "      \"accentColor\": \"BD9A0E\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"File:Bell pepper.jpg\",\n" +
                "      \"webSearchUrl\": \"https://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=UztwqkMvd-C_qSwDZw0y_U0KJRScdo-Mr_iBuz3kVU0&v=1&r=https%3a%2f%2fwww.bing.com%2fimages%2fsearch%3fview%3ddetailv2%26FORM%3dOIIRPO%26q%3dbell%2bpepper%26id%3dF0A35F2973F7921AAA6E741DB799DF6024D038F0%26simid%3d608033956006727614&p=DevEx,5048.1\",\n" +
                "      \"thumbnailUrl\": \"https://tse1.mm.bing.net/th?id=OIP.-QNrHuaWh5Rj05uS4h5OwgEsDI&pid=Api\",\n" +
                "      \"datePublished\": \"2012-03-22T12:00:00\",\n" +
                "      \"contentUrl\": \"http://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=E162Ca4HEUqVsQer-FNdDoU5il9Xl5pz5_hEVR4WQns&v=1&r=http%3a%2f%2fupload.wikimedia.org%2fwikipedia%2fcommons%2fc%2fc0%2fBell_pepper.jpg&p=DevEx,5050.1\",\n" +
                "      \"hostPageUrl\": \"http://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=GnuAoppXOdxrQ1J1tfRoHBNl5iKqR1XIjN9jsuj0ZqE&v=1&r=http%3a%2f%2fcommons.wikimedia.org%2fwiki%2fFile%3aBell_pepper.jpg&p=DevEx,5049.1\",\n" +
                "      \"contentSize\": \"120483 B\",\n" +
                "      \"encodingFormat\": \"jpeg\",\n" +
                "      \"hostPageDisplayUrl\": \"commons.wikimedia.org/wiki/File:Bell_pepper.jpg\",\n" +
                "      \"width\": 1024,\n" +
                "      \"height\": 683,\n" +
                "      \"thumbnail\": {\n" +
                "        \"width\": 300,\n" +
                "        \"height\": 200\n" +
                "      },\n" +
                "      \"imageInsightsToken\": \"ccid_+QNrHuaW*mid_F0A35F2973F7921AAA6E741DB799DF6024D038F0*simid_608033956006727614\",\n" +
                "      \"insightsSourcesSummary\": {\n" +
                "        \"shoppingSourcesCount\": 1,\n" +
                "        \"recipeSourcesCount\": 0\n" +
                "      },\n" +
                "      \"imageId\": \"F0A35F2973F7921AAA6E741DB799DF6024D038F0\",\n" +
                "      \"accentColor\": \"C73604\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Bell Pepper or Capsicum – Capsaicin, Dihydrocapsiate and Piperine ...\",\n" +
                "      \"webSearchUrl\": \"https://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=X5Dkfv2J1XWjD7iBzmyieHEr_aFFWdB7x3U1fuhn_Kk&v=1&r=https%3a%2f%2fwww.bing.com%2fimages%2fsearch%3fview%3ddetailv2%26FORM%3dOIIRPO%26q%3dbell%2bpepper%26id%3d8974F57EF8ED700D7AD672CF253AC886B75CF31A%26simid%3d608019696696427848&p=DevEx,5054.1\",\n" +
                "      \"thumbnailUrl\": \"https://tse2.mm.bing.net/th?id=OIP.faI3JxYizpkj1Z8Qphhi4wEsCv&pid=Api\",\n" +
                "      \"datePublished\": \"2016-01-05T12:09:00\",\n" +
                "      \"contentUrl\": \"http://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=6xhFfik_iHo2ANBVtfNVN3mq1ppa3Ysr1AD_rzbwzeM&v=1&r=http%3a%2f%2fallinallnews.com%2fwp-content%2fuploads%2f2016%2f01%2fBell-Pepper.jpg&p=DevEx,5056.1\",\n" +
                "      \"hostPageUrl\": \"http://www.bing.com/cr?IG=8E3F4D7FC32B43A8B6FA17D655387E96&CID=29C16337B38B67EC08D56942B2D466C6&rd=1&h=lvFc9D3Y0kkbP48Brk6OsqK7qyox6FnfLT2v_wRm5Wc&v=1&r=http%3a%2f%2fwww.allinallnews.com%2fhealth%2ffastest-weight-loss-diet-in-india&p=DevEx,5055.1\",\n" +
                "      \"contentSize\": \"30573 B\",\n" +
                "      \"encodingFormat\": \"jpeg\",\n" +
                "      \"hostPageDisplayUrl\": \"www.allinallnews.com/health/fastest-weight-loss-diet-in-india\",\n" +
                "      \"width\": 800,\n" +
                "      \"height\": 467,\n" +
                "      \"thumbnail\": {\n" +
                "        \"width\": 300,\n" +
                "        \"height\": 175\n" +
                "      },\n" +
                "      \"imageInsightsToken\": \"ccid_faI3JxYi*mid_8974F57EF8ED700D7AD672CF253AC886B75CF31A*simid_608019696696427848\",\n" +
                "      \"insightsSourcesSummary\": {\n" +
                "        \"shoppingSourcesCount\": 0,\n" +
                "        \"recipeSourcesCount\": 0\n" +
                "      },\n" +
                "      \"imageId\": \"8974F57EF8ED700D7AD672CF253AC886B75CF31A\",\n" +
                "      \"accentColor\": \"C99F02\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"displayShoppingSourcesBadges\": false,\n" +
                "  \"displayRecipeSourcesBadges\": true\n" +
                "}";
    }

}
