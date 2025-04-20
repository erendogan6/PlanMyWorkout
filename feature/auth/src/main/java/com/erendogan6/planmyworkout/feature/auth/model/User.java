package com.erendogan6.planmyworkout.feature.auth.model;

/**
 * Model class representing a user in the application.
 * This class contains user information retrieved from authentication providers.
 */
public class User {
    private final String uid;
    private final String email;
    private final String displayName;
    private final String photoUrl;

    /**
     * Constructor for creating a User object.
     *
     * @param uid         Unique identifier for the user
     * @param email       User's email address
     * @param displayName User's display name, may be null
     * @param photoUrl    URL to user's profile photo, may be null
     */
    public User(String uid, String email, String displayName, String photoUrl) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.photoUrl = photoUrl;
    }

    /**
     * Gets the user's unique identifier.
     *
     * @return The user ID
     */
    public String getUid() {
        return uid;
    }

    /**
     * Gets the user's email address.
     *
     * @return The email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the user's display name.
     *
     * @return The display name, may be null
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the URL to the user's profile photo.
     *
     * @return The photo URL, may be null
     */
    public String getPhotoUrl() {
        return photoUrl;
    }
}