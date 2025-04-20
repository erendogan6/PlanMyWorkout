package com.erendogan6.planmyworkout.presentation.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.domain.model.Result;
import com.erendogan6.planmyworkout.domain.usecase.auth.RegisterParams;
import com.erendogan6.planmyworkout.domain.usecase.auth.RegisterUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the Register screen.
 * This class is part of the presentation layer and handles the UI logic for registration operations.
 */
@HiltViewModel
public class RegisterViewModel extends ViewModel {

    private final RegisterUseCase registerUseCase;

    private final MutableLiveData<Result<Boolean>> _registerResult = new MutableLiveData<>();

    /**
     * Constructor for RegisterViewModel.
     *
     * @param registerUseCase Use case for registration operations
     */
    @Inject
    public RegisterViewModel(RegisterUseCase registerUseCase) {
        this.registerUseCase = registerUseCase;
    }

    /**
     * Get the registration result as a LiveData.
     *
     * @return LiveData of registration result
     */
    public LiveData<Result<Boolean>> getRegisterResult() {
        return _registerResult;
    }

    /**
     * Register a new user with email and password.
     *
     * @param email    User's email
     * @param password User's password
     */
    public void register(String email, String password) {
        // Set loading state
        _registerResult.setValue(Result.loading(false));

        // Execute register use case and observe the result
        RegisterParams params = new RegisterParams(email, password);
        LiveData<Result<Boolean>> resultLiveData = registerUseCase.execute(params);
        resultLiveData.observeForever(result -> {
            if (result != null) {
                _registerResult.setValue(result);
            }
        });
    }

    /**
     * Validate email format.
     *
     * @param email Email to validate
     * @return true if email is valid, false otherwise
     */
    public boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        // Simple email validation
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Validate password format.
     *
     * @param password Password to validate
     * @return true if password is valid, false otherwise
     */
    public boolean isValidPassword(String password) {
        // Password should be at least 6 characters
        return password != null && password.length() >= 6;
    }

    /**
     * Check if passwords match.
     *
     * @param password        Password
     * @param confirmPassword Confirm password
     * @return true if passwords match, false otherwise
     */
    public boolean doPasswordsMatch(String password, String confirmPassword) {
        return password != null && password.equals(confirmPassword);
    }
}
