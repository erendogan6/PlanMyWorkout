package com.erendogan6.planmyworkout.feature.workout.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.feature.workout.model.ExerciseLog;
import com.erendogan6.planmyworkout.feature.workout.model.ExerciseWithProgress;
import com.erendogan6.planmyworkout.feature.workout.usecase.GetExerciseUseCase;
import com.erendogan6.planmyworkout.feature.workout.usecase.GetLatestExerciseLogUseCase;
import com.erendogan6.planmyworkout.feature.workout.usecase.SaveExerciseLogUseCase;
import com.erendogan6.planmyworkout.feature.workout.usecase.UpdateExerciseLogUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the ExerciseDetailFragment.
 */
@HiltViewModel
public class ExerciseDetailViewModel extends ViewModel {

    private final GetExerciseUseCase getExerciseUseCase;
    private final GetLatestExerciseLogUseCase getLatestExerciseLogUseCase;
    private final SaveExerciseLogUseCase saveExerciseLogUseCase;
    private final SavedStateHandle savedStateHandle;
    private final MutableLiveData<ExerciseWithProgress> exercise = new MutableLiveData<>();
    private final MutableLiveData<ExerciseLog> latestLog = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isSaving = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> saveSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    // Edit mode fields
    private boolean editMode = false;
    private String logId = "";

    private final UpdateExerciseLogUseCase updateExerciseLogUseCase;

    @Inject
    public ExerciseDetailViewModel(
            GetExerciseUseCase getExerciseUseCase,
            GetLatestExerciseLogUseCase getLatestExerciseLogUseCase,
            SaveExerciseLogUseCase saveExerciseLogUseCase,
            UpdateExerciseLogUseCase updateExerciseLogUseCase,
            SavedStateHandle savedStateHandle) {
        this.getExerciseUseCase = getExerciseUseCase;
        this.getLatestExerciseLogUseCase = getLatestExerciseLogUseCase;
        this.saveExerciseLogUseCase = saveExerciseLogUseCase;
        this.updateExerciseLogUseCase = updateExerciseLogUseCase;
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
     * Set the edit mode and log ID.
     */
    public void setEditMode(boolean editMode, String logId) {
        this.editMode = editMode;
        this.logId = logId;
    }

    /**
     * Check if the ViewModel is in edit mode.
     */
    public boolean isInEditMode() {
        return editMode;
    }

    /**
     * Load the exercise details from Firestore.
     */
    public void loadExerciseDetails() {
        String exerciseId = savedStateHandle.get("exerciseId");
        String planId = savedStateHandle.get("planId");

        if (exerciseId != null && planId != null) {
            isLoading.setValue(true);
            getExerciseUseCase.execute(planId, exerciseId)
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
            getLatestExerciseLogUseCase.execute(planId, exerciseId)
                    .addOnSuccessListener(log -> {
                        latestLog.setValue(log);
                        isLoading.setValue(false);
                    })
                    .addOnFailureListener(e -> isLoading.setValue(false));
        }
    }

    /**
     * Save a new exercise log to Firestore.
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
        saveExerciseLogUseCase.execute(planId, exerciseId, weight, reps, notes)
                .addOnSuccessListener(aVoid -> {
                    saveSuccess.setValue(true);
                    isSaving.setValue(false);
                    // Reload the latest log to show the updated repository
                    loadLatestLog();
                })
                .addOnFailureListener(e -> {
                    errorMessage.setValue("Failed to save log: " + e.getMessage());
                    isSaving.setValue(false);
                });
    }

    /**
     * Update an existing exercise log in Firestore.
     */
    public void updateExerciseLog(double weight, int reps, String notes) {
        String exerciseId = savedStateHandle.get("exerciseId");
        String planId = savedStateHandle.get("planId");

        if (exerciseId == null || planId == null) {
            errorMessage.setValue("Exercise ID or Plan ID not found");
            return;
        }

        if (!editMode || logId.isEmpty()) {
            errorMessage.setValue("Not in edit mode or missing log ID");
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

        // Update in Firestore
        updateExerciseLogUseCase.execute(planId, exerciseId, logId, weight, reps, notes)
                .addOnSuccessListener(aVoid -> {
                    saveSuccess.setValue(true);
                    isSaving.setValue(false);
                })
                .addOnFailureListener(e -> {
                    errorMessage.setValue("Failed to update log: " + e.getMessage());
                    isSaving.setValue(false);
                });
    }
}