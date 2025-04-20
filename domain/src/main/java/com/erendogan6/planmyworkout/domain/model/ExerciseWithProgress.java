package com.erendogan6.planmyworkout.domain.model;

import java.io.Serializable;

/**
 * Model class representing an exercise with the user's last attempt information.
 */
public class ExerciseWithProgress implements Serializable {
    private final String id;
    private final String name;
    private final String description;
    private final String muscleGroup;
    private final String imageUrl;
    private final int sets;
    private final int repsPerSet;
    private final int restSeconds;
    private final double lastWeight;
    private final int lastReps;
    private final boolean hasLastTry;

    public ExerciseWithProgress(String id, String name, String description, String muscleGroup, 
                               String imageUrl, int sets, int repsPerSet, int restSeconds,
                               double lastWeight, int lastReps, boolean hasLastTry) {
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
        this.hasLastTry = hasLastTry;
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
                0,
                0,
                false
        );
    }

    /**
     * Create an ExerciseWithProgress from an Exercise with progress data.
     */
    public static ExerciseWithProgress fromExerciseWithProgress(Exercise exercise, double lastWeight, int lastReps) {
        return new ExerciseWithProgress(
                exercise.getId(),
                exercise.getName(),
                exercise.getDescription(),
                exercise.getMuscleGroup(),
                exercise.getImageUrl(),
                exercise.getSets(),
                exercise.getRepsPerSet(),
                exercise.getRestSeconds(),
                lastWeight,
                lastReps,
                true
        );
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getSets() {
        return sets;
    }

    public int getRepsPerSet() {
        return repsPerSet;
    }

    public int getRestSeconds() {
        return restSeconds;
    }

    public double getLastWeight() {
        return lastWeight;
    }

    public int getLastReps() {
        return lastReps;
    }

    public boolean hasLastTry() {
        return hasLastTry;
    }

    /**
     * Get a formatted string for the last try information.
     */
    public String getLastTryText() {
        if (hasLastTry) {
            return "Last Try: " + lastWeight + " kg x " + lastReps;
        } else {
            return "No previous attempts";
        }
    }
}
