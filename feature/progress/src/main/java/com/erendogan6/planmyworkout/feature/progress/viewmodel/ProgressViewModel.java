package com.erendogan6.planmyworkout.feature.progress.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.feature.progress.model.ExerciseDetail;
import com.erendogan6.planmyworkout.feature.progress.model.WorkoutSummary;
import com.erendogan6.planmyworkout.feature.progress.repository.ProgressRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the progress screen.
 */
@HiltViewModel
public class ProgressViewModel extends ViewModel {

    private final ProgressRepository progressRepository;

    private final MutableLiveData<LocalDate> selectedDate = new MutableLiveData<>();
    private final MutableLiveData<List<LocalDate>> currentWeek = new MutableLiveData<>();
    private final MutableLiveData<WorkoutSummary> workoutSummary = new MutableLiveData<>();
    private final MutableLiveData<List<ExerciseDetail>> exerciseDetails = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    @Inject
    public ProgressViewModel(ProgressRepository progressRepository) {
        this.progressRepository = progressRepository;

        // Initialize with current date and week
        LocalDate today = LocalDate.now();
        selectedDate.setValue(today);
        updateCurrentWeek(today);
    }

    /**
     * Loads workout data for the specified date.
     */
    public void loadWorkoutDataForDate(LocalDate date) {
        isLoading.setValue(true);

        progressRepository.getWorkoutSummaryForDate(date)
                .addOnSuccessListener(summary -> {
                    workoutSummary.setValue(summary);

                    if (summary != null) {
                        // Load exercise details if summary exists
                        loadExerciseDetailsForDate(date);
                    } else {
                        exerciseDetails.setValue(new ArrayList<>());
                        isLoading.setValue(false);
                    }
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    errorMessage.setValue("Failed to load workout data: " + e.getMessage());
                });
    }

    /**
     * Loads detailed exercise information for the specified date.
     */
    private void loadExerciseDetailsForDate(LocalDate date) {
        progressRepository.getExerciseDetailsForDate(date)
                .addOnSuccessListener(details -> {
                    exerciseDetails.setValue(details);
                    isLoading.setValue(false);
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    errorMessage.setValue("Failed to load exercise details: " + e.getMessage());
                });
    }

    /**
     * Selects a new date and updates the view.
     */
    public void selectDate(LocalDate date) {
        selectedDate.setValue(date);
        updateCurrentWeek(date);
    }

    /**
     * Navigates to the previous week.
     */
    public void navigateToPreviousWeek() {
        LocalDate current = selectedDate.getValue();
        if (current != null) {
            LocalDate previousWeek = current.minusWeeks(1);
            selectDate(previousWeek);
        }
    }

    /**
     * Navigates to the next week.
     */
    public void navigateToNextWeek() {
        LocalDate current = selectedDate.getValue();
        if (current != null) {
            LocalDate nextWeek = current.plusWeeks(1);
            selectDate(nextWeek);
        }
    }

    /**
     * Updates the current week display based on the selected date.
     */
    private void updateCurrentWeek(LocalDate date) {
        LocalDate startOfWeek = date.minusDays(date.getDayOfWeek().getValue() - 1);
        List<LocalDate> weekDates = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            weekDates.add(startOfWeek.plusDays(i));
        }

        currentWeek.setValue(weekDates);
    }

    // LiveData getters
    public LiveData<LocalDate> getSelectedDate() {
        return selectedDate;
    }

    public LiveData<List<LocalDate>> getCurrentWeek() {
        return currentWeek;
    }

    public LiveData<WorkoutSummary> getWorkoutSummary() {
        return workoutSummary;
    }

    public LiveData<List<ExerciseDetail>> getExerciseDetails() {
        return exerciseDetails;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}