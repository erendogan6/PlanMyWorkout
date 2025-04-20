package com.erendogan6.planmyworkout.feature.workout.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.feature.workout.model.ExerciseLog;
import com.erendogan6.planmyworkout.feature.workout.model.ExerciseWithProgress;
import com.erendogan6.planmyworkout.feature.workout.repository.WorkoutRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the ExerciseDetailFragment.
 */
@HiltViewModel
public class ExerciseDetailViewModel extends ViewModel {

    private final WorkoutRepository repository;
    private final MutableLiveData<ExerciseWithProgress> exercise = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    @Inject
    public ExerciseDetailViewModel(WorkoutRepository repository) {
        this.repository = repository;
    }

    public LiveData<ExerciseWithProgress> getExercise() {
        return exercise;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getSaveSuccess() {
        return saveSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Load exercise details by ID.
     */
    public void loadExerciseDetails(String exerciseId) {
        isLoading.setValue(true);

        // In a real app, this would load from repository
        // For now, we'll just create a mock exercise
        try {
            // Simulate network delay
            Thread.sleep(500);

            // Create mock exercise with progress
            ExerciseWithProgress mockExercise = createMockExercise(exerciseId);
            exercise.postValue(mockExercise);
            isLoading.postValue(false);
        } catch (InterruptedException e) {
            errorMessage.postValue("Failed to load exercise: " + e.getMessage());
            isLoading.postValue(false);
        }
    }

    /**
     * Save the exercise log.
     */
    public void saveExerciseLog(double weight, int reps, String notes) {
        isLoading.setValue(true);

        // Create a new exercise log
        ExerciseLog log = new ExerciseLog(weight, reps, notes);

        // In a real app, we would save this to repository
        // For now, we'll just simulate a successful save
        try {
            // Simulate network delay
            Thread.sleep(1000);

            // Simulate successful save
            saveSuccess.postValue(true);
            isLoading.postValue(false);
        } catch (InterruptedException e) {
            // Handle error
            errorMessage.postValue("Failed to save exercise log: " + e.getMessage());
            isLoading.postValue(false);
        }
    }

    /**
     * Create a mock exercise with progress.
     */
    private ExerciseWithProgress createMockExercise(String exerciseId) {
        // In a real app, this would come from repository
        switch (exerciseId) {
            case "ex1":
                return new ExerciseWithProgress("ex1", "Barbell Squat",
                        "A compound lower body exercise", "Legs", "",
                        3, 10, 90, 80.0, 8);
            case "ex2":
                return new ExerciseWithProgress("ex2", "Bench Press",
                        "A compound upper body exercise", "Chest", "",
                        3, 10, 90, 70.0, 8);
            case "ex3":
                return new ExerciseWithProgress("ex3", "Deadlift",
                        "A compound full body exercise", "Back", "",
                        3, 8, 120, 100.0, 6);
            default:
                return new ExerciseWithProgress(exerciseId, "Exercise " + exerciseId,
                        "Description for exercise " + exerciseId, "Unknown", "",
                        3, 10, 60, null, null);
        }
    }
}
