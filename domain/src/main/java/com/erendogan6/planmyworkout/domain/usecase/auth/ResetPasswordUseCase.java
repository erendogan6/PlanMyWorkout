package com.erendogan6.planmyworkout.domain.usecase.auth;

import androidx.lifecycle.LiveData;

import com.erendogan6.planmyworkout.domain.model.Result;
import com.erendogan6.planmyworkout.domain.repository.AuthRepository;
import com.erendogan6.planmyworkout.domain.usecase.base.LiveDataUseCase;

import javax.inject.Inject;

/**
 * Use case for resetting a user's password.
 * This class is part of the domain layer and encapsulates the business logic for password reset.
 */
public class ResetPasswordUseCase implements LiveDataUseCase<ResetPasswordParams, Result<Boolean>> {
    
    private final AuthRepository authRepository;
    
    @Inject
    public ResetPasswordUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }
    
    @Override
    public LiveData<Result<Boolean>> execute(ResetPasswordParams params) {
        return authRepository.resetPassword(params.getEmail());
    }
}
