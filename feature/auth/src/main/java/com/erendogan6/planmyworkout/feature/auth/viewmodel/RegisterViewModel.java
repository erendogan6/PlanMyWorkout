package com.erendogan6.planmyworkout.feature.auth.viewmodel;

import android.text.TextUtils;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.core.model.Result;
import com.erendogan6.planmyworkout.feature.auth.model.AuthResult;
import com.erendogan6.planmyworkout.feature.auth.usecase.RegisterUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the register screen.
 * Handles UI-related logic and communication between the UI and use case.
 */
@HiltViewModel
public class RegisterViewModel extends ViewModel {

    private final RegisterUseCase registerUseCase;
    private final MutableLiveData<Result<AuthResult>> registerResult = new MutableLiveData<>();
    private final MutableLiveData<String> validationError = new MutableLiveData<>();

    @Inject
    public RegisterViewModel(RegisterUseCase registerUseCase) {
        this.registerUseCase = registerUseCase;
    }

    /**
     * Validates the input fields and registers a new user if validation passes.
     *
     * @param fullName The user's full name
     * @param email    The user's email address
     * @param password The user's password
     */
    public void register(String fullName, String email, String password) {
        // Clear previous validation errors
        validationError.setValue(null);
        
        // Validate inputs
        if (!validateInputs(fullName, email, password)) {
            return;
        }

        // Show loading state
        registerResult.setValue(Result.loading(null));

        // Register the user
        registerUseCase.execute(fullName, email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        if (task.getResult().isSuccess()) {
                            registerResult.setValue(Result.success(task.getResult().getData()));
                        } else {
                            registerResult.setValue(Result.error(task.getResult().getErrorMessage(), null));
                        }
                    } else {
                        Exception exception = task.getException();
                        String errorMessage = exception != null ? exception.getMessage() : "Registration failed";
                        registerResult.setValue(Result.error(errorMessage, null));
                    }
                });
    }

    /**
     * Validates the input fields.
     *
     * @param fullName The user's full name
     * @param email    The user's email address
     * @param password The user's password
     * @return True if all inputs are valid, false otherwise
     */
    private boolean validateInputs(String fullName, String email, String password) {
        // Check if fields are empty
        if (TextUtils.isEmpty(fullName)) {
            validationError.setValue("Please enter your full name");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            validationError.setValue("Please enter your email address");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            validationError.setValue("Please enter a password");
            return false;
        }

        // Validate email format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            validationError.setValue("Please enter a valid email address");
            return false;
        }

        // Validate password length
        if (password.length() < 6) {
            validationError.setValue("Password must be at least 6 characters long");
            return false;
        }

        return true;
    }

    /**
     * Gets the registration result LiveData.
     *
     * @return LiveData with the registration result
     */
    public LiveData<Result<AuthResult>> getRegisterResult() {
        return registerResult;
    }

    /**
     * Gets the validation error LiveData.
     *
     * @return LiveData with the validation error message
     */
    public LiveData<String> getValidationError() {
        return validationError;
    }
}
