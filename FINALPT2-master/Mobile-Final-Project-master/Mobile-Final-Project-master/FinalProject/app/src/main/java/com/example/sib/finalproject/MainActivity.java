package com.example.sib.finalproject;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.sib.finalproject.Services.DirectionManager;
import com.example.sib.finalproject.Services.GPSManager;
import com.example.sib.finalproject.fragments.MainScreenFragment;
import com.example.sib.finalproject.fragments.ProfileFragment;
import com.example.sib.finalproject.fragments.TaskFragment;
import com.example.sib.finalproject.interfaces.MainScreenInteraction;
import com.example.sib.finalproject.interfaces.RetainedFragmentInteraction;

import java.util.ArrayList;

/**
 * Created by Rameen Barish on 4/29/2017.
 */

public class MainActivity extends AppCompatActivity implements MainScreenInteraction {

    private Fragment mainScreenFragment, taskFragment, profileFragment;
    private SharedPreferences app_preferences;
    private FragmentManager fragmentManager;
    private ArrayList<String> friends;

    //location stuff
    GPSManager gpsManager;
    DirectionManager directionManager;
    Compass compass;

    int requestCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        //location stuff
        gpsManager = new GPSManager(this);
        directionManager = new DirectionManager(this);
        compass = new Compass();
        //////////////////////////////////////////////
        fragmentManager = getSupportFragmentManager();

        app_preferences = PreferenceManager.
                getDefaultSharedPreferences(getApplicationContext());
        String username = app_preferences.getString("userId", "");
        String password = app_preferences.getString("password", "");
        String loggedin = app_preferences.getString("loggedin", "");

        if(!(loggedin.equals("yes"))){
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
            finish();
        }



        //is our retained framgent already alive?
        taskFragment = (TaskFragment) fragmentManager.findFragmentByTag(TaskFragment.TAG_TASK_FRAGMENT);

        //if it isn't we create a new instance
        if (taskFragment == null) {

            taskFragment = new TaskFragment();
            //adding it inside fragment manager
            fragmentManager.beginTransaction().add(taskFragment, TaskFragment.TAG_TASK_FRAGMENT).commit();
        }

        if (savedInstanceState == null) {

            mainScreenFragment = new MainScreenFragment();
            // Set HomeScreenFragment  to be the default fragment shown and saving the tag inside our retained fragment.
            ((RetainedFragmentInteraction)taskFragment).setActiveFragmentTag(MainScreenFragment.TAG_MAIN_FRAGMENT);

            //swapping the empty frame with HomeScreenFragment

            fragmentManager.beginTransaction().replace(R.id.frame, mainScreenFragment).commit();



        } else {

            //getting references to our fragments via fragment manager. Notie how we are using TAGs to do that.
            profileFragment = fragmentManager.findFragmentByTag(ProfileFragment.TAG_PROFILE_FRAGMENT);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
        directionManager.register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gpsManager.unregister();
        directionManager.unregister();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        gpsManager.register();
    }

    @Override
    public void changeFragment(String fragment_name) {


        // this will keep the fragment reference
        Fragment fragment;

        //this will allows us to know which fragment user has selected
        Class fragmentClass = null;

        //Fix the if statement conditions'
        if(fragment_name.equals(ProfileFragment.TAG_PROFILE_FRAGMENT)){
            fragmentClass = ProfileFragment.class;

            ((RetainedFragmentInteraction)taskFragment).setActiveFragmentTag(ProfileFragment.TAG_PROFILE_FRAGMENT);
        }


        try {
            if (fragmentClass != null) {


                fragment = (Fragment) fragmentClass.newInstance();

                if (fragment.getClass().equals(ProfileFragment.class)) {
                    profileFragment = fragment;
                }



                //instantiate a fragment transation
                FragmentTransaction fragTrans = fragmentManager.beginTransaction();

                // show the selected fragment.
                // use FragmentTransactions's replace
                fragTrans.replace(R.id.frame, fragment, fragment_name);
                // use addToBackStack(null);
                fragTrans.addToBackStack(null);
                // don't forget to commit();
                fragTrans.commit();



            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_options_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
            app_preferences.edit().clear().commit();
            //InitiateLoginActivity();
            finish();
        }
        else if (item.getItemId() == R.id.addFriend) {
            ((MainScreenFragment)mainScreenFragment).addFriend();
        }
        else if (item.getItemId() == R.id.removeFriend) {
            ((MainScreenFragment)mainScreenFragment).removeFriend();
        }
        return true;
    }

    public void updateGPSLocation(Location lastKnownLocation) {
        compass.setCurrentLocation(lastKnownLocation);
    }

    public void updateSensor(double angle) {
        compass.setAngle(angle);
        //updateUI((float) compass.getDrillfieldAngle(), compass.getDrillfieldDistance());
    }
}