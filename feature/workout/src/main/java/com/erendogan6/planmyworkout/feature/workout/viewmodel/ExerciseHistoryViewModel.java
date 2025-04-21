package com.erendogan6.planmyworkout.feature.workout.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.feature.workout.model.ExerciseLog;
import com.erendogan6.planmyworkout.feature.workout.model.ExerciseWithProgress;
import com.erendogan6.planmyworkout.feature.workout.usecase.GetExerciseLogsUseCase;
import com.erendogan6.planmyworkout.feature.workout.usecase.GetExerciseUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the ExerciseHistoryFragment.
 */
@HiltViewModel
public class ExerciseHistoryViewModel extends ViewModel {

    private final GetExerciseUseCase getExerciseUseCase;
    private final GetExerciseLogsUseCase getExerciseLogsUseCase;
    private final SavedStateHandle savedStateHandle;
    private final MutableLiveData<ExerciseWithProgress> exercise = new MutableLiveData<>();
    private final MutableLiveData<List<ExerciseLog>> logs = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    @Inject
    public ExerciseHistoryViewModel(
            GetExerciseUseCase getExerciseUseCase,
            GetExerciseLogsUseCase getExerciseLogsUseCase,
            SavedStateHandle savedStateHandle) {
        this.getExerciseUseCase = getExerciseUseCase;
        this.getExerciseLogsUseCase = getExerciseLogsUseCase;
        this.savedStateHandle = savedStateHandle;
    }

    public LiveData<ExerciseWithProgress> getExercise() {
        return exercise;
    }

    public LiveData<List<ExerciseLog>> getLogs() {
        return logs;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Load the exercise details and logs from Firestore.
     */
    public void loadExerciseData() {
        String exerciseId = savedStateHandle.get("exerciseId");
        String planId = savedStateHandle.get("planId");

        if (exerciseId == null || planId == null) {
            errorMessage.setValue("Exercise ID or Plan ID not found");
            return;
        }

        isLoading.setValue(true);

        // Load exercise details
        getExerciseUseCase.execute(planId, exerciseId)
                .addOnSuccessListener(loadedExercise -> {
                    exercise.setValue(loadedExercise);
                    
                    // After loading exercise details, load logs
                    loadExerciseLogs(planId, exerciseId);
                })
                .addOnFailureListener(e -> {
                    errorMessage.setValue("Failed to load exercise: " + e.getMessage());
                    isLoading.setValue(false);
                });
    }

    /**
     * Load all exercise logs from Firestore.
     */
    private void loadExerciseLogs(String planId, String exerciseId) {
        getExerciseLogsUseCase.execute(planId, exerciseId)
                .addOnSuccessListener(exerciseLogs -> {
                    logs.setValue(exerciseLogs);
                    isLoading.setValue(false);
                })
                .addOnFailureListener(e -> {
                    errorMessage.setValue("Failed to load logs: " + e.getMessage());
                    isLoading.setValue(false);
                });
    }

    /**
     * Refresh the exercise logs.
     */
    public void refreshLogs() {
        String exerciseId = savedStateHandle.get("exerciseId");
        String planId = savedStateHandle.get("planId");

        if (exerciseId != null && planId != null) {
            isLoading.setValue(true);
            loadExerciseLogs(planId, exerciseId);
        }
    }

    /**
     * Get the exercise ID.
     */
    public String getExerciseId() {
        return savedStateHandle.get("exerciseId");
    }

    /**
     * Get the plan ID.
     */
    public String getPlanId() {
        return savedStateHandle.get("planId");
    }
}
