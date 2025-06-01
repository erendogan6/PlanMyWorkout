package com.erendogan6.planmyworkout.feature.workout.model;

public class ExerciseLogHeader extends ExerciseLogItem {
    private final String date;

    public ExerciseLogHeader(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int getType() {
        return TYPE_HEADER;
    }
}
