package com.pnp.store.online;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.pnp.store.online.Fragments.BabyFragment;
import com.pnp.store.online.Fragments.BeveragesFragment;
import com.pnp.store.online.Fragments.ElectronicOfficeFragment;
import com.pnp.store.online.Fragments.FoodCupboardFragment;
import com.pnp.store.online.Fragments.FrozenFoodFragment;
import com.pnp.store.online.Fragments.FreshFoodFragment;
import com.pnp.store.online.Fragments.CartFragment;
import com.pnp.store.online.Fragments.HealthBeautyFragment;
import com.pnp.store.online.Fragments.HomeFragment;
import com.pnp.store.online.Fragments.HomeOutdoorFragment;
import com.pnp.store.online.Fragments.HouseholdFragment;
import com.pnp.store.online.Fragments.MealsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String PREFS = "prefFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences preferences = getSharedPreferences(PREFS, 0);
        final String customer = preferences.getString("username", null);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUsername);
        username.setText("Welcome, " + customer);
        ImageView profilePic = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imgUser);
        profilePic.setImageResource(R.drawable.ic_profile_pic);


        HomeFragment homeFragment = new HomeFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content_main, homeFragment,
                homeFragment.getTag()).commit();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if(id == R.id.action_shoping_cart)
        {
            CartFragment cartFragment = new CartFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, cartFragment, cartFragment.getTag())
                    .addToBackStack("Cart").commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.Fresh_food) {
            FreshFoodFragment FreshFoodFragment = new FreshFoodFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, FreshFoodFragment,
                    FreshFoodFragment.getTag()).commit();
        } else if (id == R.id.meals) {
            MealsFragment MealsFragment = new MealsFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, MealsFragment,
                    MealsFragment.getTag()).addToBackStack("meals").commit();

        } else if (id == R.id.Frozen_food) {
            FrozenFoodFragment FrozenFoodFragment = new FrozenFoodFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, FrozenFoodFragment,
                    FrozenFoodFragment.getTag()).addToBackStack("Frozen food").commit();

       } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }  else if (id == R.id.Food_cupboard) {
            FoodCupboardFragment FoodCupboardFragment = new FoodCupboardFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, FoodCupboardFragment,
                    FoodCupboardFragment.getTag()).addToBackStack("Food cupboard").commit();

        }  else if (id == R.id.beverages) {
            BeveragesFragment BeveragesFragment = new BeveragesFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, BeveragesFragment,
                    BeveragesFragment.getTag()).addToBackStack("Beverages").commit();

        }  else if (id == R.id.baby) {
            BabyFragment BabyFragment = new BabyFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, BabyFragment,
                    BabyFragment.getTag()).addToBackStack("Baby").commit();

        }  else if (id == R.id.Health) {
            HealthBeautyFragment HealthBeautyFragment = new HealthBeautyFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, HealthBeautyFragment,
                    HealthBeautyFragment.getTag()).addToBackStack("Health and Beauty").commit();

        }   else if (id == R.id.office) {
            ElectronicOfficeFragment ElectronicOfficeFragment = new ElectronicOfficeFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, ElectronicOfficeFragment,
                    ElectronicOfficeFragment.getTag()).addToBackStack("Electronics and Office").commit();

        }  else if (id == R.id.household) {
            HouseholdFragment HouseholdFragment = new HouseholdFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, HouseholdFragment,
                    HouseholdFragment.getTag()).addToBackStack("Household").commit();

        }  else if (id == R.id.outdoor) {
            HomeOutdoorFragment HomeOutdoorFragment = new HomeOutdoorFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.content_main, HomeOutdoorFragment,
                    HomeOutdoorFragment.getTag()).addToBackStack("Home and Outdoor").commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
