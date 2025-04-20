package com.erendogan6.planmyworkout.domain.usecase.auth;

import androidx.lifecycle.LiveData;

import com.erendogan6.planmyworkout.domain.model.Result;
import com.erendogan6.planmyworkout.domain.repository.AuthRepository;
import com.erendogan6.planmyworkout.domain.usecase.base.LiveDataUseCase;

import javax.inject.Inject;

/**
 * Use case for registering a new user.
 * This class is part of the domain layer and encapsulates the business logic for user registration.
 */
public class RegisterUseCase implements LiveDataUseCase<RegisterParams, Result<Boolean>> {
    
    private final AuthRepository authRepository;
    
    @Inject
    public RegisterUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }
    
    @Override
    public LiveData<Result<Boolean>> execute(RegisterParams params) {
        return authRepository.register(params.getEmail(), params.getPassword());
    }
}
