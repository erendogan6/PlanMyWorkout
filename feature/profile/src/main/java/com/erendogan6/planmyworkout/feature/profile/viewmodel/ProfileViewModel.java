package com.erendogan6.planmyworkout.feature.profile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.core.model.WorkoutPlan;
import com.erendogan6.planmyworkout.feature.profile.model.UserProfile;
import com.erendogan6.planmyworkout.feature.profile.model.WorkoutStats;
import com.erendogan6.planmyworkout.feature.profile.usecase.GetUserProfileUseCase;
import com.erendogan6.planmyworkout.feature.profile.usecase.LogoutUseCase;
import com.erendogan6.planmyworkout.feature.profile.repository.ProfileRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the profile screen.
 */
@HiltViewModel
public class ProfileViewModel extends ViewModel {

    private final GetUserProfileUseCase getUserProfileUseCase;
    private final LogoutUseCase logoutUseCase;
    private final ProfileRepository profileRepository;

    private final MutableLiveData<UserProfile> userProfile = new MutableLiveData<>();
    private final MutableLiveData<WorkoutPlan> currentPlan = new MutableLiveData<>();
    private final MutableLiveData<WorkoutStats> workoutStats = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> logoutSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    @Inject
    public ProfileViewModel(GetUserProfileUseCase getUserProfileUseCase,
                            LogoutUseCase logoutUseCase,
                            ProfileRepository profileRepository) {
        this.getUserProfileUseCase = getUserProfileUseCase;
        this.logoutUseCase = logoutUseCase;
        this.profileRepository = profileRepository;
    }

    /**
     * Loads the user profile data.
     */
    public void loadUserProfile() {
        isLoading.setValue(true);

        getUserProfileUseCase.execute()
                .addOnSuccessListener(profile -> {
                    userProfile.setValue(profile);
                    loadCurrentPlan();
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    errorMessage.setValue("Failed to load profile: " + e.getMessage());
                });
    }

    /**
     * Loads the current workout plan.
     */
    private void loadCurrentPlan() {
        profileRepository.getCurrentWorkoutPlan()
                .addOnSuccessListener(plan -> {
                    currentPlan.setValue(plan);
                    isLoading.setValue(false);
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    errorMessage.setValue("Failed to load current plan: " + e.getMessage());
                });
    }

    /**
     * Loads workout statistics.
     */
    public void loadWorkoutStats() {
        profileRepository.getWorkoutStats()
                .addOnSuccessListener(stats -> workoutStats.setValue(stats))
                .addOnFailureListener(e ->
                        errorMessage.setValue("Failed to load workout stats: " + e.getMessage()));
    }

    /**
     * Logs out the current user.
     */
    public void logout() {
        isLoading.setValue(true);

        logoutUseCase.execute()
                .addOnSuccessListener(unused -> {
                    isLoading.setValue(false);
                    logoutSuccess.setValue(true);
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    errorMessage.setValue("Failed to logout: " + e.getMessage());
                });
    }

    // LiveData getters
    public LiveData<UserProfile> getUserProfile() {
        return userProfile;
    }

    public LiveData<WorkoutPlan> getCurrentPlan() {
        return currentPlan;
    }

    public LiveData<WorkoutStats> getWorkoutStats() {
        return workoutStats;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getLogoutSuccess() {
        return logoutSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}