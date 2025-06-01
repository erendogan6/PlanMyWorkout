package com.erendogan6.planmyworkout.feature.progress.repository;

import com.erendogan6.planmyworkout.feature.progress.model.ExerciseDetail;
import com.erendogan6.planmyworkout.feature.progress.model.WorkoutSummary;
import com.google.android.gms.tasks.Task;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for progress-related operations.
 */
public interface ProgressRepository {

    /**
     * Gets workout summary statistics for a specific date.
     */
    Task<WorkoutSummary> getWorkoutSummaryForDate(LocalDate date);

    /**
     * Gets detailed exercise information for a specific date.
     */
    Task<List<ExerciseDetail>> getExerciseDetailsForDate(LocalDate date);

    /**
     * Gets workout data for a date range.
     */
    Task<List<WorkoutSummary>> getWorkoutDataForDateRange(LocalDate startDate, LocalDate endDate);
}