package com.erendogan6.planmyworkout.feature.onboarding.model;

/**
 * Document model for Exercise that represents the structure stored in Firestore.
 * This class is used for automatic serialization/deserialization with Firestore.
 */
public class ExerciseDocument {
    private String name;
    private String description;
    private String muscleGroup;
    private Long sets;
    private Long reps;
    private Long restSeconds;
    private String unit;

    /**
     * Default constructor required for Firestore deserialization.
     */
    public ExerciseDocument() {
        // Required empty constructor for Firestore
    }

    /**
     * Creates an ExerciseDocument from an Exercise object.
     * This is used when saving exercises to Firestore.
     *
     * @param exercise The Exercise to convert
     * @return An ExerciseDocument ready for Firestore storage
     */
    public static ExerciseDocument fromExercise(Exercise exercise) {
        ExerciseDocument document = new ExerciseDocument();
        document.setName(exercise.getName());
        document.setDescription(exercise.getDescription());
        document.setMuscleGroup(exercise.getMuscleGroup());
        document.setSets((long) exercise.getSets());
        document.setReps((long) exercise.getRepsPerSet());
        document.setRestSeconds((long) exercise.getRestSeconds());
        document.setUnit(exercise.getUnit());
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

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public Long getSets() {
        return sets;
    }

    public void setSets(Long sets) {
        this.sets = sets;
    }

    public Long getReps() {
        return reps;
    }

    public void setReps(Long reps) {
        this.reps = reps;
    }

    public Long getRestSeconds() {
        return restSeconds;
    }

    public void setRestSeconds(Long restSeconds) {
        this.restSeconds = restSeconds;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
