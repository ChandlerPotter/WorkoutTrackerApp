package com.example.workouttracker;

import java.util.ArrayList;

public class Workout extends ArrayList<Lift> {
    private String name;

    public String getWorkoutName() {
        return this.name;
    }
    public void setWorkoutName(String w) {
        this.name = w;
    }
}
