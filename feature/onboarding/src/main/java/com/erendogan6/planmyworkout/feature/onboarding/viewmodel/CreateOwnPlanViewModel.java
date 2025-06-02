package com.erendogan6.planmyworkout.feature.onboarding.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.feature.onboarding.model.Exercise;
import com.erendogan6.planmyworkout.feature.onboarding.model.ExerciseTemplate;
import com.erendogan6.planmyworkout.feature.onboarding.model.PlanCreationState;
import com.erendogan6.planmyworkout.feature.onboarding.usecase.CreateOwnPlanUseCase;
import com.erendogan6.planmyworkout.feature.onboarding.usecase.GetExerciseTemplatesUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CreateOwnPlanViewModel extends ViewModel {

    private final CreateOwnPlanUseCase createOwnPlanUseCase;
    private final GetExerciseTemplatesUseCase getExerciseTemplatesUseCase;

    private final MutableLiveData<PlanCreationState> planState = new MutableLiveData<>(new PlanCreationState());
    private final MutableLiveData<List<ExerciseTemplate>> exerciseTemplates = new MutableLiveData<>();
    private final MutableLiveData<List<ExerciseTemplate>> filteredTemplates = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isSaving = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> planSaved = new MutableLiveData<>();

    @Inject
    public CreateOwnPlanViewModel(CreateOwnPlanUseCase createOwnPlanUseCase,
                                  GetExerciseTemplatesUseCase getExerciseTemplatesUseCase) {
        this.createOwnPlanUseCase = createOwnPlanUseCase;
        this.getExerciseTemplatesUseCase = getExerciseTemplatesUseCase;

        loadExerciseTemplates();
    }

    // LiveData Getters
    public LiveData<PlanCreationState> getPlanState() { return planState; }
    public LiveData<List<ExerciseTemplate>> getExerciseTemplates() { return exerciseTemplates; }
    public LiveData<List<ExerciseTemplate>> getFilteredTemplates() { return filteredTemplates; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<Boolean> getIsSaving() { return isSaving; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Boolean> getPlanSaved() { return planSaved; }

    // Plan Info Methods
    public void updatePlanName(String name) {
        PlanCreationState currentState = planState.getValue();
        if (currentState != null) {
            currentState.setPlanName(name);
            planState.setValue(currentState);
        }
    }

    public void updatePlanDescription(String description) {
        PlanCreationState currentState = planState.getValue();
        if (currentState != null) {
            currentState.setPlanDescription(description);
            planState.setValue(currentState);
        }
    }

    public void updateDifficulty(String difficulty) {
        PlanCreationState currentState = planState.getValue();
        if (currentState != null) {
            currentState.setDifficulty(difficulty);
            planState.setValue(currentState);
        }
    }

    public void updateDaysPerWeek(int days) {
        PlanCreationState currentState = planState.getValue();
        if (currentState != null) {
            currentState.setDaysPerWeek(days);
            planState.setValue(currentState);
        }
    }

    public void updateDurationWeeks(int weeks) {
        PlanCreationState currentState = planState.getValue();
        if (currentState != null) {
            currentState.setDurationWeeks(weeks);
            planState.setValue(currentState);
        }
    }

    // Exercise Management Methods
    public void addExerciseFromTemplate(ExerciseTemplate template) {
        PlanCreationState currentState = planState.getValue();
        if (currentState != null) {
            String exerciseId = "ex_" + currentState.getExercises().size();
            Exercise exercise = template.toExercise(exerciseId);
            currentState.addExercise(exercise);
            planState.setValue(currentState);
        }
    }

    public void removeExercise(int position) {
        PlanCreationState currentState = planState.getValue();
        if (currentState != null) {
            currentState.removeExercise(position);
            planState.setValue(currentState);
        }
    }

    public void updateExercise(int position, Exercise updatedExercise) {
        PlanCreationState currentState = planState.getValue();
        if (currentState != null && position >= 0 && position < currentState.getExercises().size()) {
            currentState.getExercises().set(position, updatedExercise);
            planState.setValue(currentState);
        }
    }

    // Exercise Template Methods
    public void loadExerciseTemplates() {
        isLoading.setValue(true);

        getExerciseTemplatesUseCase.execute()
                .addOnSuccessListener(templates -> {
                    isLoading.setValue(false);
                    exerciseTemplates.setValue(templates);
                    filteredTemplates.setValue(templates);
                })
                .addOnFailureListener(exception -> {
                    isLoading.setValue(false);
                    errorMessage.setValue("Failed to load exercises: " + exception.getMessage());
                });
    }

    public void filterExercisesByMuscleGroup(String muscleGroup) {
        if (muscleGroup == null || muscleGroup.equals("All")) {
            filteredTemplates.setValue(exerciseTemplates.getValue());
            return;
        }

        getExerciseTemplatesUseCase.executeByMuscleGroup(muscleGroup)
                .addOnSuccessListener(templates -> filteredTemplates.setValue(templates))
                .addOnFailureListener(exception ->
                        errorMessage.setValue("Failed to filter exercises: " + exception.getMessage()));
    }

    public void searchExercises(String query) {
        if (query == null || query.trim().isEmpty()) {
            filteredTemplates.setValue(exerciseTemplates.getValue());
            return;
        }

        getExerciseTemplatesUseCase.executeSearch(query)
                .addOnSuccessListener(templates -> filteredTemplates.setValue(templates))
                .addOnFailureListener(exception ->
                        errorMessage.setValue("Failed to search exercises: " + exception.getMessage()));
    }

    // Save Plan
    public void savePlan() {
        PlanCreationState currentState = planState.getValue();
        if (currentState == null) {
            errorMessage.setValue("No plan to save");
            return;
        }

        if (!currentState.isValid()) {
            errorMessage.setValue("Please fill in all required fields and add at least one exercise");
            return;
        }

        isSaving.setValue(true);

        createOwnPlanUseCase.execute(currentState)
                .addOnSuccessListener(aVoid -> {
                    isSaving.setValue(false);
                    planSaved.setValue(true);
                })
                .addOnFailureListener(exception -> {
                    isSaving.setValue(false);
                    errorMessage.setValue("Failed to save plan: " + exception.getMessage());
                });
    }

    public void clearErrorMessage() {
        errorMessage.setValue(null);
    }
}