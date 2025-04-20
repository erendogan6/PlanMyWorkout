package com.erendogan6.planmyworkout.presentation.auth.viewmodel;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.domain.model.Result;
import com.erendogan6.planmyworkout.domain.usecase.auth.RegisterParams;
import com.erendogan6.planmyworkout.domain.usecase.auth.RegisterUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the registration screen.
 */
@HiltViewModel
public class RegisterViewModel extends ViewModel {

    private final RegisterUseCase registerUseCase;

    private final MutableLiveData<Boolean> registrationResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    @Inject
    public RegisterViewModel(RegisterUseCase registerUseCase) {
        this.registerUseCase = registerUseCase;
    }

    /**
     * Attempt to register with the provided credentials.
     * @param email The user's email
     * @param password The user's password
     */
    public void register(String email, String password) {
        isLoading.setValue(true);

        // Create register parameters
        RegisterParams params = new RegisterParams(email, password);

        // Execute register use case
        registerUseCase.execute(params).observeForever(result -> {
            if (result != null) {
                // Handle the result
                if (result.getStatus() == Result.Status.SUCCESS) {
                    registrationResult.postValue(true);
                } else if (result.getStatus() == Result.Status.ERROR) {
                    registrationResult.postValue(false);
                }
                isLoading.postValue(false);
            }
        });
    }

    /**
     * Validate if the email is in a valid format.
     * @param email The email to validate
     * @return True if the email is valid, false otherwise
     */
    public boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Validate if the password meets the minimum requirements.
     * @param password The password to validate
     * @return True if the password is valid, false otherwise
     */
    public boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    /**
     * Check if the password and confirm password match.
     * @param password The password
     * @param confirmPassword The confirm password
     * @return True if the passwords match, false otherwise
     */
    public boolean doPasswordsMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    /**
     * Get the registration result LiveData.
     * @return LiveData containing the registration result
     */
    public LiveData<Boolean> getRegistrationResult() {
        return registrationResult;
    }

    /**
     * Get the loading state LiveData.
     * @return LiveData containing the loading state
     */
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
