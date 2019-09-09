package com.example.workouttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import java.util.ArrayList;
import java.util.HashMap;
import android.widget.AdapterView;
import android.widget.Toast;
import android.content.Intent;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import android.view.View;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView listview;
    private Button   Addbutton;
    private EditText GetValue;
    private ArrayList<HashMap<String, String>> data = new ArrayList<>();
    final String WORKOUT = "workout_name";

    private MainActivity activity;
    private Workout workout;

    private static final String STATE_LIST = "saved_list";
    SimpleCursorAdapter adapter = null;
    Cursor cursor = null;
    Cursor new_cursor = null;
    LiftSQLiteHelper helper = null;
    SQLiteDatabase db = null;
    ContentValues cv = null;
    private SharedPreferences prefs;
    String temp;
    String temp2;
    long ltemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        setContentView(R.layout.activity_main);


        listview  = (ListView) findViewById(R.id.workout_listView);
        Addbutton = (Button)   findViewById(R.id.addWorkout_button);
        GetValue  = (EditText) findViewById(R.id.editText1);

        if (savedInstanceState != null){
            ArrayList<HashMap<String,String>> savedList = (ArrayList<HashMap<String,String>>)savedInstanceState.getSerializable(STATE_LIST);
            data = savedList;
        }

        helper = new LiftSQLiteHelper(this);
        db     = helper.getWritableDatabase();
        cv     = new ContentValues();


        String query = "SELECT * FROM workout_1"; // No trailing ';'

        cursor = db.rawQuery(query, null);

        adapter = new SimpleCursorAdapter(
                this,
                R.layout.workout_layout,
                cursor,
                new String[]{"workout_name"},

                new int[]{
                        R.id.workoutTextView
                },
                0);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);


        Addbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                workout = new Workout();
                workout.setWorkoutName(GetValue.getText().toString());
                if (!workout.getWorkoutName().equals("")) {
                    HashMap<String, String> map = new HashMap<String, String>();

                    cv.put(helper.WORKOUT_NAME, GetValue.getText().toString());
                    db.insert(helper.WORKOUT_TABLE, null, cv);

                    map.put(WORKOUT, workout.getWorkoutName());

                    data.add(map);
                    adapter.notifyDataSetChanged();

                    String query_1 = "SELECT _id, WORKOUT_NAME FROM workout_1"; // No trailing ';'
                    new_cursor = db.rawQuery(query_1, null);
                    adapter.swapCursor(new_cursor);
                }

            }
        });



    }

    @Override
    public void onResume(){
        super.onResume();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean dark = prefs.getBoolean("check_box_preference_1", false);
        if (dark == true) {
            setTheme(R.style.ThemeOverlay_AppCompat_Dark);
        }
        else if (dark == false) {
            setTheme(R.style.ThemeOverlay_AppCompat);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, WorkoutActivity.class);
        intent.putExtra("WORKOUT_NAME",cursor.getString(1));
        intent.putExtra("WORKOUT_ID",cursor.getString(0));
        startActivity(intent);
    }


    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_LIST, data);

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.workout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.game_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;
            case R.id.game_about:
                Toast.makeText(this, "This app is to track your weight lifting progress to help you get stronger.", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
