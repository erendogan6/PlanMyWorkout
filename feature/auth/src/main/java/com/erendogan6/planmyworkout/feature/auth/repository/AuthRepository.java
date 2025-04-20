package com.erendogan6.planmyworkout.feature.auth.repository;

import com.erendogan6.planmyworkout.feature.auth.model.AuthResponse;
import com.erendogan6.planmyworkout.feature.auth.model.AuthResult;
import com.erendogan6.planmyworkout.feature.auth.model.User;
import com.google.android.gms.tasks.Task;

/**
 * Repository interface for authentication operations.
 * Defines all authentication-related operations that can be performed.
 */
public interface AuthRepository {

    /**
     * Authenticates a user with email and password.
     *
     * @param email    The user's email address
     * @param password The user's password
     * @return Task that resolves to AuthResult containing user information
     */
    Task<AuthResponse<AuthResult>> login(String email, String password);

    /**
     * Registers a new user with email and password.
     *
     * @param email    The user's email address
     * @param password The user's password
     * @return Task that resolves to AuthResult containing newly created user information
     */
    Task<AuthResponse<AuthResult>> register(String email, String password);

    /**
     * Sends a password reset email to the specified address.
     *
     * @param email The email address to send the reset link to
     * @return Task that completes when the email is sent
     */
    Task<AuthResponse<Void>> resetPassword(String email);

    /**
     * Signs out the currently authenticated user.
     */
    void signOut();

    /**
     * Checks if a user is currently logged in.
     *
     * @return true if a user is authenticated, false otherwise
     */
    boolean isUserLoggedIn();

    /**
     * Gets the currently authenticated user.
     *
     * @return The current User object, or null if no user is authenticated
     */
    User getCurrentUser();

    /**
     * Gets the ID of the currently authenticated user.
     *
     * @return The user ID, or null if no user is authenticated
     */
    String getCurrentUserId();
}