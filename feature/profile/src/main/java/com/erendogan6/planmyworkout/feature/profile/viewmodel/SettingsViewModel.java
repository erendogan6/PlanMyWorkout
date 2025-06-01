package com.erendogan6.planmyworkout.feature.profile.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.feature.profile.repository.ProfileRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the settings screen.
 */
@HiltViewModel
public class SettingsViewModel extends ViewModel {

    private final ProfileRepository profileRepository;

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<Boolean> deleteAccountSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    @Inject
    public SettingsViewModel(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    /**
     * Deletes the current user's account.
     */
    public void deleteAccount() {
        isLoading.setValue(true);

        profileRepository.deleteAccount()
                .addOnSuccessListener(unused -> {
                    isLoading.setValue(false);
                    deleteAccountSuccess.setValue(true);
                })
                .addOnFailureListener(e -> {
                    isLoading.setValue(false);
                    errorMessage.setValue("Failed to delete account: " + e.getMessage());
                });
    }

    // LiveData getters
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<Boolean> getDeleteAccountSuccess() {
        return deleteAccountSuccess;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
}