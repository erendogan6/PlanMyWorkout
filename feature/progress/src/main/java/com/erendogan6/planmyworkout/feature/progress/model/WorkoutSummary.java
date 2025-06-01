package com.erendogan6.planmyworkout.feature.progress.model;

/**
 * Model representing workout summary statistics for a specific date.
 */
public class WorkoutSummary {
    private int totalSets;
    private int totalReps;
    private int totalExercises;
    private long sessionTimeMinutes;

    public WorkoutSummary() {}

    public WorkoutSummary(int totalSets, int totalReps, int totalExercises, long sessionTimeMinutes) {
        this.totalSets = totalSets;
        this.totalReps = totalReps;
        this.totalExercises = totalExercises;
        this.sessionTimeMinutes = sessionTimeMinutes;
    }

    public int getTotalSets() { return totalSets; }
    public void setTotalSets(int totalSets) { this.totalSets = totalSets; }

    public int getTotalReps() { return totalReps; }
    public void setTotalReps(int totalReps) { this.totalReps = totalReps; }

    public int getTotalExercises() { return totalExercises; }
    public void setTotalExercises(int totalExercises) { this.totalExercises = totalExercises; }

    public long getSessionTimeMinutes() { return sessionTimeMinutes; }
    public void setSessionTimeMinutes(long sessionTimeMinutes) { this.sessionTimeMinutes = sessionTimeMinutes; }

    public String getFormattedSessionTime() {
        long hours = sessionTimeMinutes / 60;
        long minutes = sessionTimeMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }
}