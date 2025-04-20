package com.erendogan6.planmyworkout.feature.workout.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Model class representing a log entry for an exercise.
 */
public class ExerciseLog {
    private String id;
    private double weight;
    private int reps;
    private String notes;
    private Date timestamp;

    // Default constructor for Firebase
    public ExerciseLog() {
    }

    public ExerciseLog(double weight, int reps, String notes) {
        this.weight = weight;
        this.reps = reps;
        this.notes = notes;
        this.timestamp = new Date();
    }

    public ExerciseLog(String id, double weight, int reps, String notes, Date timestamp) {
        this.id = id;
        this.weight = weight;
        this.reps = reps;
        this.notes = notes;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Format the date for display in the UI
     * @return Formatted date string (e.g., "April 18, 2024")
     */
    public String getFormattedDate() {
        if (timestamp == null) {
            return "";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
        return dateFormat.format(timestamp);
    }
}
