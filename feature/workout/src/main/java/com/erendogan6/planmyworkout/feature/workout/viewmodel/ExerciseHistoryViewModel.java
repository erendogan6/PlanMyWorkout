package com.erendogan6.planmyworkout.feature.workout.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.feature.workout.model.ExerciseLog;
import com.erendogan6.planmyworkout.feature.workout.model.ExerciseWithProgress;
import com.erendogan6.planmyworkout.feature.workout.usecase.GetExerciseLogsUseCase;
import com.erendogan6.planmyworkout.feature.workout.usecase.GetExerciseUseCase;
import com.erendogan6.planmyworkout.feature.workout.usecase.GetExerciseImageUseCase;

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
    private final GetExerciseImageUseCase getExerciseImageUseCase;
    private final SavedStateHandle savedStateHandle;
    private final MutableLiveData<ExerciseWithProgress> exercise = new MutableLiveData<>();
    private final MutableLiveData<List<ExerciseLog>> logs = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> exerciseImageUrl = new MutableLiveData<>();

    @Inject
    public ExerciseHistoryViewModel(
            GetExerciseUseCase getExerciseUseCase,
            GetExerciseLogsUseCase getExerciseLogsUseCase,
            GetExerciseImageUseCase getExerciseImageUseCase,
            SavedStateHandle savedStateHandle) {
        this.getExerciseUseCase = getExerciseUseCase;
        this.getExerciseLogsUseCase = getExerciseLogsUseCase;
        this.getExerciseImageUseCase = getExerciseImageUseCase;
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

    public LiveData<String> getExerciseImageUrl() {
        return exerciseImageUrl;
    }

    public String getExerciseId() {
        return savedStateHandle.get("exerciseId");
    }

    public String getPlanId() {
        return savedStateHandle.get("planId");
    }

    /**
     * Load exercise data including details and logs.
     */
    public void loadExerciseData() {
        String exerciseId = getExerciseId();
        String planId = getPlanId();

        if (exerciseId != null && planId != null) {
            isLoading.setValue(true);

            // Load exercise details
            getExerciseUseCase.execute(planId, exerciseId)
                    .addOnSuccessListener(loadedExercise -> {
                        exercise.setValue(loadedExercise);

                        // Fetch exercise image after loading exercise details
                        if (loadedExercise != null) {
                            fetchExerciseImage(loadedExercise.getName());
                        }

                        // Load logs
                        loadLogs();
                    })
                    .addOnFailureListener(e -> {
                        errorMessage.setValue("Failed to load exercise: " + e.getMessage());
                        isLoading.setValue(false);
                    });
        }
    }

    /**
     * Load exercise logs.
     */
    private void loadLogs() {
        String exerciseId = getExerciseId();
        String planId = getPlanId();

        if (exerciseId != null && planId != null) {
            getExerciseLogsUseCase.execute(planId, exerciseId)
                    .addOnSuccessListener(loadedLogs -> {
                        logs.setValue(loadedLogs);
                        isLoading.setValue(false);
                    })
                    .addOnFailureListener(e -> {
                        errorMessage.setValue("Failed to load logs: " + e.getMessage());
                        isLoading.setValue(false);
                    });
        }
    }

    /**
     * Fetch exercise image from Pexels API.
     */
    public void fetchExerciseImage(String exerciseName) {
        getExerciseImageUseCase.execute(exerciseName)
                .addOnSuccessListener(imageUrl -> {
                    exerciseImageUrl.setValue(imageUrl);
                })
                .addOnFailureListener(exception -> {
                    exerciseImageUrl.setValue(null);
                });
    }

    /**
     * Refresh logs data.
     */
    public void refreshLogs() {
        loadLogs();
    }
}