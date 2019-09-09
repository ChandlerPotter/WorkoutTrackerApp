package com.example.workouttracker;

public class Lift {

    private String name;
    private String weight;
    private String sets;
    private String reps;

    public String getLiftName() {
        return this.name;
    }
    public void setLiftName(String n) {
        this.name = n;
    }

    public String getWeight() {
        return this.weight;
    }
    public void setWeight(String w) {
        this.weight = w;
    }

    public String getSets() {
        return this.sets;
    }
    public void setSets(String s) {
        this.sets = s;
    }

    public String getReps() {
        return this.reps;
    }
    public void setReps(String r) {
        this.reps = r;
    }
}
