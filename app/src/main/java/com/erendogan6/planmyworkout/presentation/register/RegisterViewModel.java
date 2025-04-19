package com.erendogan6.planmyworkout.presentation.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.domain.model.Result;
import com.erendogan6.planmyworkout.domain.repository.AuthRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the Register screen.
 * This class is part of the presentation layer and handles the UI logic for registration operations.
 */
@HiltViewModel
public class RegisterViewModel extends ViewModel {
    
    private final AuthRepository authRepository;
    
    private final MutableLiveData<Result<Boolean>> _registerResult = new MutableLiveData<>();
    
    /**
     * Constructor for RegisterViewModel.
     *
     * @param authRepository Repository for authentication operations
     */
    @Inject
    public RegisterViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
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
     * @return LiveData of registration result
     */
    public LiveData<Result<Boolean>> register(String email, String password) {
        return authRepository.register(email, password);
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
