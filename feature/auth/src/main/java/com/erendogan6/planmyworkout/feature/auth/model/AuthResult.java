package com.erendogan6.planmyworkout.feature.auth.model;

/**
 * Model class representing the result of an authentication operation.
 * This class wraps the User information after successful authentication.
 */
public class AuthResult {
    private final User user;

    /**
     * Constructor for creating an AuthResult object.
     *
     * @param user The authenticated user
     */
    public AuthResult(User user) {
        this.user = user;
    }

    /**
     * Gets the authenticated user.
     *
     * @return The User object containing user information
     */
    public User getUser() {
        return user;
    }
}