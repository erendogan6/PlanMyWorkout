package com.erendogan6.planmyworkout.feature.workout.usecase;

import com.erendogan6.planmyworkout.feature.workout.repository.WorkoutRepository;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

public class GetExerciseImageUseCase {

    private final WorkoutRepository workoutRepository;

    @Inject
    public GetExerciseImageUseCase(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public Task<String> execute(String exerciseName) {
        return workoutRepository.getExerciseImage(exerciseName);
    }
}