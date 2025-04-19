package com.erendogan6.planmyworkout.domain.usecase.auth;

import com.erendogan6.planmyworkout.domain.repository.AuthRepository;
import com.erendogan6.planmyworkout.domain.usecase.base.NoParamsUseCase;

import javax.inject.Inject;

/**
 * Use case for signing out a user.
 * This class is part of the domain layer and encapsulates the business logic for user sign out.
 */
public class SignOutUseCase implements NoParamsUseCase<Void> {
    
    private final AuthRepository authRepository;
    
    @Inject
    public SignOutUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }
    
    @Override
    public Void execute() {
        authRepository.signOut();
        return null;
    }
}
