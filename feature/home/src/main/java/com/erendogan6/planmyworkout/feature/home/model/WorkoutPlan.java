package com.erendogan6.planmyworkout.feature.home.model;

import java.util.List;

/**
 * Model class for a workout plan.
 */
public class WorkoutPlan {
    
    private String id;
    private String name;
    private String description;
    private String frequency;
    private String difficulty;
    private List<String> exerciseIds;
    
    // Required empty constructor for Firestore
    public WorkoutPlan() {
    }
    
    public WorkoutPlan(String id, String name, String description, String frequency, String difficulty, List<String> exerciseIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.difficulty = difficulty;
        this.exerciseIds = exerciseIds;
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
    
    public String getFrequency() {
        return frequency;
    }
    
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
    
    public String getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    public List<String> getExerciseIds() {
        return exerciseIds;
    }
    
    public void setExerciseIds(List<String> exerciseIds) {
        this.exerciseIds = exerciseIds;
    }
}
