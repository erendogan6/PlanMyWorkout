package com.erendogan6.planmyworkout.feature.workout.model;

// ExerciseLogItem.java - Base class for adapter items
public abstract class ExerciseLogItem {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_LOG = 1;

    public abstract int getType();
}

