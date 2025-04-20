package com.erendogan6.planmyworkout.presentation.exercise.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.domain.model.ExerciseWithProgress;
import com.erendogan6.planmyworkout.domain.model.WorkoutPlan;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the exercise list screen.
 */
@HiltViewModel
public class ExerciseListViewModel extends ViewModel {

    private final MutableLiveData<WorkoutPlan> workoutPlan = new MutableLiveData<>();
    private final MutableLiveData<List<ExerciseWithProgress>> exercises = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    @Inject
    public ExerciseListViewModel() {
        // Required empty constructor for Hilt
    }

    /**
     * Set the workout plan.
     */
    public void setWorkoutPlan(WorkoutPlan plan) {
        workoutPlan.setValue(plan);
    }

    /**
     * Set the exercises for the workout plan.
     */
    public void setExercises(List<ExerciseWithProgress> exerciseList) {
        exercises.setValue(exerciseList);
    }

    /**
     * Get the workout plan LiveData.
     */
    public LiveData<WorkoutPlan> getWorkoutPlan() {
        return workoutPlan;
    }

    /**
     * Get the exercises LiveData.
     */
    public LiveData<List<ExerciseWithProgress>> getExercises() {
        return exercises;
    }

    /**
     * Get the loading state LiveData.
     */
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
