package com.erendogan6.planmyworkout.presentation.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.domain.model.Result;
import com.erendogan6.planmyworkout.domain.usecase.auth.LoginParams;
import com.erendogan6.planmyworkout.domain.usecase.auth.LoginUseCase;
import com.erendogan6.planmyworkout.domain.usecase.auth.ResetPasswordParams;
import com.erendogan6.planmyworkout.domain.usecase.auth.ResetPasswordUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the Login screen.
 * This class is part of the presentation layer and handles the UI logic for login operations.
 */
@HiltViewModel
public class LoginViewModel extends ViewModel {

    private final LoginUseCase loginUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;

    private final MutableLiveData<Result<Boolean>> _loginResult = new MutableLiveData<>();
    private final MutableLiveData<Result<Boolean>> _resetPasswordResult = new MutableLiveData<>();

    /**
     * Constructor for LoginViewModel.
     *
     * @param loginUseCase         Use case for login operation
     * @param resetPasswordUseCase Use case for password reset operation
     */
    @Inject
    public LoginViewModel(LoginUseCase loginUseCase, ResetPasswordUseCase resetPasswordUseCase) {
        this.loginUseCase = loginUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
    }

    /**
     * Get the login result as a LiveData.
     *
     * @return LiveData of login result
     */
    public LiveData<Result<Boolean>> getLoginResult() {
        return _loginResult;
    }

    /**
     * Get the password reset result as a LiveData.
     *
     * @return LiveData of password reset result
     */
    public LiveData<Result<Boolean>> getResetPasswordResult() {
        return _resetPasswordResult;
    }

    /**
     * Login with email and password.
     *
     * @param email    User's email
     * @param password User's password
     */
    public void login(String email, String password) {
        // Set loading state
        _loginResult.setValue(Result.loading(false));

        // Execute login use case and observe the result
        LoginParams params = new LoginParams(email, password);
        LiveData<Result<Boolean>> resultLiveData = loginUseCase.execute(params);
        resultLiveData.observeForever(result -> {
            if (result != null) {
                _loginResult.setValue(result);
            }
        });
    }

    /**
     * Reset password for the given email.
     *
     * @param email User's email
     */
    public void resetPassword(String email) {
        // Set loading state
        _resetPasswordResult.setValue(Result.loading(false));

        // Execute reset password use case and observe the result
        ResetPasswordParams params = new ResetPasswordParams(email);
        LiveData<Result<Boolean>> resultLiveData = resetPasswordUseCase.execute(params);
        resultLiveData.observeForever(result -> {
            if (result != null) {
                _resetPasswordResult.setValue(result);
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
}
