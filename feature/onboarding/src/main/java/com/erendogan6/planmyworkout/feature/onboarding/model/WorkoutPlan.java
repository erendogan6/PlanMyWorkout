package com.erendogan6.planmyworkout.feature.onboarding.model;

import java.io.Serializable;
import java.util.List;

/**
 * Model class representing a workout plan.
 */
public class WorkoutPlan implements Serializable {
    private String id;
    private String name;
    private String description;
    private String difficulty;
    private int daysPerWeek; // This will be mapped from 'days' in Firestore
    private int durationWeeks;
    private List<String> weeklySchedule; // New field for the days of the week
    private List<Exercise> exercises;

    public WorkoutPlan() {
        // Required empty constructor for Firestore
    }

    public WorkoutPlan(String id, String name, String description, String difficulty,
                      int daysPerWeek, int durationWeeks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.daysPerWeek = daysPerWeek;
        this.durationWeeks = durationWeeks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getDaysPerWeek() {
        return daysPerWeek;
    }

    public int getDurationWeeks() {
        return durationWeeks;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public List<String> getWeeklySchedule() {
        return weeklySchedule;
    }

    public void setWeeklySchedule(List<String> weeklySchedule) {
        this.weeklySchedule = weeklySchedule;
    }
}
