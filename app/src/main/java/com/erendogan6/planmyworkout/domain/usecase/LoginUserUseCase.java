package com.erendogan6.planmyworkout.domain.usecase;

import androidx.lifecycle.LiveData;

import com.erendogan6.planmyworkout.domain.model.Result;
import com.erendogan6.planmyworkout.domain.repository.AuthRepository;

import javax.inject.Inject;

/**
 * Use case for logging in a user.
 * This class is part of the domain layer and encapsulates the business logic for user login.
 */
public class LoginUserUseCase {

    private final AuthRepository authRepository;

    @Inject
    public LoginUserUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    /**
     * Execute the login operation.
     *
     * @param email    User's email
     * @param password User's password
     * @return LiveData emitting Result of login operation
     */
    public LiveData<Result<Boolean>> execute(String email, String password) {
        return authRepository.login(email, password);
    }
}
