package com.erendogan6.planmyworkout.feature.onboarding.model;

import java.util.ArrayList;
import java.util.List;

public class PlanCreationState {
    private String planName;
    private String planDescription;
    private String difficulty;
    private int daysPerWeek;
    private int durationWeeks;
    private List<Exercise> exercises;

    public PlanCreationState() {
        this.exercises = new ArrayList<>();
        this.daysPerWeek = 3;
        this.durationWeeks = 4;
        this.difficulty = "Intermediate";
    }

    // Getters and Setters
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }

    public String getPlanDescription() { return planDescription; }
    public void setPlanDescription(String planDescription) { this.planDescription = planDescription; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public int getDaysPerWeek() { return daysPerWeek; }
    public void setDaysPerWeek(int daysPerWeek) { this.daysPerWeek = daysPerWeek; }

    public int getDurationWeeks() { return durationWeeks; }
    public void setDurationWeeks(int durationWeeks) { this.durationWeeks = durationWeeks; }

    public List<Exercise> getExercises() { return exercises; }
    public void setExercises(List<Exercise> exercises) { this.exercises = exercises; }

    public void addExercise(Exercise exercise) {
        this.exercises.add(exercise);
    }

    public void removeExercise(int position) {
        if (position >= 0 && position < exercises.size()) {
            exercises.remove(position);
        }
    }

    public boolean isValid() {
        return planName != null && !planName.trim().isEmpty() &&
                !exercises.isEmpty();
    }

    public WorkoutPlan toWorkoutPlan() {
        WorkoutPlan plan = new WorkoutPlan(
                generatePlanId(),
                planName,
                planDescription != null ? planDescription : "",
                difficulty,
                daysPerWeek,
                durationWeeks
        );
        plan.setExercises(new ArrayList<>(exercises));
        return plan;
    }

    private String generatePlanId() {
        return "custom_" + System.currentTimeMillis();
    }
}