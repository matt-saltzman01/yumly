package com.example.matt.yumly20;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        HomeScreenFragment.OnFragmentInteractionListener,
        MyFridgeFragment.OnFragmentInteractionListener,
        MyRecipesFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        RecipeFragment.OnFragmentInteractionListener,
        IngredientSearchFragment.OnFragmentInteractionListener {


    private static HomeScreenFragment homeScreenFragment = new HomeScreenFragment();
    private static MyFridgeFragment myFridgeFragment = new MyFridgeFragment();
    private static MyRecipesFragment myRecipesFragment = new MyRecipesFragment();
    private static SettingsFragment settingsFragment = new SettingsFragment();
    private static IngredientSearchFragment ingredSearchFragment = new IngredientSearchFragment();

    private Menu navMenu;

    private static Fragment currentFragment = homeScreenFragment;
    private CharSequence currentTitle = "Home";
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.covered_plate)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.primary));
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        android.support.v7.app.ActionBarDrawerToggle toggle = new
                android.support.v7.app.ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_closed);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        getFragmentManager().beginTransaction().replace(R.id.content_frame,
                currentFragment).commit();
        setTitle(currentTitle);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getFragmentManager();


        if (id == R.id.home_frag) {
            currentFragment = homeScreenFragment;
            currentTitle = "Home";
        } else if (id == R.id.fridge_frag) {
            currentFragment = myFridgeFragment;
            currentTitle = "My Fridge";
        } else if (id == R.id.my_recipes_frag) {
            currentFragment = myRecipesFragment;
            currentTitle = "Favorite Recipes";
        } else if (id == R.id.settings_frag) {
            currentFragment = settingsFragment;
            currentTitle = "Settings";
        }

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, currentFragment)
                .addToBackStack("")
                .commit();
        setTitle(currentTitle);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.action_search) {

            currentFragment = new SearchRecipesFragment();
            currentTitle = "Search Recipes";

        } else if (id == R.id.action_my_fridge) {

            currentFragment = myFridgeFragment;
            currentTitle = "My Fridge";
        }

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, currentFragment)
                .addToBackStack("")
                .commit();
        setTitle(currentTitle);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void findRecipesButtonClick(String checked) {
        currentFragment = (new SearchRecipesFragment()).newInstance(checked, "");
        currentTitle = "Recipe Search";
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, currentFragment)
                .commit();
        setTitle(currentTitle);
    }

    public void findNewIngredient(View view) {
        currentFragment = ingredSearchFragment;
        currentTitle = "Add Ingredient";
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, currentFragment)
                .commit();
        setTitle(currentTitle);
    }

    public void toFridgeButtonClick(View view) {
        currentFragment = myFridgeFragment;
        currentTitle = "My Fridge";
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, currentFragment)
                .addToBackStack("")
                .commit();
        setTitle(currentTitle);
    }

    public void searchRecipeClick(String id) {
        currentFragment = (new RecipeFragment()).newInstance("search", id);
        currentTitle = "Recipe Details";
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, currentFragment)
                .addToBackStack("")
                .commit();
        setTitle(currentTitle);
    }

    public void myRecipeClick(String id) {
        currentFragment = (new RecipeFragment()).newInstance("saved", id);
        currentTitle = "Recipe Details";
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, currentFragment)
                .addToBackStack("")
                .commit();
        setTitle(currentTitle);
    }

    public void newIngredientClick(final String name, final String url) {

        final CharSequence[] items = {"Meat/Protein", "Fruit/Veggies", "Dairy", "Carbs"};
        final boolean[] bools = {false, false, false, false};
        final int[] selected = new int[1];
        selected[0] = -1;
        final Context context = this;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Food Group");
        /*builder.setMessage("Choose a category for the item.");*/


        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selected[0] = which;
            }
        });

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FoodItem foodItem = new FoodItem();
                switch (selected[0]) {
                    case 0:
                        foodItem = new FoodItem(context, name, "M/P", url);
                        myFridgeFragment.group = "M/P";
                        break;
                    case 1:
                        foodItem = new FoodItem(context, name, "F/V", url);
                        myFridgeFragment.group = "F/V";
                        break;
                    case 2:
                        foodItem = new FoodItem(context, name, "Dairy", url);
                        myFridgeFragment.group = "Dairy";
                        break;
                    case 3:
                        foodItem = new FoodItem(context, name, "Carbs", url);
                        myFridgeFragment.group = "Carbs";
                        break;
                    default:
                        foodItem = null;
                        break;
                }

                if (foodItem != null) {
                    myFridgeFragment.fridge.add(foodItem);
                    currentFragment = myFridgeFragment;
                    currentTitle = "My Fridge";

                    getFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, currentFragment)
                            .commit();
                    setTitle(currentTitle);
                } else {
                    Toast.makeText(context, "Please select a food group.", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentFragment = myFridgeFragment;
                currentTitle = "My Fridge";

                getFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, currentFragment)
                        .commit();
                setTitle(currentTitle);

            }
        });

        builder.create().show();

    }

    public void noIngredientClick(View view) {
        currentFragment = myFridgeFragment;
        currentTitle = "My Fridge";
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, currentFragment)
                .commit();
        setTitle(currentTitle);
    }

    public void selectFromFridge(FoodItem item) {
        for (int a = 0; a < myFridgeFragment.fridge.size(); a++) {
            if (myFridgeFragment.fridge.get(a).equals(item)) {
                myFridgeFragment.fridge.get(a).checked = !myFridgeFragment.fridge.get(a).checked;
            }
        }
    }

    private Recipe buildSampleRecipe() {
        ArrayList<Ingredient> ingredients = new ArrayList();
        ingredients.add(new Ingredient("4", "chicken breasts"));
        ingredients.add(new Ingredient("1 teaspoon", "kosher salt"));
        ingredients.add(new Ingredient("1/2 teaspoon", "black pepper"));
        ingredients.add(new Ingredient("1/2 teaspoon", "onion powder"));
        ingredients.add(new Ingredient("1/2 teaspoon", "garlic powder"));
        ingredients.add(new Ingredient("1/2 teaspoon", "oregano"));
        ingredients.add(new Ingredient("1/2 teaspoon", "paprika"));

        ArrayList<String> directions = new ArrayList();
        directions.add("Preheat oven to 425 degrees.");
        directions.add("Mix all of the dry spices together.");
        directions.add("Spray a baking pan with oil and place the chicken breasts on the pan. ");
        directions.add("Sprinkle the spice mixture over the chicken and rub with your hands." +
                "Repeat on the other side.");
        directions.add("Bake in the oven for ten minutes and flip and bake for ten more.");

        return null;
        /*return new Recipe("Everyday Baked Chicken", ingredients, directions,
                "http://78recipes.com/wp-content/uploads/2017/03/Everyday-Baked-Chicken.jpg");*/
    }
}
