package com.erendogan6.planmyworkout.feature.onboarding.model;

import java.util.List;

public class AiWorkoutPlanResponse {
    private String name;
    private String description;
    private String difficulty;
    private int days;
    private int durationWeeks;
    private List<AiExercise> exercises;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public int getDays() { return days; }
    public void setDays(int days) { this.days = days; }

    public int getDurationWeeks() { return durationWeeks; }
    public void setDurationWeeks(int durationWeeks) { this.durationWeeks = durationWeeks; }

    public List<AiExercise> getExercises() { return exercises; }
    public void setExercises(List<AiExercise> exercises) { this.exercises = exercises; }

    public static class AiExercise {
        private String name;
        private String description;
        private String muscleGroup;

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getMuscleGroup() { return muscleGroup; }
        public void setMuscleGroup(String muscleGroup) { this.muscleGroup = muscleGroup; }
    }
}