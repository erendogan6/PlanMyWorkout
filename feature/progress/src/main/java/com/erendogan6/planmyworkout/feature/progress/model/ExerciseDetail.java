package com.erendogan6.planmyworkout.feature.progress.model;

import java.util.List;

/**
 * Model representing detailed exercise information for progress tracking.
 */
public class ExerciseDetail {
    private String exerciseName;
    private List<SetDetail> sets;

    public ExerciseDetail() {}

    public ExerciseDetail(String exerciseName, List<SetDetail> sets) {
        this.exerciseName = exerciseName;
        this.sets = sets;
    }

    public String getExerciseName() { return exerciseName; }
    public void setExerciseName(String exerciseName) { this.exerciseName = exerciseName; }

    public List<SetDetail> getSets() { return sets; }
    public void setSets(List<SetDetail> sets) { this.sets = sets; }

    public String getFormattedSetDetails() {
        if (sets == null || sets.isEmpty()) {
            return "No sets recorded";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sets.size(); i++) {
            SetDetail set = sets.get(i);
            if (i > 0) sb.append(", ");
            sb.append(set.getReps()).append(" reps: ").append(set.getWeight()).append(" kg");
        }
        return sb.toString();
    }
}