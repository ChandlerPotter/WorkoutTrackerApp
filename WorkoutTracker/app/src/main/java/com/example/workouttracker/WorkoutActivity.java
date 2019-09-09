package com.example.workouttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class WorkoutActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private TextView workoutName;
    private TextView liftName;
    private TextView repsName;
    private TextView setsName;
    private TextView weightName;
    private EditText liftInput;
    private EditText repsInput;
    private EditText setsInput;
    private EditText weightInput;
    private Button   addLift;
    private ListView liftListView;
    private ListView listview;


    private ArrayList<HashMap<String, String>> data = new ArrayList<>();

    private final String NAME   = "lift_name";
    private final String REPS   = "lift_reps";
    private final String SETS   = "lift_sets";
    private final String WEIGHT = "lift_weight";

    private final String LIFT_LIST_KEY = "liftList";

    SimpleCursorAdapter adapter = null;
    SimpleCursorAdapter adapter_2 = null;
    Cursor cursor = null;
    Cursor cursor_2 = null;
    LiftSQLiteHelper helper = null;
    SQLiteDatabase db = null;
    ContentValues cv = null;
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
        setContentView(R.layout.activity_workout);

        listview     = (ListView) findViewById(R.id.workout_listView);
        workoutName  = (TextView) this.findViewById(R.id.WorkoutNameTextView);
        liftName     = (TextView) this.findViewById(R.id.liftNameTextView);
        repsName     = (TextView) this.findViewById(R.id.RepsTextView);
        setsName     = (TextView) this.findViewById(R.id.SetsTextView);
        weightName   = (TextView) this.findViewById(R.id.weightTextView);
        liftInput    = (EditText) this.findViewById(R.id.liftNameInput);
        repsInput    = (EditText) this.findViewById(R.id.repsInput);
        setsInput    = (EditText) this.findViewById(R.id.setsInput);
        weightInput  = (EditText) this.findViewById(R.id.weightInput);
        addLift      = (Button)   this.findViewById(R.id.AddliftButton);
        liftListView = (ListView) this.findViewById(R.id.lift_list_view);


        CharSequence workoutseq = this.getIntent().getCharSequenceExtra("WORKOUT_NAME");
        final CharSequence workout_idseq = this.getIntent().getCharSequenceExtra("WORKOUT_ID");
        workoutName.setText(workoutseq);

        if (savedInstanceState != null){
            ArrayList<HashMap<String,String>> savedList = (ArrayList<HashMap<String,String>>)savedInstanceState.getSerializable(LIFT_LIST_KEY);
            data = savedList;
        }

        helper = new LiftSQLiteHelper(this);
        db     = helper.getWritableDatabase();
        cv     = new ContentValues();

        String query = "SELECT * FROM lift_1 WHERE workout_1_list_id = ?";
        cursor = db.rawQuery(query, new String[]{workout_idseq.toString()});

        adapter = new SimpleCursorAdapter(
                this,
                R.layout.lift_layout,
                cursor,
                new String[]{NAME, WEIGHT, SETS, REPS},
                new int[]{R.id.LiftListTextView,
                        R.id.WeightListTextView,
                        R.id.SetsListTextView,
                        R.id.RepsListTextView
                },
                0);
        //use cursor adapter...add text to the database table directly and then update view based on thatn
        liftListView.setAdapter(adapter);

        liftListView.setOnItemClickListener(this);

        addLift.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Lift lift = new Lift();
                lift.setLiftName(liftInput.getText().toString());
                lift.setWeight(weightInput.getText().toString());
                lift.setSets(setsInput.getText().toString());
                lift.setReps(repsInput.getText().toString());

                cv.put("workout_1_list_id", workout_idseq.toString());
                cv.put(helper.LIFT_NAME, lift.getLiftName());
                cv.put(helper.LIFT_WEIGHT, lift.getWeight() + " lbs.");
                cv.put(helper.LIFT_SETS, lift.getSets());
                cv.put(helper.LIFT_REPS, lift.getReps());

                db.insert(helper.LIFT_TABLE, null, cv);


                String query = "SELECT * FROM lift_1 WHERE workout_1_list_id = ?";
                Cursor new_cursor = db.rawQuery(query, new String[]{workout_idseq.toString()});
                adapter.swapCursor(new_cursor);



                liftInput.setText("");
                repsInput.setText("");
                setsInput.setText("");
                weightInput.setText("");

                }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(this, "row " + i + " clicked", Toast.LENGTH_SHORT).show();
        //db.delete("lift", helper.LIFT_NAME + "=" + )
    }


    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(LIFT_LIST_KEY, data);

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
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

}
