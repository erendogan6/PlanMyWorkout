package com.erendogan6.planmyworkout.feature.workout.model;

import java.io.Serializable;

/**
 * Model class representing an exercise with progress information.
 */
public class ExerciseWithProgress implements Serializable {
    private String id;
    private String name;
    private String description;
    private String muscleGroup;
    private String imageUrl;
    private int sets;
    private int repsPerSet;
    private int restSeconds;
    private Double lastWeight;
    private Integer lastReps;

    public ExerciseWithProgress() {
        // Required empty constructor for Firestore
    }

    public ExerciseWithProgress(String id, String name, String description, String muscleGroup,
                               String imageUrl, int sets, int repsPerSet, int restSeconds,
                               Double lastWeight, Integer lastReps) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.muscleGroup = muscleGroup;
        this.imageUrl = imageUrl;
        this.sets = sets;
        this.repsPerSet = repsPerSet;
        this.restSeconds = restSeconds;
        this.lastWeight = lastWeight;
        this.lastReps = lastReps;
    }

    /**
     * Create an ExerciseWithProgress from an Exercise with no progress data.
     */
    public static ExerciseWithProgress fromExercise(Exercise exercise) {
        return new ExerciseWithProgress(
                exercise.getId(),
                exercise.getName(),
                exercise.getDescription(),
                exercise.getMuscleGroup(),
                exercise.getImageUrl(),
                exercise.getSets(),
                exercise.getRepsPerSet(),
                exercise.getRestSeconds(),
                null,
                null
        );
    }

    /**
     * Create an ExerciseWithProgress from an Exercise with progress data.
     */
    public static ExerciseWithProgress fromExerciseWithProgress(Exercise exercise, Double weight, Integer reps) {
        return new ExerciseWithProgress(
                exercise.getId(),
                exercise.getName(),
                exercise.getDescription(),
                exercise.getMuscleGroup(),
                exercise.getImageUrl(),
                exercise.getSets(),
                exercise.getRepsPerSet(),
                exercise.getRestSeconds(),
                weight,
                reps
        );
    }

    /**
     * Get a formatted string representing the last try.
     */
    public String getLastTryText() {
        if (lastWeight != null && lastReps != null) {
            return String.format("Last try: %.1f kg × %d reps", lastWeight, lastReps);
        } else {
            return "No previous attempts";
        }
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

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getRepsPerSet() {
        return repsPerSet;
    }

    public void setRepsPerSet(int repsPerSet) {
        this.repsPerSet = repsPerSet;
    }

    public int getRestSeconds() {
        return restSeconds;
    }

    public void setRestSeconds(int restSeconds) {
        this.restSeconds = restSeconds;
    }

    public Double getLastWeight() {
        return lastWeight;
    }

    public void setLastWeight(Double lastWeight) {
        this.lastWeight = lastWeight;
    }

    public Integer getLastReps() {
        return lastReps;
    }

    public void setLastReps(Integer lastReps) {
        this.lastReps = lastReps;
    }
}
