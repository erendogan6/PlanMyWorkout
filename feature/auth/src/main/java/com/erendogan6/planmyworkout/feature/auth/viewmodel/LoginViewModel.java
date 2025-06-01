package com.erendogan6.planmyworkout.feature.auth.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.core.model.Result;
import com.erendogan6.planmyworkout.feature.auth.model.AuthResponse;
import com.erendogan6.planmyworkout.feature.auth.model.AuthResult;
import com.erendogan6.planmyworkout.feature.auth.repository.AuthRepository;
import com.erendogan6.planmyworkout.feature.auth.usecase.LoginUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the login screen.
 * Handles UI-related logic and communication between the UI and use case.
 */
@HiltViewModel
public class LoginViewModel extends ViewModel {

    private final LoginUseCase loginUseCase;
    private final AuthRepository authRepository;
    private final MutableLiveData<Result<AuthResult>> loginResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> onboardingCompleted = new MutableLiveData<>();
    private final MutableLiveData<Boolean> autoLoginChecked = new MutableLiveData<>();

    @Inject
    public LoginViewModel(LoginUseCase loginUseCase, AuthRepository authRepository) {
        this.loginUseCase = loginUseCase;
        this.authRepository = authRepository;
    }

    /**
     * Attempts to log in with the provided credentials.
     */
    public void login(String email, String password) {
        loginResult.setValue(Result.loading(null));

        loginUseCase.execute(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        AuthResponse<AuthResult> response = task.getResult();

                        if (response.isSuccess()) {
                            // Check if the user has completed onboarding
                            checkOnboardingStatus();
                            loginResult.setValue(Result.success(response.getData()));
                        } else {
                            loginResult.setValue(Result.error(response.getErrorMessage(), null));
                        }
                    } else {
                        Exception exception = task.getException();
                        String errorMessage = exception != null ? exception.getMessage() : "Login failed";
                        loginResult.setValue(Result.error(errorMessage, null));
                    }
                });
    }

    public boolean shouldAttemptAutoLogin() {
        return authRepository.isUserLoggedIn() && authRepository.getRememberMePreference();
    }

    /**
     * Attempts automatic login for remembered users.
     */
    public void attemptAutoLogin() {
        loginResult.setValue(Result.loading(null));

        loginUseCase.executeAutoLogin()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        AuthResponse<AuthResult> response = task.getResult();

                        if (response.isSuccess()) {
                            checkOnboardingStatus();
                            loginResult.setValue(Result.success(response.getData()));
                        } else {
                            loginResult.setValue(Result.error(null, null));
                        }
                    } else {
                        loginResult.setValue(Result.error(null, null));
                    }
                    autoLoginChecked.setValue(true);
                });
    }

    /**
     * Checks if auto-login should be attempted.
     */
    public void checkForAutoLogin() {
        if (authRepository.isUserLoggedIn() && authRepository.getRememberMePreference()) {
            attemptAutoLogin();
        } else {
            autoLoginChecked.setValue(true);
        }
    }

    /**
     * Gets the login result LiveData.
     */
    public LiveData<Result<AuthResult>> getLoginResult() {
        return loginResult;
    }

    /**
     * Gets the onboarding completion status LiveData.
     */
    public LiveData<Boolean> getOnboardingCompleted() {
        return onboardingCompleted;
    }

    /**
     * Gets the auto login checked status LiveData.
     */
    public LiveData<Boolean> getAutoLoginChecked() {
        return autoLoginChecked;
    }

    /**
     * Checks if the user has completed the onboarding process.
     */
    private void checkOnboardingStatus() {
        authRepository.hasCompletedOnboarding()
                .addOnSuccessListener(onboardingCompleted::setValue)
                .addOnFailureListener(e -> onboardingCompleted.setValue(false));
    }
}