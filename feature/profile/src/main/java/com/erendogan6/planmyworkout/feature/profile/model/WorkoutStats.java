package com.erendogan6.planmyworkout.feature.profile.model;

public class WorkoutStats {
    private int totalWorkouts;
    private int totalExercises;
    private int consecutiveDays;

    public WorkoutStats() {}

    public WorkoutStats(int totalWorkouts, int totalExercises, int consecutiveDays) {
        this.totalWorkouts = totalWorkouts;
        this.totalExercises = totalExercises;
        this.consecutiveDays = consecutiveDays;
    }

    // Getters and setters
    public int getTotalWorkouts() { return totalWorkouts; }
    public void setTotalWorkouts(int totalWorkouts) { this.totalWorkouts = totalWorkouts; }

    public int getTotalExercises() { return totalExercises; }
    public void setTotalExercises(int totalExercises) { this.totalExercises = totalExercises; }

    public int getConsecutiveDays() { return consecutiveDays; }
    public void setConsecutiveDays(int consecutiveDays) { this.consecutiveDays = consecutiveDays; }
}