package com.erendogan6.planmyworkout.domain.usecase.auth;

import com.erendogan6.planmyworkout.domain.repository.AuthRepository;
import com.erendogan6.planmyworkout.domain.usecase.base.NoParamsUseCase;

import javax.inject.Inject;

/**
 * Use case for checking if a user is logged in.
 * This class is part of the domain layer and encapsulates the business logic for checking login status.
 */
public class IsUserLoggedInUseCase implements NoParamsUseCase<Boolean> {
    
    private final AuthRepository authRepository;
    
    @Inject
    public IsUserLoggedInUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }
    
    @Override
    public Boolean execute() {
        return authRepository.isLoggedIn();
    }
}
