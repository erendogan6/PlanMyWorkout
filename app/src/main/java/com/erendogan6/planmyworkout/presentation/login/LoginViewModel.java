package com.erendogan6.planmyworkout.presentation.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.domain.model.Result;
import com.erendogan6.planmyworkout.domain.usecase.LoginUserUseCase;
import com.erendogan6.planmyworkout.domain.usecase.ResetPasswordUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the Login screen.
 * This class is part of the presentation layer and handles the UI logic for login operations.
 */
@HiltViewModel
public class LoginViewModel extends ViewModel {

    private final LoginUserUseCase loginUserUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;

    private final MutableLiveData<Result<Boolean>> _loginResult = new MutableLiveData<>();
    private final MutableLiveData<Result<Boolean>> _resetPasswordResult = new MutableLiveData<>();

    /**
     * Constructor for LoginViewModel.
     *
     * @param loginUserUseCase     Use case for login operation
     * @param resetPasswordUseCase Use case for password reset operation
     */
    @Inject
    public LoginViewModel(LoginUserUseCase loginUserUseCase, ResetPasswordUseCase resetPasswordUseCase) {
        this.loginUserUseCase = loginUserUseCase;
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
     * @return LiveData of login result
     */
    public LiveData<Result<Boolean>> login(String email, String password) {
        return loginUserUseCase.execute(email, password);
    }

    /**
     * Reset password for the given email.
     *
     * @param email User's email
     * @return LiveData of password reset result
     */
    public LiveData<Result<Boolean>> resetPassword(String email) {
        return resetPasswordUseCase.execute(email);
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
