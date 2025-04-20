package com.erendogan6.planmyworkout.feature.onboarding.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Document model for WorkoutPlan that represents the structure stored in Firestore.
 * This class is used for automatic serialization/deserialization with Firestore.
 */
public class WorkoutPlanDocument {
    private String name;
    private String description;
    private String difficulty;
    private Long days;
    private Long durationWeeks;
    private List<String> weeklySchedule;
    private List<ExerciseDocument> exercises;

    /**
     * Default constructor required for Firestore deserialization.
     */
    public WorkoutPlanDocument() {
        // Required empty constructor for Firestore
    }

    /**
     * Creates a WorkoutPlanDocument from a WorkoutPlan object.
     * This is used when saving a WorkoutPlan to Firestore.
     *
     * @param plan The WorkoutPlan to convert
     * @return A WorkoutPlanDocument ready for Firestore storage
     */
    public static WorkoutPlanDocument fromWorkoutPlan(WorkoutPlan plan) {
        WorkoutPlanDocument document = new WorkoutPlanDocument();
        document.setName(plan.getName());
        document.setDescription(plan.getDescription());
        document.setDifficulty(plan.getDifficulty());
        document.setDays((long) plan.getDaysPerWeek());
        document.setDurationWeeks((long) plan.getDurationWeeks());
        document.setWeeklySchedule(plan.getWeeklySchedule());

        if (plan.getExercises() != null) {
            List<ExerciseDocument> exerciseDocs = new ArrayList<>();
            for (Exercise exercise : plan.getExercises()) {
                exerciseDocs.add(ExerciseDocument.fromExercise(exercise));
            }
            document.setExercises(exerciseDocs);
        }

        return document;
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

    public Long getDays() {
        return days;
    }

    public void setDays(Long days) {
        this.days = days;
    }

    public Long getDurationWeeks() {
        return durationWeeks;
    }

    public void setDurationWeeks(Long durationWeeks) {
        this.durationWeeks = durationWeeks;
    }

    public List<String> getWeeklySchedule() {
        return weeklySchedule;
    }

    public void setWeeklySchedule(List<String> weeklySchedule) {
        this.weeklySchedule = weeklySchedule;
    }

    public List<ExerciseDocument> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseDocument> exercises) {
        this.exercises = exercises;
    }
}
