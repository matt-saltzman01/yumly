package com.example.matt.yumly20;

import android.app.Fragment;
import android.app.FragmentManager;
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


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        HomeScreenFragment.OnFragmentInteractionListener,
        MyFridgeFragment.OnFragmentInteractionListener,
        MyRecipesFragment.OnFragmentInteractionListener,
        CookBookFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        RecipeFragment.OnFragmentInteractionListener { //temporary until recipe lists are built


    private static HomeScreenFragment homeScreenFragment = new HomeScreenFragment();
    private static MyFridgeFragment myFridgeFragment = new MyFridgeFragment();
    private static MyRecipesFragment myRecipesFragment = new MyRecipesFragment();
    private static CookBookFragment cookBookFragment = new CookBookFragment();
    private static SettingsFragment settingsFragment = new SettingsFragment();

    private static Fragment currentFragment = homeScreenFragment;
    private CharSequence currentTitle = "Home";
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        } else if (id == R.id.cookbook_frag) {
            currentFragment = cookBookFragment;
            currentTitle = "Cookbook";
        } else if (id == R.id.settings_frag) {
            currentFragment = settingsFragment;
            currentTitle = "Settings";
        }

        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, currentFragment)
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

        boolean replace = false;

        if (id == R.id.action_search) {
            //nothing for now
        } else if (id == R.id.action_my_cart) {
            if (!currentFragment.equals(myFridgeFragment)) {
                currentFragment = myFridgeFragment;
                currentTitle = "My Fridge";
                replace = true;
            }
        }

        if (replace) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, currentFragment)
                    .commit();
            setTitle(currentTitle);
        }

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

    public void findRecipesButtonClick(View view) {
        currentFragment = myRecipesFragment;
        currentTitle = "Recipe Search";
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
                .commit();
        setTitle(currentTitle);
    }

    public void myRecipeClick(View view) {
        currentFragment = new RecipeFragment();
        currentTitle = "Recipe Details";
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, currentFragment)
                .commit();
        setTitle(currentTitle);
    }

    public void myWeekClick(View view) {
        currentFragment = new RecipeFragment();
        currentTitle = "Recipe Details";
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, currentFragment)
                .commit();
        setTitle(currentTitle);
    }
}
