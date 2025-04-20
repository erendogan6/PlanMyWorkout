package com.erendogan6.planmyworkout.feature.workout.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
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
    private final SavedStateHandle savedStateHandle;
    private final MutableLiveData<ExerciseWithProgress> exercise = new MutableLiveData<>();
    private final MutableLiveData<ExerciseLog> latestLog = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isSaving = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    @Inject
    public ExerciseDetailViewModel(WorkoutRepository repository, SavedStateHandle savedStateHandle) {
        this.repository = repository;
        this.savedStateHandle = savedStateHandle;
    }

    public LiveData<ExerciseWithProgress> getExercise() {
        return exercise;
    }

    public LiveData<ExerciseLog> getLatestLog() {
        return latestLog;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getIsSaving() {
        return isSaving;
    }

    public LiveData<Boolean> getSaveSuccess() {
        return saveSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Load the exercise details from Firestore.
     */
    public void loadExerciseDetails() {
        String exerciseId = savedStateHandle.get("exerciseId");
        String planId = savedStateHandle.get("planId");

        if (exerciseId != null && planId != null) {
            isLoading.setValue(true);
            repository.getExercise(planId, exerciseId)
                    .addOnSuccessListener(loadedExercise -> {
                        exercise.setValue(loadedExercise);
                        isLoading.setValue(false);
                    })
                    .addOnFailureListener(e -> {
                        errorMessage.setValue("Failed to load exercise: " + e.getMessage());
                        isLoading.setValue(false);
                    });
        }
    }

    /**
     * Load the latest exercise log from Firestore.
     */
    public void loadLatestLog() {
        String exerciseId = savedStateHandle.get("exerciseId");
        String planId = savedStateHandle.get("planId");

        if (exerciseId != null && planId != null) {
            isLoading.setValue(true);
            repository.getLatestExerciseLog(planId, exerciseId)
                    .addOnSuccessListener(log -> {
                        latestLog.setValue(log);
                        isLoading.setValue(false);
                    })
                    .addOnFailureListener(e -> {
                        // It's okay if there's no log yet, just set loading to false
                        isLoading.setValue(false);
                    });
        }
    }

    /**
     * Save the exercise log to Firestore.
     */
    public void saveExerciseLog(double weight, int reps, String notes) {
        String exerciseId = savedStateHandle.get("exerciseId");
        String planId = savedStateHandle.get("planId");

        if (exerciseId == null || planId == null) {
            errorMessage.setValue("Exercise ID or Plan ID not found");
            return;
        }

        // Validate inputs
        if (weight <= 0) {
            errorMessage.setValue("Please enter a valid weight");
            return;
        }

        if (reps <= 0) {
            errorMessage.setValue("Please enter a valid number of reps");
            return;
        }

        // Show loading state
        isSaving.setValue(true);

        // Save to Firestore
        repository.saveExerciseLog(planId, exerciseId, weight, reps, notes)
                .addOnSuccessListener(aVoid -> {
                    saveSuccess.setValue(true);
                    isSaving.setValue(false);
                    // Reload the latest log to show the updated data
                    loadLatestLog();
                })
                .addOnFailureListener(e -> {
                    errorMessage.setValue("Failed to save log: " + e.getMessage());
                    isSaving.setValue(false);
                });
    }
}
