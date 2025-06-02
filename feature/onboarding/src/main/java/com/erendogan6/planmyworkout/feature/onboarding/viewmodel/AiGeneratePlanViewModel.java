package com.erendogan6.planmyworkout.feature.onboarding.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.feature.onboarding.model.AiWorkoutPlanRequest;
import com.erendogan6.planmyworkout.feature.onboarding.model.AiWorkoutPlanResponse;
import com.erendogan6.planmyworkout.feature.onboarding.usecase.GenerateAiWorkoutPlanUseCase;
import com.erendogan6.planmyworkout.feature.onboarding.usecase.SaveAiWorkoutPlanUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AiGeneratePlanViewModel extends ViewModel {

    private final GenerateAiWorkoutPlanUseCase generateWorkoutPlanUseCase;
    private final SaveAiWorkoutPlanUseCase saveAiWorkoutPlanUseCase;

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<AiWorkoutPlanResponse> generatedPlan = new MutableLiveData<>();

    // New LiveData for save operation
    private final MutableLiveData<Boolean> isSaving = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> planSaved = new MutableLiveData<>();

    @Inject
    public AiGeneratePlanViewModel(GenerateAiWorkoutPlanUseCase generateWorkoutPlanUseCase,
                                   SaveAiWorkoutPlanUseCase saveAiWorkoutPlanUseCase) {
        this.generateWorkoutPlanUseCase = generateWorkoutPlanUseCase;
        this.saveAiWorkoutPlanUseCase = saveAiWorkoutPlanUseCase;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<AiWorkoutPlanResponse> getGeneratedPlan() {
        return generatedPlan;
    }

    public LiveData<Boolean> getIsSaving() {
        return isSaving;
    }

    public LiveData<Boolean> getPlanSaved() {
        return planSaved;
    }

    public void generateWorkoutPlan(String goal, String experience, int daysPerWeek,
                                    int sessionDuration, String equipment, String limitations,
                                    String focusAreas) {
        isLoading.setValue(true);
        errorMessage.setValue(null);

        AiWorkoutPlanRequest request = new AiWorkoutPlanRequest(
                goal, experience, daysPerWeek, sessionDuration,
                equipment, limitations, focusAreas
        );

        generateWorkoutPlanUseCase.execute(request)
                .addOnSuccessListener(response -> {
                    isLoading.setValue(false);
                    generatedPlan.setValue(response);
                })
                .addOnFailureListener(exception -> {
                    isLoading.setValue(false);
                    errorMessage.setValue(exception.getMessage());
                });
    }

    public void saveAiWorkoutPlan(AiWorkoutPlanResponse aiPlan) {
        isSaving.setValue(true);
        errorMessage.setValue(null);

        saveAiWorkoutPlanUseCase.execute(aiPlan)
                .addOnSuccessListener(aVoid -> {
                    isSaving.setValue(false);
                    planSaved.setValue(true);
                })
                .addOnFailureListener(exception -> {
                    isSaving.setValue(false);
                    errorMessage.setValue("Failed to save plan: " + exception.getMessage());
                });
    }
}