package com.erendogan6.planmyworkout.feature.workout.model;

public class ExerciseLogItemWrapper extends ExerciseLogItem {
    private final ExerciseLog log;

    public ExerciseLogItemWrapper(ExerciseLog log) {
        this.log = log;
    }

    public ExerciseLog getLog() {
        return log;
    }

    @Override
    public int getType() {
        return TYPE_LOG;
    }
}
