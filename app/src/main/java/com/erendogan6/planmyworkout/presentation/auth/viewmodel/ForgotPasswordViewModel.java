package com.erendogan6.planmyworkout.presentation.auth.viewmodel;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.erendogan6.planmyworkout.domain.model.Result;
import com.erendogan6.planmyworkout.domain.usecase.auth.ResetPasswordParams;
import com.erendogan6.planmyworkout.domain.usecase.auth.ResetPasswordUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the forgot password screen.
 */
@HiltViewModel
public class ForgotPasswordViewModel extends ViewModel {

    private final ResetPasswordUseCase resetPasswordUseCase;

    private final MutableLiveData<Boolean> resetPasswordResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    @Inject
    public ForgotPasswordViewModel(ResetPasswordUseCase resetPasswordUseCase) {
        this.resetPasswordUseCase = resetPasswordUseCase;
    }

    /**
     * Attempt to send a password reset email.
     * @param email The user's email
     */
    public void resetPassword(String email) {
        isLoading.setValue(true);

        // Create reset password parameters
        ResetPasswordParams params = new ResetPasswordParams(email);

        // Execute reset password use case
        resetPasswordUseCase.execute(params).observeForever(result -> {
            if (result != null) {
                // Handle the result
                if (result.getStatus() == Result.Status.SUCCESS) {
                    resetPasswordResult.postValue(true);
                } else if (result.getStatus() == Result.Status.ERROR) {
                    resetPasswordResult.postValue(false);
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
     * Get the reset password result LiveData.
     * @return LiveData containing the reset password result
     */
    public LiveData<Boolean> getResetPasswordResult() {
        return resetPasswordResult;
    }

    /**
     * Get the loading state LiveData.
     * @return LiveData containing the loading state
     */
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
}
