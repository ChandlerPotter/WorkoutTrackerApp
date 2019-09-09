package com.example.workouttracker;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class LiftSQLiteHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "liftlist.db";
    public static final int DB_VERSION = 1;

    public static final String WORKOUT_TABLE = "workout_1";
    public static final String WORKOUT_1_ID = "_id";
    public static final String WORKOUT_NAME = "workout_name";


    public static final String LIFT_TABLE = "lift_1";
    public static final String LIFT_1_ID = "_id";
    public static final String LIFT_WORKOUT_1_ID = "workout_1_list_id";
    public static final String LIFT_NAME = "lift_name";
    public static final String LIFT_WEIGHT = "lift_weight";
    public static final String LIFT_SETS = "lift_sets";
    public static final String LIFT_REPS = "lift_reps";

    private Context context = null;

    public LiftSQLiteHelper(Context c) {
        super(c, DB_NAME, null, DB_VERSION);
        this.context = c;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + WORKOUT_TABLE
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + WORKOUT_NAME + " TEXT"
                + ")" );

        db.execSQL("CREATE TABLE " + LIFT_TABLE
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + LIFT_WORKOUT_1_ID + " INTEGER,"
                + LIFT_NAME       + " TEXT,"
                + LIFT_WEIGHT     + " TEXT,"
                + LIFT_SETS       + " TEXT,"
                + LIFT_REPS       + " TEXT,"
                + "FOREIGN KEY(\"workout_1_list_id\") REFERENCES WORKOUT_TABLE(WORKOUT_1_ID)"
                + ")" );

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }


}

