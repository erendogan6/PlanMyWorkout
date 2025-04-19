package com.erendogan6.planmyworkout.presentation.auth.viewmodel;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.domain.model.Result;
import com.erendogan6.planmyworkout.domain.usecase.auth.LoginParams;
import com.erendogan6.planmyworkout.domain.usecase.auth.LoginUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the login screen.
 */
@HiltViewModel
public class LoginViewModel extends ViewModel {

    private final LoginUseCase loginUseCase;

    private final MutableLiveData<Boolean> loginResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    @Inject
    public LoginViewModel(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    /**
     * Attempt to log in with the provided credentials.
     * @param email The user's email
     * @param password The user's password
     */
    public void login(String email, String password) {
        isLoading.setValue(true);

        // Create login parameters
        LoginParams params = new LoginParams(email, password);

        // Execute login use case
        loginUseCase.execute(params).observeForever(result -> {
            if (result != null) {
                // Handle the result
                if (result.getStatus() == Result.Status.SUCCESS) {
                    loginResult.postValue(true);
                } else if (result.getStatus() == Result.Status.ERROR) {
                    loginResult.postValue(false);
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
     * Get the login result LiveData.
     * @return LiveData containing the login result
     */
    public LiveData<Boolean> getLoginResult() {
        return loginResult;
    }

    /**
     * Get the loading state LiveData.
     * @return LiveData containing the loading state
     */
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
