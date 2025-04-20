package com.erendogan6.planmyworkout.domain.model;

import java.io.Serializable;

/**
 * Model class representing a workout plan.
 * This class is Serializable to allow passing it between activities.
 */
public class WorkoutPlan implements Serializable {
    private String id;
    private String name;
    private String description;
    private String difficulty;
    private int daysPerWeek;
    private int durationWeeks;

    public WorkoutPlan() {
        // Required empty constructor
    }

    public WorkoutPlan(String id, String name, String description, String difficulty, int daysPerWeek, int durationWeeks) {
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

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
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
}
