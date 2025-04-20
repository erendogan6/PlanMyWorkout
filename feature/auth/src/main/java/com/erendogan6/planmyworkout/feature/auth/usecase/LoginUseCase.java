package com.erendogan6.planmyworkout.feature.auth.usecase;

import com.erendogan6.planmyworkout.feature.auth.model.AuthResponse;
import com.erendogan6.planmyworkout.feature.auth.model.AuthResult;
import com.erendogan6.planmyworkout.feature.auth.repository.AuthRepository;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;

import javax.inject.Inject;

/**
 * Use case for handling user login.
 * This class encapsulates the business logic for the login operation.
 */
public class LoginUseCase {

    private final AuthRepository authRepository;

    @Inject
    public LoginUseCase(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    /**
     * Executes the login operation with provided email and password.
     * Performs basic validation before calling the repository.
     *
     * @param email    User's email address
     * @param password User's password
     * @return Task containing AuthResponse with either AuthResult or error
     */
    public Task<AuthResponse<AuthResult>> execute(String email, String password) {
        // Basic validation
        if (email == null || email.trim().isEmpty()) {
            return createErrorTask("Email cannot be empty");
        }

        if (password == null || password.trim().isEmpty()) {
            return createErrorTask("Password cannot be empty");
        }

        // Call repository
        return authRepository.login(email, password);
    }

    /**
     * Helper method to create a Task that returns an error AuthResponse.
     *
     * @param errorMessage The error message to include in the response
     * @return Task containing an error AuthResponse
     */
    private Task<AuthResponse<AuthResult>> createErrorTask(String errorMessage) {
        TaskCompletionSource<AuthResponse<AuthResult>> taskSource = new TaskCompletionSource<>();
        taskSource.setResult(new AuthResponse.Error<>(errorMessage, new IllegalArgumentException(errorMessage)));
        return taskSource.getTask();
    }
}