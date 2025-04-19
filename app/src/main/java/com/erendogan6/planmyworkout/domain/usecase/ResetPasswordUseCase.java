package com.erendogan6.planmyworkout.domain.usecase;

import androidx.lifecycle.LiveData;

import com.erendogan6.planmyworkout.domain.model.Result;
import com.erendogan6.planmyworkout.domain.repository.AuthRepository;

import javax.inject.Inject;

/**
 * Use case for resetting a user's password.
 * This class is part of the domain layer and encapsulates the business logic for password reset.
 */
public class ResetPasswordUseCase {

    private final AuthRepository authRepository;

    @Inject
    public ResetPasswordUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    /**
     * Execute the password reset operation.
     *
     * @param email User's email
     * @return LiveData emitting Result of password reset operation
     */
    public LiveData<Result<Boolean>> execute(String email) {
        return authRepository.resetPassword(email);
    }
}
