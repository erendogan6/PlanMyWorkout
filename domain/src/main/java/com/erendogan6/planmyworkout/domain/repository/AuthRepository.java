package com.erendogan6.planmyworkout.domain.repository;

import androidx.lifecycle.LiveData;

import com.erendogan6.planmyworkout.domain.model.Result;

/**
 * Repository interface for authentication operations.
 * This interface is part of the domain layer and defines the contract for authentication operations.
 */
public interface AuthRepository {

    /**
     * Login a user with email and password.
     *
     * @param email    User's email
     * @param password User's password
     * @return LiveData emitting Result of login operation
     */
    LiveData<Result<Boolean>> login(String email, String password);

    /**
     * Register a new user with email and password.
     *
     * @param email    User's email
     * @param password User's password
     * @return LiveData emitting Result of registration operation
     */
    LiveData<Result<Boolean>> register(String email, String password);

    /**
     * Send password reset email to the user.
     *
     * @param email User's email
     * @return LiveData emitting Result of password reset operation
     */
    LiveData<Result<Boolean>> resetPassword(String email);

    /**
     * Check if user is currently logged in.
     *
     * @return true if user is logged in, false otherwise
     */
    boolean isLoggedIn();

    /**
     * Sign out the current user.
     */
    void signOut();
}
