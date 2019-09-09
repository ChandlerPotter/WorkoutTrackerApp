package com.example.workouttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences prefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get boolean from settings to set this dark theme
        //setTheme(R.style.ThemeOverlay_AppCompat_Dark);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean dark = prefs.getBoolean("check_box_preference_1", false);
        if (dark == true) {
            setTheme(R.style.ThemeOverlay_AppCompat_Dark);
        }
        else if (dark == false) {
            setTheme(R.style.ThemeOverlay_AppCompat);
        }
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();


    }

}