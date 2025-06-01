package com.erendogan6.planmyworkout.feature.progress.model;

/**
 * Model representing a single set within an exercise.
 */
public class SetDetail {
    private int reps;
    private double weight;
    private String notes;

    public SetDetail() {}

    public SetDetail(int reps, double weight, String notes) {
        this.reps = reps;
        this.weight = weight;
        this.notes = notes;
    }

    public int getReps() { return reps; }
    public void setReps(int reps) { this.reps = reps; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}