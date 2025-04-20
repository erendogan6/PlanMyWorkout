package com.erendogan6.planmyworkout.feature.home.model;

import java.util.List;

/**
 * Model class for a workout plan.
 */
public class WorkoutPlan {

    private String id;
    private String name;
    private String description;
    private String difficulty;
    private int daysPerWeek;
    private int durationWeeks;
    private List<String> weeklySchedule;
    private List<String> exerciseNames;

    // Required empty constructor for Firestore
    public WorkoutPlan() {
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

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDaysPerWeek() {
        return daysPerWeek;
    }

    public void setDaysPerWeek(int daysPerWeek) {
        this.daysPerWeek = daysPerWeek;
    }

    public int getDurationWeeks() {
        return durationWeeks;
    }

    public void setDurationWeeks(int durationWeeks) {
        this.durationWeeks = durationWeeks;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getWeeklySchedule() {
        return weeklySchedule;
    }

    public void setWeeklySchedule(List<String> weeklySchedule) {
        this.weeklySchedule = weeklySchedule;
    }

    public List<String> getExerciseNames() {
        return exerciseNames;
    }

    public void setExerciseNames(List<String> exerciseNames) {
        this.exerciseNames = exerciseNames;
    }
}
