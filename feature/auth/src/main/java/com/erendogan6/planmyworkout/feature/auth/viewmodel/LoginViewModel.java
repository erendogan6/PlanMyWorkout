package com.erendogan6.planmyworkout.feature.auth.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.core.model.Result;
import com.erendogan6.planmyworkout.feature.auth.model.AuthResponse;
import com.erendogan6.planmyworkout.feature.auth.model.AuthResult;
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
    private final MutableLiveData<Result<AuthResult>> loginResult = new MutableLiveData<>();

    @Inject
    public LoginViewModel(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    /**
     * Attempts to log in with the provided credentials.
     *
     * @param email    User's email
     * @param password User's password
     */
    public void login(String email, String password) {
        loginResult.setValue(Result.loading(null));

        loginUseCase.execute(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        AuthResponse<AuthResult> response = task.getResult();

                        if (response.isSuccess()) {
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

    /**
     * Gets the login result LiveData.
     *
     * @return LiveData with the login result
     */
    public LiveData<Result<AuthResult>> getLoginResult() {
        return loginResult;
    }
}